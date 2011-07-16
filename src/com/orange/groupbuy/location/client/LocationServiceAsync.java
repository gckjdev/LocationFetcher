package com.orange.groupbuy.location.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.orange.groupbuy.location.client.model.PlaceRecord;

public interface LocationServiceAsync {

	void getPlaceAddress(Date date, AsyncCallback<List<PlaceRecord>> callback);

	void savePlaceRecord(PlaceRecord record, AsyncCallback<Void> callback);

}
