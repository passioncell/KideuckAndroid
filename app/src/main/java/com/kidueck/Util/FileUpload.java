package com.kidueck.Util;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

public class FileUpload {
    private static String lineEnd = "\r\n";
    private static String twoHyphens = "--";
    private static String boundary = "*****";
    private static String file_name = "";
    private static String iconname = "";
    private static Context mContext;
    private static Activity mActivity;
    private static Map<String, String> mParameters;
    private String locationProvider;
    private Location location = null;
    private LocationManager locationManager = null;

    public FileUpload(Context context, Activity act) {
        super();
        mContext = context;
        mActivity = act;
        mParameters = null;

    }
    public FileUpload(Context context, Activity act, Map<String, String> parameters) {
        super();
        mContext = context;
        mActivity = act;
        mParameters = parameters;

    }

    public String HttpFileUpload(Context context, String params, String fileName, String uploadUrl) {
        try {
            file_name = null;
            FileInputStream mFileInputStream = new FileInputStream(fileName);
            URL connectUrl = new URL(uploadUrl);
            Log.d("Test", "mFileInputStream  is " + mFileInputStream);
            File f = new File(fileName);
            // open connection
            HttpURLConnection conn = (HttpURLConnection)connectUrl.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            // write data
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());


            if(!(mParameters == null))
            {
                Iterator<String> iterator = mParameters.keySet().iterator();
                while(iterator.hasNext())
                {
                    String key = (String) iterator.next();

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + lineEnd );
                    dos.writeBytes(lineEnd);
                    dos.write(mParameters.get(key).getBytes("utf-8"));
                    dos.writeBytes(lineEnd);
                }
            }
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + fileName+"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.d("Test", "fileName  is " + fileName);

            int bytesAvailable = mFileInputStream.available();
            int maxBufferSize = (int) f.length();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            byte[] buffer = new byte[bufferSize];
            int bytesRead = mFileInputStream.read(buffer, 0, bufferSize);

            Log.d("Test", "image byte is " + bytesRead);

            // read image
            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = mFileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = mFileInputStream.read(buffer, 0, bufferSize);
            }

            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            Log.e("Test" , "File is written");
            mFileInputStream.close();
            dos.flush(); // finish upload...

            // get response
            int ch;
            InputStream is = conn.getInputStream();
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){
                b.append( (char)ch );
            }
            file_name=b.toString();
            Log.e("Test", "result = " + fileName);
            //mEdityEntry.setText(s);
            dos.close();

        } catch (Exception e) {
            Log.d("Test", "exception " + e.getMessage());
            // TODO: handle exception
        }
        return file_name;
    }

    public static int getBitmapOfWidth( String fileName ){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            return options.outWidth;
        } catch(Exception e) {
            return 0;
        }
    }

    public static int getBitmapOfHeight( String fileName ){
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);

            return options.outHeight;
        } catch(Exception e) {
            return 0;
        }
    }
    public void SaveBitmapToFileCache(Bitmap bitmap, String strFilePath)
    {
        File fileCacheItem = new File(strFilePath);
        OutputStream out = null;

        try
        {
            File file = new File(strFilePath);
            out = new FileOutputStream(fileCacheItem);
            bitmap.compress(CompressFormat.JPEG, 100 , out);
            out.flush();
            out.close();



//
//
//	    		fileCacheItem.createNewFile();
//	    		out = new FileOutputStream(fileCacheItem);
//
//	    		bitmap.compress(CompressFormat.JPEG, 100, out);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public  static Bitmap BitmapSizeChange(Bitmap wbitmap, int width, int height){
        int wwidth = wbitmap.getWidth();
        int wheight = wbitmap.getHeight();
        int newWidth = width;
        int newHeight = height;

        float scalewidth = ((float)newWidth) / wwidth;
        float scaleheight = ((float)newHeight) / wheight;

        Matrix mt = new Matrix();
        mt.postScale(scalewidth, scaleheight);

        Bitmap resizebt = Bitmap.createBitmap(wbitmap, 0, 0, wwidth, wheight, mt, true);
        mt = null;

        return resizebt;
    }

    public static Bitmap GetImageFromURL(String strImageURL) {
        Bitmap imgBitmap = null;
        try {
            URL url = new URL(strImageURL);
            URLConnection conn = url.openConnection();
            conn.connect();
            int nSize = conn.getContentLength();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream(), nSize);
            imgBitmap = BitmapFactory.decodeStream(bis);
            bis.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return imgBitmap;
    }



}