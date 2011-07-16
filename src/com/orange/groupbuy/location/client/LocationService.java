package com.orange.groupbuy.location.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.orange.groupbuy.location.client.model.PlaceRecord;

@RemoteServiceRelativePath("location")
public interface LocationService extends RemoteService {
	List<PlaceRecord> getPlaceAddress(Date date);

	void savePlaceRecord(PlaceRecord record);
}
