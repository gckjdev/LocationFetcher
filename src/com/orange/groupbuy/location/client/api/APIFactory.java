package com.orange.groupbuy.location.client.api;

public class APIFactory {

	public static enum Type{
		BAIDU,GOOGLE,MAP123;
	}
	
	public static FetchAPI create(Type type) {
		switch (type) {
		case BAIDU:
			return new BaiduAPIImpl();
		case GOOGLE:
			return new GoogleAPIImpl();
		}
		throw new RuntimeException("unkonw type for FetchAPI");
	}
}
