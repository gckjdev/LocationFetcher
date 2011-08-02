package com.orange.groupbuy.location.server.model;

import java.util.ArrayList;
import java.util.HashMap;
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

			List<Double> gps = address.getGPS();
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put(DBConstants.F_GPS, gps);
			
			List<ObjectId> productIdList = address.getProductList();
			if (productIdList != null && productIdList.size() > 0){			
				System.out.println("<debug> update product id list");
				mongoClient.findAndModifySet(DBConstants.T_PRODUCT, DBConstants.F_ID, productIdList, updateMap);
			}
			else{
				System.out.println("<debug> update product id list, but list is null, address="+address.getDbObject().toString());				
			}			
		}
		return true;
	}

}
