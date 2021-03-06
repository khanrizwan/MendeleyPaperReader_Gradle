package com.mendeleypaperreader.utl;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PedroLourenco (pdrolourenco@gmail.com)
 */

public class JSONParser {

    List<InputStream> jacksonArray = new ArrayList<InputStream>();


    public String header(org.apache.http.Header[] header) {

        String aux = null;

        if (header.length > 0) {

            if (header[0].toString().contains("rel=\"next\"")) {

                Pattern pattern = Pattern.compile("\\<(.+?)\\>");
                Matcher matcher = pattern.matcher(header[0].toString());


                while (matcher.find()) {

                    aux = matcher.group(1);
                }
                return aux;
            }
        }

        return "finish";

    }


    public List<InputStream> getJACKSONFromUrl(String url, boolean with_header) {
        // Making HTTP request

        if (url.equals("finish")) {
            return jacksonArray;
        }

        //InputStream content = null;
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();


        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                jacksonArray.add(entity.getContent());

                if (with_header) {
                    String link = header(response.getHeaders("Link"));
                    getJACKSONFromUrl(link, true);
                }

            } else {
                if (Globalconstant.LOG)
                    Log.e(Globalconstant.TAG, "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Globalconstant.LOG)
            Log.e(Globalconstant.TAG, "builder.toString(): " + builder.toString());

        return jacksonArray;
    }


}