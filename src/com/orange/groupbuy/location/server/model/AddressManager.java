package com.orange.groupbuy.location.server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.orange.groupbuy.location.client.model.PlaceRecord;

public class AddressManager {

	public static List<ProductAddress> findAddressForGPSUpdate(
			MongoDBClient mongoClient, int limit) {

		DBCursor cursor = mongoClient.find(DBConstants.T_IDX_PRODUCT_GPS,
				DBConstants.F_GPS, null, limit);
		if (cursor == null)
			return null;

		try {
			List<ProductAddress> list = new ArrayList<ProductAddress>();
			while (cursor.hasNext()) {
				BasicDBObject obj = (BasicDBObject) cursor.next();
				list.add(new ProductAddress(obj));
				System.out.println("address to be parsed = "+obj.toString());
			}
			cursor.close();
			return list;
		} catch (Exception e) {
			cursor.close();
			e.printStackTrace(); // TODO to be improved
			return null;
		}
	}

	public static boolean findAndUpdateGPS(MongoDBClient mongoClient,
			List<ProductAddress> productAddressList) {
		if (productAddressList == null)
			return false;

		for (ProductAddress address : productAddressList) {
			String cityString = address.getCity();
			String addrString = address.getAddress();
			List<Double> gps = address.getGPS();
			Map<String, Object> equalCondition = new HashMap<String, Object>();
			equalCondition.put(DBConstants.F_ADDRESS, addrString);
			equalCondition.put(DBConstants.F_CITY, cityString);
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put(DBConstants.F_GPS, gps);
			mongoClient.findAndModify(DBConstants.T_IDX_PRODUCT_GPS,
					equalCondition, updateMap);

		}

		return true;
	}
	
	
	public static boolean findAndUpdateProductGPS(MongoDBClient mongoClient,
			List<ProductAddress> productAddressList) {
		if (productAddressList == null)
			return false;

		for (ProductAddress address : productAddressList) {

			String cityString = address.getCity();
			String addrString = address.getAddress();
			List<Double> gps = address.getGPS();
			Map<String, Object> equalCondition = new HashMap<String, Object>();
			equalCondition.put(DBConstants.F_ADDRESS, addrString);
			equalCondition.put(DBConstants.F_CITY, cityString);
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put(DBConstants.F_GPS, gps);
			mongoClient.findAndModifySet(DBConstants.T_PRODUCT,
					equalCondition, updateMap);

		}
		return true;
	}

	public static void findAndUpdateGPSFailure(MongoDBClient mongoclient,
			String id) {
		
		ObjectId objectId = new ObjectId(id);
		List<Double> gpsFailureValue = new LinkedList<Double>();
		gpsFailureValue.add(-1.0);
		gpsFailureValue.add(-1.0);
		mongoclient.updateByKey(DBConstants.T_IDX_PRODUCT_GPS, objectId, DBConstants.F_GPS, gpsFailureValue);
		
	}

}
