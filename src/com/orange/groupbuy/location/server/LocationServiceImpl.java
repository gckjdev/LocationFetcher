package com.orange.groupbuy.location.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.BasicDBObject;
import com.orange.groupbuy.location.client.LocationService;
import com.orange.groupbuy.location.client.model.PlaceRecord;
import com.orange.groupbuy.location.server.model.AddressManager;
import com.orange.groupbuy.location.server.model.DBConstants;
import com.orange.groupbuy.location.server.model.MongoDBClient;
import com.orange.groupbuy.location.server.model.ProductAddress;

@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements
		LocationService {

	private Logger log = Logger.getLogger(LocationServiceImpl.class.getName());
	public static final String GET_URL = "http://maps.google.com/maps/api/geocode/xml?address=";
	public static final MongoDBClient mongoClient = new MongoDBClient("groupbuy");
	
	static AtomicInteger failCounter = new AtomicInteger(0);
	static AtomicInteger successCounter = new AtomicInteger(0);
	static AtomicInteger totalCounter = new AtomicInteger(0);
	static AtomicInteger googleCounter = new AtomicInteger(0);
	static String dateString = "";
	@Override
	public List<PlaceRecord> getPlaceAddress(Date date) {

		List<PlaceRecord> records = new ArrayList<PlaceRecord>();
		final int limit = 10;
		// read address and city from address table
		List<ProductAddress> list = AddressManager.findAddressForGPSUpdate(mongoClient, limit);
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			ProductAddress item = (ProductAddress) iter.next();
			String addr = (String) item.getAddress();
			String city = (String) item.getCity();
			String id = (String) item.getStringObjectId();
			records.add(new PlaceRecord(id, addr, city));
		}
		
		
		return records;

	}

	public boolean tryGoogleParsing(PlaceRecord record){
		if (record == null)
			return false;
		
		String lngString = record.getLongitude();
		String latString = record.getLatitude();
		if (lngString != null && !lngString.isEmpty() && latString != null && !latString.isEmpty()) {
			// already has latitude/longitude, return success
			return true;
		} else {
			int googleCounts = googleCounter.incrementAndGet();
			
			if(reachMaxGoogleRequest(googleCounts))
				return false;

			String address = updateAddress(record.getAddress(), record.getCity());
			List<Double> latlngList = parseAddressByGoogleAPI(address);
			if(latlngList == null || latlngList.size() < 2){
				// google parsing failure, return failure
				return false;
			}
			else {
				// google parsing success, return success
				double lat = latlngList.get(0);
				double lng = latlngList.get(1);
				record.setLatitude(String.valueOf(lat));
				record.setLongitude(String.valueOf(lng));
				return true;
			}
		}
	}
	
	
	private boolean reachMaxGoogleRequest(int googleCounts) {
		final int MAX_GOOGLE_REQUEST = 1500;
		Date currentDate = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String currentDateString = format.format(currentDate);	
		synchronized(dateString){
			if(dateString.isEmpty()){
				dateString = currentDateString;
				log.info("<reachMaxGoogleRequest> dateString="+dateString);
				return false;
			} else {
				if(dateString.equals(currentDateString)){
					if(googleCounts > MAX_GOOGLE_REQUEST){
						log.info("<reachMaxGoogleRequest> numers of parser reach "+MAX_GOOGLE_REQUEST);
						return true;
					}
					log.info("<tryGoogleParsing> googleCounts="+googleCounts);
					return false;
				} else {
					dateString = currentDateString;
					googleCounter.set(0);
					log.info("<reachMaxGoogleRequest> dateString="+dateString);
					return false;
				}
			}
		}	
	}

	private String updateAddress(String address, String city) {
		
		if (address == null)
			return address;
		
		int indexForCity = address.indexOf("市");
		int indexForProvince = address.indexOf("省");
		if(indexForCity != -1 || indexForProvince != -1)
			return address;
		else {
			if (city == null)
				return address;
			
			String updateAddress = city.concat("市").concat(address);
			return updateAddress;
		}
	}

	@Override
	public void savePlaceRecord(PlaceRecord record) {
		
		try{
		printStatistic();
		//TODO
		boolean result = tryGoogleParsing(record);
		if (!result || !isValidGps(record.getDoubleLatitude(), record.getDoubleLongitude())){
			// update to failure status, never load the record again
			AddressManager.findAndUpdateGPSFailure(mongoClient, record.getId());
			incFailCounter();
			return;
		}
		
		// set data
		List<ProductAddress> list = new LinkedList<ProductAddress>();
		ProductAddress productAddress = new ProductAddress(new BasicDBObject());
		productAddress.setCity(record.getCity());
		productAddress.setAddress(record.getAddress());
		productAddress.setGPS(record.getDoubleLongitude(), record.getDoubleLatitude());
		list.add(productAddress);
		
		// update address table
		if (!AddressManager.findAndUpdateGPS(mongoClient, list)) {
			log.severe("<savePlaceRecord> fail to findAndUpdateGPS, product address = "+productAddress.toString());
			incFailCounter();
			return;
		}
		
		// update gps to product table
		if (!AddressManager.findAndUpdateProductGPS(mongoClient, list)) {
			log.severe("<savePlaceRecord> fail to findAndUpdateProductGPS, product address = "+productAddress.toString());
			return;
		}
		else{
			incSucessCounter();
		}
		}catch (Exception e){
			log.severe("<savePlaceRecord> but catch exception = "+e.toString());
			e.printStackTrace();
		}

	}

	private boolean isValidGps(double latitude, double longitude) {
		if (latitude >= 180.0f || latitude < -180.0f ||
			longitude >= 180.0f || longitude < -180.0f	)
			return false;
		return true;
	}

	public List<Double> parseAddressByGoogleAPI(String address) {

		if (address == null || address.isEmpty())
			return null;
		try {
			
			String getURL = GET_URL.concat(URLEncoder.encode(address, "utf-8")).concat("&sensor=false");
//			log.info("<parseAddressByGoogleAPI> url="+getURL);
			
			URL getUrl = new URL(getURL);
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(getUrl);
			
			if (doc == null)
				return null;
			
			Element root = doc.getRootElement();
			if (root == null)
				return null;
			
			Element data = getFieldElement(root, "result", "geometry", "location");
			if (data == null)
				return null;
			
			String lat = getFieldValue(data, "lat");
			String lng = getFieldValue(data, "lng");
			if(lat == null || lat.isEmpty() || lng == null || lng.isEmpty())
				return null;
			
			List<Double> list = new LinkedList<Double>();
			list.add(Double.parseDouble(lat));
			list.add(Double.parseDouble(lng));
			
			log.info("<parseAddressByGoogleAPI> address="+address+", result = "+lat+", "+lng);
			return list;
		} catch (Exception e) {
			log.info("<parseAddressByGoogleAPI> catch exception = "+e.toString());
			//e.printStackTrace();
			return null;
		}
	}

	private static Element getFieldElement(Element e, String... fieldNames) {

		List<?> elementList = getFieldBlock(e, fieldNames);
		if (elementList == null)
			return null;

		Iterator<?> it = elementList.iterator();
		if (it.hasNext() == false)
			return null;

		return (Element) it.next();
	}

	private static List<?> getFieldBlock(Element e, String... fieldNames) {

		if (fieldNames == null || fieldNames.length == 0)
			return null;

		List<?> elementList = e.getChildren(fieldNames[0]);
		for (int i = 1; i < fieldNames.length; i++) {

			if (elementList == null)
				return null;

			Iterator<?> it = elementList.iterator();
			if (it.hasNext() == false)
				return null;

			Element firstElement = (Element) it.next();
			if (firstElement == null)
				return null;

			elementList = firstElement.getChildren(fieldNames[i]);
		}
		return elementList;
	}

	private static String getFieldValue(Element e, String fieldName) {
		Element subElement = e.getChild(fieldName);
		if (subElement == null)
			return null;
		if (subElement.getText().trim().equals(""))
			return null;
		else
			return subElement.getText().trim();
	}
	
	private void incSucessCounter() {
		successCounter.incrementAndGet();
		totalCounter.incrementAndGet();
	}
	
	private void incFailCounter() {
		failCounter.incrementAndGet();
		totalCounter.incrementAndGet();
	}
	
	public void printStatistic(){
		if (totalCounter.get() % 10 == 0){
			log.info("total="+totalCounter.get()+",succ="+successCounter.get()+",fail="+failCounter.get());
		}
	}

}
