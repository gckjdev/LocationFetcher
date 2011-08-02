package com.orange.groupbuy.location.client.api;

public class MapabcAPIImpl implements FetchAPI {

	@Override
	public void fetch(String address, String city) {
		// TODO Auto-generated method stub
		fetchNaviveImpl(address, city);
	}

	public static native void fetchNaviveImpl(String address, String city)/*-{
		// 创建地址解析器实例
		var mls = new $wnd.MGeoCodeSearch();
		var mlsp= new $wnd.MGeoCodeSearchOptions();
		var latResult = $doc.getElementById("latResult");
		var lngResult = $doc.getElementById("lngResult");
		mls.setCallbackFunction(myfunc);
　　		mls.addressToGeocode(address,mlsp);
　　		function myfunc(data){
　　　　		if(data.error_message != undefined){
　　　　　　		alert(data.error_message);
　　　　		}else{
　　　　　　	//解析返回数据
				alert(data.message);
　　　　		}	
　　		}
	}-*/;
}
