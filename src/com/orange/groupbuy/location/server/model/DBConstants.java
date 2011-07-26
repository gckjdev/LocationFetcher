package com.orange.groupbuy.location.server.model;


public class DBConstants {


	public static final String D_GROUPBUY = "groupbuy";

	// tables
	public static final String T_USER = "user";
	public static final String T_FETCH_TASK = "task"; 
	public static final String T_PRODUCT = "product";
	public static final String T_IDX_PRODUCT_GPS = "address";
	// fields
	public static final String F_TASK_STATUS = "status";
	public static final String F_TASK_URL = "url";
	public static final String F_TASK_FILE_PATH = "path";
	public static final String F_TASK_PARSER_TYPE = "parser";
	public static final String F_TASK_SITE_ID = "site_id";

	
	// constants
	public static final int C_TASK_STATUS_NOT_RUNNING = 0;
	public static final int C_TASK_STATUS_RUNNING = 1;
	public static final int C_TASK_STATUS_DOWNLOAD_OK = 2;
	public static final int C_TASK_STATUS_CLOSE = 3;
	
	// major product or not
	public static final int C_NOT_MAJOR = 0;
	public static final int C_IS_MAJOR = 1;
	
	public static final int C_CATEGORY_UNKNOWN = 0;
	public static final int C_CATEGORY_EAT = 1;
	public static final int C_CATEGORY_FUN = 2;
	public static final int C_CATEGORY_FACE = 3;
	public static final int C_CATEGORY_SHOPPING = 4;
	public static final int C_CATEGORY_KEEPFIT = 5;
	public static final int C_CATEGORY_LIFE = 6;
	
	// site ID
	public static final String C_SITE_MEITUAN = "meituan";
	public static final String C_SITE_DIANPIAN = "dianping";
	public static final String C_SITE_WOWO = "wowo";
	public static final String C_SITE_58 = "58";
	public static final String C_SITE_24QUAN = "24quan";
	public static final String C_SITE_FTUAN = "ftuan";
	public static final String C_SITE_MANZUO = "manzuo";
	public static final String C_SITE_GAOPENG = "gaopeng";
	public static final String C_SITE_DIDA = "dida";
	public static final String C_SITE_NUOMI = "nuomi";
	public static final String C_SITE_GANJI = "ganji";
	public static final String C_SITE_KAIXIN = "kaixin";
	public static final String C_SITE_XING800 = "xing800";
	public static final String C_SITE_FANTONG = "fantong";
	public static final String C_SITE_LASHOU = "lashou";
	public static final String C_SITE_QUNAER = "qunaer";
	public static final String C_SITE_JUQI = "juqi";
	public static final String C_SITE_TUANBAO = "tuanbao";
	public static final String C_SITE_JUMEIYOUPIN = "jumeiyoupin";
	public static final String C_SITE_TUANHAO = "tuanhao";

//	user("device_id","device_model","device_os","device_token","language",
//	"country_code","create_date","source_id","subscribe")
	
	// DB User Fields
	public static final String F_USERID = "_id";
	public static final String F_LOGINID = "login_id";
	public static final String F_APPID = "app_id";
	public static final String F_DEVICEID = "device_id";
	public static final String F_DEVICEMODEL = "device_model";
	public static final String F_DEVICEOS = "device_os";
	public static final String F_DEVICETOKEN = "device_token";
	public static final String F_LANGUAGE = "language";
	public static final String F_COUNTRYCODE = "country_code";
	public static final String F_CREATE_SOURCE_ID = "source_id";

	public static final String F_CREATE_DATE = "create_date";

	public static final String F_EMAIL = "email";
	public static final String F_MOBILE = "mobile";
	public static final String F_PASSWORD = "password";
	public static final String F_STATUS = "status";

	public static final String F_NICKNAME = "nickName";
	public static final String F_AVATAR = "avatar";
	public static final String F_SINAID = "sina_id";
	public static final String F_QQID = "qq_id";
	public static final String F_RENRENID = "renren_id";
	public static final String F_FACEBOOKID = "facebook_id";
	public static final String F_TWITTERID = "twitter_id";

	public static final String F_SINA_ACCESS_TOKEN = "sina_at";
	public static final String F_SINA_ACCESS_TOKEN_SECRET = "sina_ats";
	public static final String F_QQ_ACCESS_TOKEN = "qq_at";
	public static final String F_QQ_ACCESS_TOKEN_SECRET = "qq_ats";

	public static final String F_PROVINCE = "province";
	public static final String F_LOCATION = "location";
	public static final String F_GENDER = "gender";
	public static final String F_BIRTHDAY = "birthday";
	public static final String F_SINA_NICKNAME = "sina_nick";
	public static final String F_SINA_DOMAIN = "sina_domain";
	public static final String F_QQ_NICKNAME = "qq_nick";
	public static final String F_QQ_DOMAIN = "qq_domain";

	// DB Place Fields
	public static final String F_PLACEID = "placeId";
	public static final String F_LONGITUDE = "long";
	public static final String F_LATITUDE = "lat";
	public static final String F_NAME = "name";
	public static final String F_RADIUS = "radius";
	public static final String F_POST_TYPE = "postType";
	public static final String F_DESC = "desc";
	public static final String F_PLACE_TYPE = "type";
	public static final String F_AUTH_FLAG = "auth";

	// DB Post Fields
	public static final String F_POSTID = "postId";
	public static final String F_USER_LONGITUDE = "userLong";
	public static final String F_USER_LATITUDE = "userLat";
	public static final String F_TEXT_CONTENT = "text";
	public static final String F_CONTENT_TYPE = "type";
	public static final String F_IMAGE_URL = "image";
	public static final String F_TOTAL_VIEW = "totalView";
	public static final String F_TOTAL_FORWARD = "totalForward";
	public static final String F_TOTAL_QUOTE = "totalQuote";
	public static final String F_TOTAL_REPLY = "totalReply";
	public static final String F_SRC_POSTID = "srcPostId";
	public static final String F_REPLY_POSTID = "replyPostId";

	//DB App fields
//	public static final String F_APPID = "appid";
//	public static final String F_NAME = "name";
//	public static final String F_DESC = "desc";

	public static final String F_APPURL = "appUrl";
	public static final String F_VERSION = "version";
	public static final String F_ICON = "icon";

	public static final String F_SINA_APPKEY = "sinaAppKey";
	public static final String F_SINA_APPSECRET = "sinaAppSecret";
	public static final String F_QQ_APPKEY = "qqAppKey";
	public static final String F_QQ_APPSECRET = "qqAppSecret";
	public static final String F_RENREN_APPKEY = "renrenAppKey";
	public static final String F_RENREN_APPSECRET = "renrenAppSecret";
	// computed fields, not stored in DB
	public static final String C_TOTAL_RELATED = "totalRelatedPost";

	public static final String F_MESSAGEID = "messageId";
	public static final String F_MESSAGE_CONTENT = "text";
	public static final String F_FROM_USERID = "fromUserId";
	public static final String F_TO_USERID = "toUserId";
	public static final String F_SRC_MESSAGEID = "srcMessageId";

	public static final String F_MESSAGE_TYPE = "messageType";

	// Value
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_SUSPEND = "2";

	// Constants
	public static final int LOGINID_OWN = 1;
	public static final int LOGINID_SINA = 2;
	public static final int LOGINID_QQ = 3;
	public static final int LOGINID_RENREN = 4;
	public static final int LOGINID_FACEBOOK = 5;
	public static final int LOGINID_TWITTER = 6;

	public static final int CONTENT_TYPE_TEXT = 1;
	public static final int CONTENT_TYPE_TEXT_PHOTO = 2;

	public static final String AUTH_FLAG_NONE = "0";

	public static final String PLACE_TYPE_UNKNOWN = "0";

	// TODO: CommonManager.java
	public static int UNLIMITED_COUNT = 9999999;

	// normal statistic Constants
	public static final String USER_SIMILARITY = "place_user_similarity";
	public static final String USER_POST_STATISTIC = "place_user_post_stat";

	public static final String F_USER_POST_STATISTIC_TOTOAL = "totalPost";

	// DB UPDATE Row Key
	public static final String R_UPDATE_DATA = "update_data";
	public static final String R_RECOMMEND_APPS = "recommend_apps";

	// appID type
	public static final String R_APPID_ALL = "ALL";
	
	public static final String F_LOC = "loc";
	public static final String F_WAP_LOC = "wap_loc";
	
	public static final String F_SITE_NAME = "site_name";
	public static final String F_SITE_URL = "site_url";
	public static final String F_SITE_ID = "site_id";	
	
	public static final String F_TITLE = "title";
	public static final String F_DESCRIPTION = "desc";
	public static final String F_DETAIL = "detail";	
	
	public static final String F_IMAGE = "image";
	public static final String F_START_DATE = "start_date";
	public static final String F_END_DATE = "end_date";
	public static final String F_VALUE = "value";
	public static final String F_PRICE = "price";
	public static final String F_REBATE = "rebate";
	public static final String F_BOUGHT = "bought";

	public static final String F_CATEGORY = "category";
	public static final String F_MAJOR = "major";
	
	public static final String F_CITY = "city";

	public static final String F_VENDOR = "vendor";
	public static final String F_SHOP = "shop";
	public static final String F_ADDRESS = "address";
	public static final String F_GPS = "gps";
	public static final String F_RANGE = "range";
	public static final String F_DP_SHOPID = "dpid";
	public static final String F_TEL = "tel";

	public static final String F_COMMENTS = "comments";

	public static final String F_KEYWORD = "keyword";

	public static final String F_PRODUCTID = "product_id";
	
	public static final String V_NATIONWIDE = "ȫ��";

	public static final String F_ID = "_id";


	//user table fields
	
//	user("device_id","device_model","device_os","device_token","language",
//			"country_code","create_date","create_source_id","subscribe")
	
//	public static final String F_DEVICEID = "device_id";
	
	
	





}
