/**
 * 
 */
package timezone;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.function.ToIntBiFunction;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * @author student
 *
 */
public class TimezoneMain {

	/**
	 * @param args
	 */

	private static String GOOGLE_API_K = "AIzaSyARiki0HBLlyR7xH0K3e4eifaSLzx8b-7E";

	private final HttpClient client  = HttpClientBuilder.create().build();
	//private static Scanner scanner = new Scanner( System.in );
	public static void main(String[] args) throws ClientProtocolException, URISyntaxException, IOException {
		// TODO Auto-generated method stub
		new TimezoneMain().performSearch("45.6479343,-74.2722061", "1466265003");

	}
	private void performSearch(final String location, final String timestamp) throws URISyntaxException, ClientProtocolException, IOException {
		// TODO Auto-generated method stub
		final URIBuilder builder = new URIBuilder().setScheme("https").setHost("maps.googleapis.com").setPath("/maps/api/timezone/json");
		final String longitudeLatitude =  "45.6479343,-74.2722061";
		
	/*	DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	       Date dateobj = new Date();
	       System.out.println(df.format(dateobj));

	       //*getting current date time using calendar class 
	       // * An Alternative of above*
	       Calendar calobj = Calendar.getInstance();
	       System.out.println(df.format(calobj.getTime())); */
		
	       long timeStampIntvalue = System.currentTimeMillis()/1000; // Unix time also known as POSIX time or Epoch time
	       String timeStampStringValue = String.valueOf(timeStampIntvalue);
	       //user.getCreatedTime().getTime();
	       
		builder.addParameter("location", longitudeLatitude);
		builder.addParameter("timestamp", timeStampStringValue );
		builder.addParameter("key",TimezoneMain.GOOGLE_API_K);
		
		final HttpUriRequest request = new HttpGet(builder.build());
		final HttpResponse execute = client.execute(request);
		final String response = EntityUtils.toString(execute.getEntity());
		System.out.println(response);
		
		JSONObject object = JSONObject.fromObject(response);
		isLocationDaylightSavingZone(object);
		
		calculatingLocalTime(timeStampIntvalue, object);
		
	}
	
	public void calculatingLocalTime(long timeStampIntvalue, JSONObject object) {
		
		long localTime = 	(timeStampIntvalue + object.getInt("dstOffset") + object.getInt("rawOffset"));
		
		System.out.println("The Localtime for current location issss : " + localTime);
		
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    //formatter.setTimeZone(TimeZone.getTimeZone(object.getString("timeZoneName")));
	    Date date= new Date(localTime);  
	    String dateString = formatter.format(date);
	    System.out.println("Converted UTC TIME (using Format method) : "+dateString);
	    
	    
	}
	public void isLocationDaylightSavingZone(JSONObject object) {
		double IsDaylightSavingTime = object.getInt("dstOffset");
		if(IsDaylightSavingTime > 0)
		{
			IsDaylightSavingTime = IsDaylightSavingTime/60;
			System.out.println("The Locatin is in Day-light Saving Timezone and it is " + IsDaylightSavingTime  + " minutes ahead");
		}
	}
}