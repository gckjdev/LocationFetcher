package com.orange.groupbuy.location.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.jdt.core.compiler.IScanner;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.orange.groupbuy.location.client.LocationService;
import com.orange.groupbuy.location.client.model.PlaceRecord;
import com.orange.groupbuy.location.server.model.AddressManager;
import com.orange.groupbuy.location.server.model.DBConstants;
import com.orange.groupbuy.location.server.model.MongoDBClient;
import com.orange.groupbuy.location.server.model.ProductAddress;

@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements
		LocationService {

	static final MongoDBClient mongoClient = new MongoDBClient("localhost", "groupbuy", "", "");
	
	private Logger log = Logger.getLogger(LocationServiceImpl.class.getName());
	@Override
	public List<PlaceRecord> getPlaceAddress(Date date) {

		List<PlaceRecord> records = new ArrayList<PlaceRecord>();
		// read address and city from address table
		
		List<ProductAddress> list = AddressManager.findAddressForGPSUpdate(mongoClient, 50);
		Iterator<?> iter = list.iterator();
		while(iter.hasNext()){
			ProductAddress item = (ProductAddress)iter.next();
			String addr = item.getAddress();
			String city = item.getCity();
			String id   = item.getStringObjectId();
			List<String> productIdList = item.getProductStringList();
			records.add(new PlaceRecord(id, addr, city, productIdList));
		}
		
		return records;
		
	}

	@Override
	public void savePlaceRecord(PlaceRecord record) {
		// update gps to address table
		List<ProductAddress> list = new LinkedList<ProductAddress>();
		ProductAddress productAddress = new ProductAddress(new BasicDBObject());
		productAddress.setCity(record.getCity());
		productAddress.setAddress(record.getAddress());
		productAddress.setProductIdList(record.getProductIdList());
		double lng = Double.valueOf(record.getLongitude());
		double lat = Double.valueOf(record.getLatitude());
		productAddress.setGPS(lng, lat);
		list.add(productAddress);
		if(!AddressManager.findAndUpdateGPS(mongoClient, list)) {
			log.info("ProductAddress list is null");
		}
		// update gps to product table
		if(!AddressManager.findAndUpdateProductGPS(mongoClient, list)) {
			log.info("ProductAddress list is null");
		}
		
		// TODO debug
		String message = String.format("id=%1s, latitude=%2s, longitude=%3s",
				record.getId(), record.getLatitude(), record.getLongitude());
		System.out.print(message);
		log.warning(message);
		
		
		
	}

}
