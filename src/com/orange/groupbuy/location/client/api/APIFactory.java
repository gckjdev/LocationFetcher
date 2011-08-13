package com.orange.groupbuy.location.client.api;

public class APIFactory {

	public static enum Type{
		BAIDU,GOOGLE,MAPABC,SOSO;
	}
	
	public static FetchAPI create(Type type) {
		switch (type) {
		case BAIDU:
			return new BaiduAPIImpl();
		case GOOGLE:
			return new GoogleAPIImpl();
		case MAPABC:
			return new MapabcAPIImpl();
		case SOSO:
			return new SosoAPIImpl();
		}
		throw new RuntimeException("unkonw type for FetchAPI");
	}
}
