package com.orange.groupbuy.location.client;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.orange.groupbuy.location.client.api.APIFactory;
import com.orange.groupbuy.location.client.api.FetchAPI;
import com.orange.groupbuy.location.client.model.PlaceRecord;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LocationFetcher implements EntryPoint {

	private final LocationServiceAsync locationService = GWT
			.create(LocationService.class);

	/**
	 * change the init value if you want different fetch source
	 */
	//TODO
	private final static APIFactory.Type FETCH_SOURCE_TYPE = APIFactory.Type.SOSO;

	private CellTable<PlaceRecord> cellTable;

	private int processedRow;

	final ListDataProvider<PlaceRecord> dataProvider = new ListDataProvider<PlaceRecord>();

	private FetchAPI fether;

	private static final int REFRESH_INTERVAL = 1000;
	private static final int SAVE_INTERVAL = 1500;

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		initFetcherAPI();

		cellTable = new CellTable<PlaceRecord>(PlaceRecord.KEY_PROVIDER);
		RootPanel.get("locationContainer").add(cellTable);
		cellTable.setWidth("100%", true);
		initTableColumns();
		//
		locationService.getPlaceAddress(new Date(),
				new AsyncCallback<List<PlaceRecord>>() {

					@Override
					public void onFailure(Throwable caught) {
						DialogBox message = new DialogBox();
						message.setTitle("failed to get place");
						message.setText(caught.getMessage());
						message.show();
					}

					@Override
					public void onSuccess(List<PlaceRecord> records) {
						List<PlaceRecord> list = dataProvider.getList();
						for (PlaceRecord palce : records) {
							list.add(palce);
						}
						dataProvider.addDataDisplay(cellTable);
						cellTable.setPageSize(list.size());
					}
				});

		processedRow = -1;
		Timer fetchTimer = new Timer() {
			@Override
			public void run() {
				fetchData();
			}
		};
		fetchTimer.scheduleRepeating(REFRESH_INTERVAL);

		Timer saveTimer = new Timer() {
			@Override
			public void run() {
				saveData();
			}
		};
		saveTimer.scheduleRepeating(SAVE_INTERVAL);
		

	}

	private void initFetcherAPI() {
		fether = APIFactory.create(FETCH_SOURCE_TYPE);
	}

	protected void saveData() {
		List<PlaceRecord> records = dataProvider.getList();
		if(records.size() == 0)
			return;
		String lat = LocationFetcher.getLatResult();
		String lng = LocationFetcher.getLngResult();
		if (processedRow < records.size()) {
			PlaceRecord r = records.get(processedRow);
			r.setLatitude(lat);
			r.setLongitude(lng);

			locationService.savePlaceRecord(r, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {
					GWT.log("save failed");
					DialogBox box = new DialogBox();
					box.setTitle("savePlaceRecord Failed");
					box.setText(caught.getMessage());
					box.show();
				}

				@Override
				public void onSuccess(Void result) {
					GWT.log("save ok");
					// refresh table
					dataProvider.refresh();
					LocationFetcher.setLatResult("");
					LocationFetcher.setLngResult("");
				}
			});
		} else {
			GWT.log("lat and lng is null");
		}
	}

	protected void fetchData() {
		List<PlaceRecord> records = dataProvider.getList();
		String lat = LocationFetcher.getLatResult();
		String lng = LocationFetcher.getLngResult();
		if (lat.isEmpty() && lng.isEmpty()) {
			if (records != null && !records.isEmpty()
					&& ++processedRow < records.size()) {
				PlaceRecord r = records.get(processedRow);
				fether.fetch(r.getAddress(), r.getCity());
			}
			// TODO
			if (processedRow >= records.size()) {
				// let saveData() function quit
				processedRow = Integer.MAX_VALUE;
				locationService.getPlaceAddress(new Date(),
						new AsyncCallback<List<PlaceRecord>>() {

							@Override
							public void onFailure(Throwable caught) {
								DialogBox message = new DialogBox();
								message.setTitle("failed to get palce");
								message.setText(caught.getMessage());
								message.show();
							}

							@Override
							public void onSuccess(List<PlaceRecord> records) {
								List<PlaceRecord> list = dataProvider.getList();
								int len = list.size();
								for (int i = 0; i < len; i++)
									list.remove(0);
								for (PlaceRecord palce : records) {
									list.add(palce);
								}
								dataProvider.flush();
								dataProvider.refresh();
							}
						});
				if (records.size() > 0) {
					processedRow = 0;
					PlaceRecord r = records.get(processedRow);
					fether.fetch(r.getAddress(), r.getCity());
				} 
			}
		}
	}

	private static native String getLngResult() /*-{
		var lngResult = $doc.getElementById("lngResult");
		return lngResult.value;
	}-*/;

	private static native void setLngResult(String lat) /*-{
		var lngResult = $doc.getElementById("lngResult");
		lngResult.value = lat;
	}-*/;

	private static native String getLatResult() /*-{
		var latResult = $doc.getElementById("latResult");
		return latResult.value;
	}-*/;

	private static native void setLatResult(String lng) /*-{
		var latResult = $doc.getElementById("latResult");
		latResult.value = lng;
	}-*/;

	private void initTableColumns() {
		// id
		Column<PlaceRecord, String> idColumn = new Column<PlaceRecord, String>(
				new TextCell()) {
			@Override
			public String getValue(PlaceRecord object) {
				return object.getId();
			}
		};
		cellTable.addColumn(idColumn, "id");
		cellTable.setColumnWidth(idColumn, 60, Unit.PCT);
		// Address.
		String headerString = "address";
		Column<PlaceRecord, String> addressColumn = new Column<PlaceRecord, String>(
				new TextCell()) {
			@Override
			public String getValue(PlaceRecord object) {
				return object.getAddress();
			}
		};
		addColumn(headerString, addressColumn);
		// city
		// lat
		headerString = "city";
		Column<PlaceRecord, String> cityColumn = new Column<PlaceRecord, String>(
				new TextCell()) {
			@Override
			public String getValue(PlaceRecord object) {
				return object.getCity();
			}
		};
		addColumn(headerString, cityColumn);
		// lat
		headerString = "latitude";
		Column<PlaceRecord, String> latColumn = new Column<PlaceRecord, String>(
				new TextCell()) {
			@Override
			public String getValue(PlaceRecord object) {
				return object.getLatitude();
			}
		};
		addColumn(headerString, latColumn);
		// long
		headerString = "longitude";
		Column<PlaceRecord, String> longColumn = new Column<PlaceRecord, String>(
				new TextCell()) {
			@Override
			public String getValue(PlaceRecord object) {
				return object.getLongitude();
			}
		};
		addColumn(headerString, longColumn);
	}

	private void addColumn(String headerString,
			Column<PlaceRecord, String> addressColumn) {
		cellTable.addColumn(addressColumn, headerString);
		cellTable.setColumnWidth(addressColumn, 60, Unit.PCT);
	}

}
