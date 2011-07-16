package com.orange.groupbuy.location.client.model;

import java.io.Serializable;

import com.google.gwt.view.client.ProvidesKey;

public class PlaceRecord implements Serializable {

	private static final long serialVersionUID = -9034213772392656023L;

	public static final ProvidesKey<PlaceRecord> KEY_PROVIDER = new ProvidesKey<PlaceRecord>() {
		@Override
		public Object getKey(PlaceRecord item) {
			return item == null ? null : item.getId();
		}
	};
	private String id;
	private String address;
	private String city;
	private String latitude;
	private String longitude;

	public PlaceRecord() {

	}

	public PlaceRecord(String id, String address) {
		this.id = id;
		this.address = address;
	}

	public PlaceRecord(String id, String address, String city) {
		this.id = id;
		this.address = address;
		this.city = city;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
}
