package com.orange.groupbuy.location.server.model;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ProductAddress {
	
	BasicDBObject dbObject = new BasicDBObject();
	
	public DBObject getDbObject() {
		return dbObject;
	}

	public void setDbObject(BasicDBObject dbObject) {
		this.dbObject = dbObject;
	}

	public ProductAddress(BasicDBObject obj) {
		dbObject = obj;
	}

	public void setGPS(double longitude, double latitude){
		List<Double> gps = new ArrayList<Double>();
		gps.add(Double.valueOf(latitude));
		gps.add(Double.valueOf(longitude));
		
		getDbObject().put(DBConstants.F_GPS, gps);
	}
	
	public List<Double> getGPS(){
		return (List<Double>)this.dbObject.get(DBConstants.F_GPS);
	}
	
	
	public String getCity() {
		return (String)this.dbObject.get(DBConstants.F_CITY);
	}
	
	public void setCity(String city) {
		this.dbObject.put(DBConstants.F_CITY, city);
	}
	
	public String getAddress() {
		return (String)this.dbObject.get(DBConstants.F_ADDRESS);
	}
	
	public void setAddress(String address) {
		this.dbObject.put(DBConstants.F_ADDRESS, address);
	}

	public String getStringObjectId() {
		// TODO Auto-generated method stub
		return dbObject.getString(DBConstants.F_ID);
	}
	
		
}
	
