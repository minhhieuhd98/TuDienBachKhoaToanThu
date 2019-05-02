package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class WikiSearch {
     
    private final String wikimedia = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro=&explaintext=&titles=";

    private String userAgent = "Mozilla/5.0";
 
    public String translate(String query) throws MalformedURLException, IOException, ParseException {
        URL url = new URL(this.buildURLRequestWith(query));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty("User-Agent", this.userAgent);
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(30000);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        BufferedReader bis = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        String responseSB = bis.lines().collect(Collectors.joining());
        String result = responseSB.split("extract\":\"")[1];
        inputStream.close();
        bis.close();
        conn.disconnect();
        return result;
    }
 
    private String buildURLRequestWith(String query) {
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(this.wikimedia);
        String queryEncoded = "";
        try {
            queryEncoded = URLEncoder.encode(query, "UTF-8");
        } catch (Exception e) {
        }
        urlBuilder.append(queryEncoded);
        urlBuilder.append("&format=json");
        return urlBuilder.toString();
    }
 
    public String getUserAgent() {
        return userAgent;
    }
 
    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
         
}