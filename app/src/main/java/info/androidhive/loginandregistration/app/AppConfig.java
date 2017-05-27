package info.androidhive.loginandregistration.app;

public class AppConfig {

	private static String host = "192.168.1.101";
	// Server user login url
	public static String URL_LOGIN = "http://"+host+":8080/android_login_api/login.php";

	// Server user register url
	public static String URL_REGISTER = "http://"+host+":8080/android_login_api/register.php";

	public static String URL_CREATE_EVENT = "http://"+host+":8080/android_login_api/createEvent.php";

	public static String URL_SEARCH_EVENT = "http://"+host+":8080/android_login_api/searchEvent.php";

	public static String URL_DELETE_EVENT = "http://"+host+":8080/android_login_api/deleteEvent.php";

	public static String URL_GET_EVENTS = "http://"+host+":8080/android_login_api/getUserEvents.php";

	public static String URL_CREATE_PRESENT = "http://"+host+":8080/android_login_api/createPresent.php";

	public static String URL_DELETE_PRESENT = "http://"+host+":8080/android_login_api/deletePresent.php";

	public static String URL_TAKE_PRESENT = "http://"+host+":8080/android_login_api/takePresent.php";

	public static String URL_UNTAKE_PRESENT = "http://"+host+":8080/android_login_api/untakePresent.php";

	public static String URL_GET_PRESENTS = "http://"+host+":8080/android_login_api/getPresents.php";
}
