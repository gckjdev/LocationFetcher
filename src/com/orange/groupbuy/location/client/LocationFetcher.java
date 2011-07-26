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
	private final static APIFactory.Type FETCH_SOURCE_TYPE = APIFactory.Type.BAIDU;

	private CellTable<PlaceRecord> cellTable;

	private int processedRow;

	final ListDataProvider<PlaceRecord> dataProvider = new ListDataProvider<PlaceRecord>();

	private FetchAPI fether;

	private static final int REFRESH_INTERVAL = 1000;
	private static final int SAVE_INTERVAL = 1300;
	private Boolean first_bool = true;

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
						message.setTitle("failed to get palce");
						message.setText(caught.getMessage());
						message.show();
					}

					@Override
					public void onSuccess(List<PlaceRecord> records) {
						for (PlaceRecord palce : records) {
							addPlace(palce);
						}
						dataProvider.addDataDisplay(cellTable);
						// TODO: only for debug, not suitable for huge data.
						// cellTable.setPageSize(records.size());
					}
				});

		processedRow = 0;
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
		String lat = LocationFetcher.getLatResult();
		String lng = LocationFetcher.getLngResult();
		if (!lat.isEmpty() && !lng.isEmpty() && processedRow < records.size()) {
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
				if (first_bool) {
					processedRow--;
					first_bool = false;
				}
				PlaceRecord r = records.get(processedRow);
				fether.fetch(r.getAddress(), r.getCity());
			}
			// TODO
			if (processedRow >= records.size()) {
				processedRow = 0;
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
								for (PlaceRecord palce : records) {
									addPlace(palce);
								}
								// TODO: only for debug, not suitable for huge
								// cellTable.setPageSize(records.size());
							}
						});
				PlaceRecord r = records.get(processedRow);
				fether.fetch(r.getAddress(), r.getCity());
				processedRow++;
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

	public void addPlace(PlaceRecord contact) {
		List<PlaceRecord> records = dataProvider.getList();
		// Remove the contact first so we don't add a duplicate.
		records.remove(contact);
		records.add(contact);
	}
}
