package com.example.james.foodapp;

import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import static android.content.ContentValues.TAG;

/**
 * Created by james on 2017-03-19.
 */

public class SearchFood {

    /**
     * FatSecret Authentication
     * http://platform.fatsecret.com/api/default.aspx?screen=rapiauth
     * Reference
     * https://github.com/ethan-james/cookbox/blob/master/src/com/vitaminc4/cookbox/FatSecret.java
     */
    final static private String APP_METHOD = "GET";
    final static private String APP_KEY = "e8f1ee8aeb2640d2831349c9e2d63334";
    static private String APP_SECRET = "68a6ee2e74034474a625a5dfdf2546d1";
    final static private String APP_URL = "http://platform.fatsecret.com/rest/server.api";
    private static final String HMAC_SHA1_ALGORITHM = "HMAC-SHA1";

    final static String METHOD = "method";
    final static String EXPRESSION = "search_expression";
    final static String LON_PARAM = "lon";
    final static String FORMAT_PARAM = "mode";
    final static String UNITS_PARAM = "units";
    final static String DAYS_PARAM = "cnt";

/*
    public static URL buildUri(String food) {
        Uri builtUri = Uri.parse(APP_URL).buildUpon()
                .appendQueryParameter(METHOD, "foods.search")
                .appendQueryParameter(EXPRESSION, Uri.encode(food))
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
*/
    public JSONObject searchFood(String searchFood) {
        List<String> params = new ArrayList<>(Arrays.asList(generateOauthParams()));
        String[] template = new String[1];
        params.add("method=foods.search");
        params.add("search_expression=" + Uri.encode(searchFood));
        params.add("oauth_signature=" + sign(APP_METHOD, APP_URL, params.toArray(template)));

        JSONObject foods = null;
        try {
            URL url = new URL(APP_URL + "?" + paramify(params.toArray(template)));
            URLConnection api = url.openConnection();
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(api.getInputStream()));
            while ((line = reader.readLine()) != null) builder.append(line);
            Log.v(TAG, builder.toString());
            JSONObject food = new JSONObject(builder.toString());   // { first

            foods = food.getJSONObject("foods");                    // { second
        } catch (Exception exception) {
            Log.e("FatSecret Error", exception.toString());
            exception.printStackTrace();
        }
        return foods;
    }

    private static String[] generateOauthParams() {
        return new String[]{
                "oauth_consumer_key=" + APP_KEY,        // Your API key when you registered as a developer
                "oauth_signature_method=HMAC-SHA1",     //The method used to generate the signature (only HMAC-SHA1 is supported)
                "oauth_timestamp=" +                    //The date and time, expressed in the number of seconds since January 1, 1970 00:00:00 GMT.
                        Long.valueOf(System.currentTimeMillis() * 2).toString(), // Should be  Long.valueOf(System.currentTimeMillis() / 1000).toString()
                "oauth_nonce=" + nonce(),               // A randomly generated string for a request that can be combined with the timestamp to produce a unique value
                "oauth_version=1.0",                    // MUST be "1.0"
                "format=json",                          // The desired response format. Valid reponse formats are "xml" or "json" (default value is "xml").// The zero-based offset into the results for the query. Use this parameter with max_results to request successive pages of search results (default value is 0).
                "max_results=" + 1,
                "generic_description=portion"};                   // The maximum number of results to return (default value is 20). This number cannot be greater than 50.
    }

    private static String sign(String method, String uri, String[] params) {
        String[] p = {method, Uri.encode(uri), Uri.encode(paramify(params))};
        String s = join(p, "&");
        APP_SECRET += "&";
        SecretKey sk = new SecretKeySpec(APP_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        APP_SECRET = APP_SECRET.substring(0, APP_SECRET.length() - 1);
        try {
            Mac m = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            m.init(sk);
            return Uri.encode(new String(Base64.encode(m.doFinal(s.getBytes()), Base64.DEFAULT)).trim());
        } catch (java.security.NoSuchAlgorithmException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        } catch (java.security.InvalidKeyException e) {
            Log.w("FatSecret_TEST FAIL", e.getMessage());
            return null;
        }
    }

    private static String paramify(String[] params) {
        String[] p = Arrays.copyOf(params, params.length);
        Arrays.sort(p);
        return join(p, "&");
    }

    private static String join(String[] array, String separator) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0)
                b.append(separator);
            b.append(array[i]);
        }
        return b.toString();
    }

    private static String nonce() {
        Random r = new Random();
        StringBuilder n = new StringBuilder();
        for (int i = 0; i < r.nextInt(8) + 2; i++)
            n.append(r.nextInt(26) + 'a');
        return n.toString();
    }
}
