package com.orange.groupbuy.location.server.model;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;



import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDBClient {

	public static String ID = "_id";

	Mongo mongo;
	DB db;

	public Mongo getMongo() {
		return mongo;
	}

	public void setMongo(Mongo mongo) {
		this.mongo = mongo;
	}

	public DB getDb() {
		return db;
	}

	public void setDb(DB db) {
		this.db = db;
	}

	public MongoDBClient(String serverAddress, String dbName, String userName,
			String password) {

		try {
			this.mongo = new Mongo(serverAddress, 27017);
		} catch (UnknownHostException e) {
			e.printStackTrace(); // TODO
		} catch (MongoException e) {
			e.printStackTrace(); // TODO
		}
		this.db = mongo.getDB(dbName);
		boolean auth = db.authenticate(userName, password.toCharArray());
		return;
	}

	public boolean insert(String tableName, DBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;
		collection.insert(docObject);
		return true;
	}

	public DBObject findAndModify(String tableName, String fieldName,
			int findValue, int modifyValue) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, findValue);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(fieldName, modifyValue);
		update.put("$set", updateValue);

		return collection.findAndModify(query, null, null, false, update, true,
				false);
	}

	public DBObject findAndModify(String tableName,
			Map<String, Object> equalCondition, Map<String, Object> updateMap) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		// query.put(fieldName, findValue);
		query.putAll(equalCondition);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		// updateValue.put(fieldName, modifyValue);
		updateValue.putAll(updateMap);
		update.put("$set", updateValue);
		// collection.findan
//		System.out.println("query = " + query.toString());
//		System.out.println("update = " + updateValue.toString());
		return collection.findAndModify(query, update);
	}
	
	public Boolean findAndModifySet(String tableName,
			Map<String, Object> equalCondition, Map<String, Object> updateMap) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return false;

		DBObject query = new BasicDBObject();
		// query.put(fieldName, findValue);
		query.putAll(equalCondition);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		// updateValue.put(fieldName, modifyValue);
		updateValue.putAll(updateMap);
		update.put("$addtoset", updateValue);
		// collection.findan
//		System.out.println("query = " + query.toString());
//		System.out.println("update = " + updateValue.toString());
		return true;
	}

	public void save(String tableName, DBObject docObject) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		collection.save(docObject);
		return;
	}

	public DBObject findOne(String tableName, String fieldName,
			String fieldValue) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.findOne(query);
	}
	
	public DBCursor find(String tableName, String fieldName,
			String fieldValue, int limit) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.put(fieldName, fieldValue);
		return collection.find(query).limit(limit);
	}

	public DBObject findOne(String tableName, Map<String, String> fieldValues) {
		if (fieldValues == null)
			return null;

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject query = new BasicDBObject();
		query.putAll(fieldValues);
		return collection.findOne(query);
	}

	public static final int SORT_ASCENDING = 1;
	public static final int SORT_DESCENDING = -1;

	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<String> valueList, String sortFieldName,
			boolean sortAscending, int offset, int limit) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		DBObject orderBy = null;
		if (sortFieldName != null) {
			orderBy = new BasicDBObject();
			if (sortAscending) {
				orderBy.put(sortFieldName, 1);
			} else {
				orderBy.put(sortFieldName, -1);
			}
		}

		DBObject in = new BasicDBObject();
		DBObject query = new BasicDBObject();
		if (fieldName != null && fieldName.trim().length() > 0
				&& valueList != null && valueList.size() > 0) {
			in.put("$in", valueList);
			query.put(fieldName, in);
		}
		DBCursor result;
		if (orderBy != null) {
			result = collection.find(query).sort(orderBy).skip(offset).limit(
					limit);
		} else {
			result = collection.find(query).skip(offset).limit(limit);
		}
		return result;
	}

	public DBCursor findNearby(String tableName, String gpsFieldName,
			double latitude, double longitude, int offset, int count) {

		if (gpsFieldName == null || gpsFieldName.trim().length() == 0) {
			return null;
		}

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;

		List<Double> gpsList = new ArrayList<Double>();
		gpsList.add(latitude);
		gpsList.add(longitude);

		DBObject near = new BasicDBObject();
		near.put("$near", gpsList);
		DBObject query = new BasicDBObject();
		query.put(gpsFieldName, near);

//		System.out.println(query.toString());

		DBCursor result = collection.find(query).skip(offset).limit(count);
		return result;
	}

	public DBCursor findByFieldInValues(String tableName, String fieldName,
			List<Object> valueList, int offset, int count) {

		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return null;
		DBObject in = new BasicDBObject();
		in.put("$in", valueList);
		DBObject query = new BasicDBObject();
		query.put(fieldName, in);
		return collection.find(query).skip(offset).limit(count);
	}

	public DBCursor findByFieldsInValues(String tableName,
			Map<String, List<Object>> fieldValueMap, int offset, int limit) {
		DBCollection collection = db.getCollection(tableName);
		if (collection == null) {
			return null;
		}
		
		
		DBObject query = null;
		if (fieldValueMap != null && fieldValueMap.size() > 0) {
			query = new BasicDBObject();
			for(String field : fieldValueMap.keySet()){
				DBObject in = new BasicDBObject();
				in.put("$in", fieldValueMap.get(field));
				query.put(field, in);
			}
		}
		if (query == null ) {
			return collection.find().skip(offset).limit(limit);
		}
		return collection.find(query).skip(offset).limit(limit);
	}

	public void updateByKey(String tableName, ObjectId keyId, String fieldName,
			List<Double> fieldValue) {
		
		DBCollection collection = db.getCollection(tableName);
		if (collection == null)
			return;

		DBObject query = new BasicDBObject();
		query.put("_id", keyId);

		DBObject update = new BasicDBObject();
		DBObject updateValue = new BasicDBObject();
		updateValue.put(fieldName, fieldValue);
		update.put("$set", updateValue);
		
//		System.out.println("<updateByKey> query = " + query.toString() + ", update = " + updateValue.toString());
		collection.update(query, update);
	}

}
