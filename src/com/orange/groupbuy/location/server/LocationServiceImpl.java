package com.orange.groupbuy.location.server;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.orange.groupbuy.location.client.LocationService;
import com.orange.groupbuy.location.client.model.PlaceRecord;

@SuppressWarnings("serial")
public class LocationServiceImpl extends RemoteServiceServlet implements
		LocationService {

	private Logger log = Logger.getLogger(LocationServiceImpl.class.getName());
	@Override
	public List<PlaceRecord> getPlaceAddress(Date date) {
		List<PlaceRecord> records = new ArrayList<PlaceRecord>();
		String[] address = new String[] { "丰台区六里桥甲 1号5栋1层", "丰台区开阳路1号 瀚海花园大厦3层" };
		String[] city = new String[] { "北京市", "北京市" };
		
		int id = 0;
		for (int i = 0; i < address.length; i++) {
			String addr = address[i];
			String ci = city[i];
			records.add(new PlaceRecord(String.valueOf(id), addr, ci));
			id++;
		}
		return records;
	}

	@Override
	public void savePlaceRecord(PlaceRecord record) {
		String message = String.format("id=%1s, latitude=%2s, longitude=%3s",
				record.getId(), record.getLatitude(), record.getLongitude());
		System.out.print(message);
		log.warning(message);
	}

}
