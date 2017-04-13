package com.example.denny.qrcode;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.*;


import javax.net.ssl.HttpsURLConnection;
/**
 * Created by Omar Ibrahim on 4/9/2017.
 */

public class HttpURL extends AsyncTask<String, String, String>{

    public static void main(String[] args) throws Exception {

        HttpURL http = new HttpURL();

        String webSite = "www.fart.com";
        String safe = http.get();
        System.out.println(safe);
    }

    //Sends an HTTP Get request to the api, and receives answer
    @Override
    protected String doInBackground(String... Params){
        String webSite = Params[0];
        if(!webSite.contains("www.")){
            webSite = "www." + webSite;
        }

        String safe = "";
        System.out.println(webSite);

        String url = "http://api.mywot.com/0.4/public_link_json2?hosts=" + webSite + "/&callback=process&key=55c2be73f4b425000dce3874caa04feedf25e417";

        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            safe = checkSafe(response.toString(), webSite);

            if(con != null){
                con.disconnect();
            }
            if(in != null){
                in.close();
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }


        return safe;
    }

    //Parses the JSON response. Based on info concludes if website is safe.
    private String checkSafe(String response, String webSite){
        String ans = "";
        String one = "process({ ,";
        String two = ",: { ,target,: ,";
        String three = ",, ,0,: [";
        int len = one.length() + webSite.length() + two.length() + webSite.length() + three.length();
        if(len > response.length()){
            ans = "unknown";
            return ans;
        }
        String num = response.substring(len-3,len-1);
        if(num.contains(" ")) {
            ans = "unsafe";
            return ans;
        }
        int result = Integer.parseInt(num);
        if(result < 71){
            ans = "unsafe";
            return ans;
        }
        else{
            ans = "safe";
            return ans;
        }
    }
    
    public String parseURL(URL url) {
        String webname = url.getHost();
        String website = webname;
        return website;
    }
}
