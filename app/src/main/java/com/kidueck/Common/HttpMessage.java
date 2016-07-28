package com.kidueck.Common;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;


public class HttpMessage {
    /**
     * HTTP 프로토콜을 사용하여 연결할 URL
     */
    private URL targetURL;
    private static final String TAG = "HttpMessage";
    /**
     * POST 방식으로 데이터를 전송할 때 사용되는 출력 스트림
     */
    private DataOutputStream out;

    public HttpMessage(URL targetURL) {
        this.targetURL = targetURL;
    }

    public InputStream sendGetMessage() throws IOException {
        return sendGetMessage(null);
    }

    public InputStream sendGetMessage(Properties params)     throws IOException {
        String paramString = "";
        try {
            if (params != null) {
                paramString = "?"+encodeString(params);
            }
            URL url = new URL(targetURL.toExternalForm() + paramString);
            Log.d("경로", url.toString());
            URLConnection conn = url.openConnection();
            conn.setUseCaches(false);
            return conn.getInputStream();
        }catch (IOException e1) {

            Log.d(TAG, "sendGetMessage :" + e1.getMessage());

            return null;
        }

    }

    public InputStream sendPostMessage() throws IOException {
        return sendPostMessage("");
    }

    public InputStream sendPostMessage(Properties params)     throws IOException {
        String paramString = "";

        if (params != null) {
            paramString = encodeString(params);
        }
        return sendPostMessage(paramString);
    }

    private InputStream sendPostMessage(String encodedParamString)     throws IOException {
        URLConnection conn = targetURL.openConnection();

        conn.setDoInput(true);
        conn.setDoOutput(true);

        conn.setUseCaches(false);

        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");

        out = null;
        try {
            out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(encodedParamString);
            out.flush();
        } finally {
            if (out != null) out.close();
        }

        return conn.getInputStream();
    }

    public static String encodeString(Properties params) {
        StringBuffer sb = new StringBuffer(256);
        Enumeration names = params.propertyNames();

        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = params.getProperty(name);
            sb.append(URLEncoder.encode(name) + "=" + URLEncoder.encode(value) );

            if (names.hasMoreElements()) sb.append("&");
        }
        return sb.toString();
    }

    public static String getWebContentText(InputStream stream) throws IOException{

        String ContentText = "";
        InputStream is = stream;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        char[] buff = new char[256];
        int len = -1;

        while( (len = br.read(buff)) != -1) {
            ContentText  += new String(buff, 0, len);
        }
        br.close();

        return ContentText ;
    }

    public static String convertStreamToString(InputStream is)  {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}