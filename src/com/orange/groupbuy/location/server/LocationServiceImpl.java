package com.orange.groupbuy.location.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.google.gwt.core.client.GWT;
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

	@Override
	public List<PlaceRecord> getPlaceAddress(Date date) {

		List<PlaceRecord> records = new ArrayList<PlaceRecord>();
		// read address and city from address table
		final int limit = 10;
		MongoDBClient mongoClient = new MongoDBClient("localhost", "groupbuy",
				"", "");
		List<ProductAddress> list = AddressManager.findAddressForGPSUpdate(
				mongoClient, limit);
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			ProductAddress item = (ProductAddress) iter.next();
			String addr = item.getAddress();
			String city = item.getCity();
			String id = item.getStringObjectId();
			records.add(new PlaceRecord(id, addr, city));
		}

		return records;

	}

	@Override
	public void savePlaceRecord(PlaceRecord record) {
		// update gps to address table
		MongoDBClient mongoClient = new MongoDBClient("localhost", "groupbuy",
				"", "");
		List<ProductAddress> list = new LinkedList<ProductAddress>();
		ProductAddress productAddress = new ProductAddress(new BasicDBObject());
		productAddress.setCity(record.getCity());
		productAddress.setAddress(record.getAddress());
		String lngString = record.getLongitude();
		String latString = record.getLatitude();
		if (!lngString.isEmpty() && !latString.isEmpty()) {
			double lng = Double.valueOf(record.getLongitude());
			double lat = Double.valueOf(record.getLatitude());
			productAddress.setGPS(lng, lat);
		} else {
			// TODO
			List<Double> latlngList = GoogleAPIImpl(record.getAddress());
			if(latlngList == null){
				productAddress.setGPS(0, 0);
			}
			else {
				double lat = latlngList.get(0);
				double lng = latlngList.get(1);
				productAddress.setGPS(lng, lat);
			}
		}

		list.add(productAddress);
		if (!AddressManager.findAndUpdateGPS(mongoClient, list)) {
			log.info("ProductAddress list is null");
		}
		// update gps to product table
		if (!AddressManager.findAndUpdateProductGPS(mongoClient, list)) {
			log.info("ProductAddress list is null");
		}

	}

	public List<Double> GoogleAPIImpl(String address) {

		if (address == null || address.isEmpty())
			return null;
		try {
			
			String getURL = GET_URL.concat(URLEncoder.encode(address, "utf-8")).concat("&sensor=false");
			log.info("<debug> url="+getURL);
			//getURL = URLEncoder.encode(getURL, "utf-8");
			URL getUrl = new URL(getURL);
			SAXBuilder sb = new SAXBuilder();
			Document doc = sb.build(getUrl);
			Element root = doc.getRootElement();
			Element data = getFieldElement(root, "result", "geometry", "location");
			String lat = getFieldValue(data, "lat");
			String lng = getFieldValue(data, "lng");
			if(lat.isEmpty() || lng.isEmpty())
				return null;
			List<Double> list = new LinkedList<Double>();
			list.add(Double.parseDouble(lat));
			list.add(Double.parseDouble(lng));
			
			log.info("<debug> google result = "+lat+", "+lng);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Element getFieldElement(Element e, String... fieldNames) {

		List<?> elementList = getFieldBlock(e, fieldNames);
		if (elementList == null)
			return null;

		Iterator<?> it = elementList.iterator();
		if (it.hasNext() == false)
			return null;

		return (Element) it.next();
	}

	public static List<?> getFieldBlock(Element e, String... fieldNames) {

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

	public static String getFieldValue(Element e, String fieldName) {
		Element subElement = e.getChild(fieldName);
		if (subElement == null)
			return null;
		if (subElement.getText().trim().equals(""))
			return null;
		else
			return subElement.getText().trim();
	}
	
	
	

}
