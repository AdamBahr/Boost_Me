package com.appdupe.flamer.utility;

public class Constant {
	// Update the server End point here

	// private static String urlPath =
	// "http://www.skcript.com/appdupe/final/flamer/";

	// private static String urlPath =
	// "http://192.168.0.114/PHP_ServerF/phpserver/";

	private static final String urlPath = "http://elluminati.in/Flamer/";

	public static String hostUrl = urlPath + "process.php/";

	public static final String login_url = hostUrl + "login";
	public static final String UpdatePrefrence_url = hostUrl + "updateEntity";
	public static final String uploadImage_url = hostUrl + "uploadImage";
	public static final String findMatch_url = hostUrl + "findMatches";
	public static final String getProfile_url = hostUrl + "getProfile";
	public static final String editUseProfile_url = hostUrl + "editProfile";
	public static final String getliked_url = hostUrl + "getProfileMatches";
	public static final String imagedelete_url = hostUrl + "deleteImage";
	public static final String UpdateToken_url = hostUrl + "updateSession";
	public static final String inviteaction_url = hostUrl + "inviteAction";
	public static final String updatePrefrence_url = hostUrl
			+ "updatePreferences";
	public static final String logout_url = hostUrl + "logout";
	public static final String deleteUserAccount_url = hostUrl
			+ "deleteAccount";
	public static final String uploadChunk_url = hostUrl + "uploadChunk";

	// Added
	public static final String sendMessage_url = hostUrl + "sendMessage";
	public static final String getChatHistory_url = hostUrl + "getChatHistory";
	public static final String getChatMessage_url = hostUrl + "getChatMessage";

	public static final String ImageHostUrl = urlPath + "pics/";
	public static final int ChunkSize = 524288;

	public static final String methodeName = "POST";
	// Update the Flurry Key Here:
	// http://support.flurry.com/index.php?title=Analytics/GettingStarted/TechnicalQuickStart/Android
	public static final String flurryKey = "6Z4Z9HMGHKBW3Q88WCFZ";

	public static final String facebooAuthenticationType = "1";
	public static final String deviceType = "2";
	public static final String FromLogin = "fromLogin";
	public static final String Fromsignup = "fromsignup";
	public static final String Fromsplash = "fromsplash";
	public static final String ALUBUMID = "lubumid";
	public static final String ALUBUMNAME = "alubumName";
	public static final String FRIENDFACEBOOKID = "friendfacebookid";
	public static final String CHECK_FOR_PUSH_OR_NOT = "check_for_push";
	public static boolean isMatchedFound = false;
	public static String isFromChatScreen = "isfromChateScreen";
	public static String isLikde = "1";
	public static String isDisliked = "2";

	public static final String FACEBOOK_ID = "facebookid";

}
