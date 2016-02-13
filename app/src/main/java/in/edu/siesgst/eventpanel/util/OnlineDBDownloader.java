package in.edu.siesgst.eventpanel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by vishal on 30/12/15.
 */
public class OnlineDBDownloader {

    final String link = "http://tml.siesgst.ac.in/includes/resources.php";

    private JSONArray JSON;
    private JSONArray regEventDetailsArray;
    Context context;

    public OnlineDBDownloader(Context context) {
        this.context = context;
    }

    public boolean validate(String IMEI, String email, String eventName) {
        String parameters = "uIMEI=" + IMEI + "&" + "uEmail=" + email + "&" + "eName" + eventName;
        byte[] postData = parameters.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;
        HttpURLConnection conn;
        try {

            URL url = new URL(link/*TODO to be changed*/);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(parameters);
            writer.flush();
            writer.close();
            String response = convertStreamToString(conn.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; //TODO Link Up.
    }

    public JSONArray downloadData() {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(100000);
            conn.setConnectTimeout(150000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.connect();
            JSONObject object = new JSONObject(convertStreamToString(conn.getInputStream()));
            JSON = object.optJSONArray("events");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
        return JSON;
    }

    public JSONArray downloadParticipants(String eventName, String IMEI) {
        String parameters = "uIMEI=" + IMEI + "&" + "eName" + eventName;
        byte[] postData = parameters.getBytes(Charset.forName("UTF-8"));
        int postDataLength = postData.length;
        HttpURLConnection conn;
        try {

            URL url = new URL(link/*TODO to be changed*/);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.connect();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(parameters);
            writer.flush();
            writer.close();
            JSON= new JSONArray(convertStreamToString(conn.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return JSON;
    }

    // Reads an InputStream and converts it to a String.
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null)
                sb.append(line).append('\n');
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public JSONArray getJSON() {
        return JSON;
    }

    public boolean checkConnection() {
        final ConnectivityManager ComMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo nwInfo = ComMgr.getActiveNetworkInfo();
        if (nwInfo != null && nwInfo.isConnected())
            return true;
        else
            return false;
    }

}