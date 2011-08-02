package com.orange.groupbuy.location.client.api;

import org.jdom.Element;

public class BaiduAPIImpl implements FetchAPI {

	@Override
	public void fetch(String address, String city) {
		fetchNaviveImpl(address, city);
	}

	private String updateAddress(String address, String city) {
		int index = address.indexOf("市");
		if(index != -1)
			return address;
		else {
			String updateAddress = city.concat("市").concat(address);
			return address;
		}
		
	}

	public static native void fetchNaviveImpl(String address, String city)/*-{
		//var map = new $wnd.BMap.Map("container");
		// 创建地址解析器实例
		var myGeo = new $wnd.BMap.Geocoder();
		var latResult = $doc.getElementById("latResult");
		var lngResult = $doc.getElementById("lngResult");
		// 将地址解析结果显示在地图上,并调整地图视野
		myGeo.getPoint(address, function(point) {
			if (point) {
				latResult.value = point.lat;
				lngResult.value = point.lng;
			}
		}, city);
	}-*/;
	
	
	
}
