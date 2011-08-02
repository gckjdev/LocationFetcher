package com.orange.groupbuy.location.client.api;

public class APIFactory {

	public static enum Type{
		BAIDU,GOOGLE,MAPABC;
	}
	
	public static FetchAPI create(Type type) {
		switch (type) {
		case BAIDU:
			return new BaiduAPIImpl();
		case GOOGLE:
			return new GoogleAPIImpl();
		case MAPABC:
			return new MapabcAPIImpl();
		}
		throw new RuntimeException("unkonw type for FetchAPI");
	}
}
