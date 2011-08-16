package com.orange.groupbuy.location.client.api;

import com.google.gwt.core.client.GWT;

public class SosoAPIImpl implements FetchAPI {

	@Override
	public void fetch(String address, String city) {
		address = addCitytoAddress(address, city);
		GWT.log("<SosoAPIImpl> address="+address);
		fetchNaviveImpl(address, city);	
	}
	
	private String addCitytoAddress(String address, String city) {
		if (address.contains("市"))
			return address;
		else
			address = city.concat("市").concat(address);
		return address;	
	}

	public static native void fetchNaviveImpl(String address, String city)/*-{
	// 创建地址解析器实例
	var myGeo = new $wnd.soso.maps.Geocoder();
	var latResult = $doc.getElementById("latResult");
	var lngResult = $doc.getElementById("lngResult");
	// 将地址解析结果显示在地图上,并调整地图视野
	myGeo.geocode({'address': address}, function(results, status) {
    	if (status == $wnd.soso.maps.GeocoderStatus.OK) {
	 		var location = results.location;
	 		latResult.value = location.getLat();
	 		lngResult.value = location.getLng(); 	
         } else {
         	// TODO
//         	alert("检索没有结果，原因: " + status + ",address=" + address);
         }
    });
	}-*/;

}
