package com.kidueck.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 * @author www.codejava.net
 *
 */
public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    //리사이즈용
    Bitmap bitmap;
    /**
     * 저장할 폴더
     */
    private final String SAVE_FOLDER = "/kideuck";

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        //boundary = "===" + System.currentTimeMillis() + "===";
        boundary = "*****";

        URL url = new URL(requestURL);
        httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setRequestMethod("POST");

        httpConn.setRequestProperty("Connection", "Keep-Alive");
        httpConn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

//        httpConn.setRequestProperty("Content-Type",
//                "multipart/form-data; boundary=" + boundary);
//        httpConn.setRequestProperty("User-Agent", "CodeJava Agent");
//        httpConn.setRequestProperty("Test", "Bonjour");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     * @param name field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     * @param fieldName name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {

        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                "Content-Disposition: form-data; name=\"" + fieldName
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        //RESIZE
        uploadFile = resizeImage(uploadFile);

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     * @param name - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();

        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        String result = httpConn.getResponseMessage();
        int status = httpConn.getResponseCode();

        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            throw new IOException("Server returned non-OK status: " + status);
        }

        return response;
    }



    public File resizeImage(File inputFile) throws IOException {
        Bitmap srcBmp = BitmapFactory.decodeFile(inputFile.getAbsolutePath());

        String localPath = null;

        int iWidth   = 1000;   // 축소시킬 너비
        int iHeight  = 1000;   // 축소시킬 높이
        float fWidth  = srcBmp.getWidth();
        float fHeight = srcBmp.getHeight();

// 원하는 널이보다 클 경우의 설정
        if(fWidth > iWidth) {
            float mWidth = (float) (fWidth / 100);
            float fScale = (float) (iWidth / mWidth);
            fWidth *= (fScale / 100);
            fHeight *= (fScale / 100);
// 원하는 높이보다 클 경우의 설정
        }else if (fHeight > iHeight) {
            float mHeight = (float) (fHeight / 100);
            float fScale = (float) (iHeight / mHeight);
            fWidth *= (fScale / 100);
            fHeight *= (fScale / 100);
        }

        FileOutputStream fosObj = null;
        try {

            String uploadFileName; //리사이즈된 이미지 저장할 파일이름
            //다운로드 경로를 지정
            String savePath = Environment.getExternalStorageDirectory().toString() + SAVE_FOLDER;

            File dir = new File(savePath);
            //상위 디렉토리가 존재하지 않을 경우 생성
            if (!dir.exists()) {
                dir.mkdirs();
            }

            //파일 이름 :날짜_시간
            Date day = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA);
            uploadFileName = String.valueOf(sdf.format(day));

            //웹 서버 쪽 파일이 있는 경로
            String fileUrl = inputFile.getAbsolutePath();

            //다운로드 폴더에 동일한 파일명이 존재하는지 확인
            if (new File(savePath + "/" + uploadFileName).exists() == false) {
            } else {
            }

            localPath = savePath + "/" + uploadFileName + ".jpg";

            // 리사이즈 이미지 kideuck폴더에 따로 저장
            Bitmap resizedBmp = Bitmap.createScaledBitmap(srcBmp, (int)fWidth, (int)fHeight, true);
            fosObj = new FileOutputStream(localPath);
            resizedBmp.compress(Bitmap.CompressFormat.JPEG, 100, fosObj);
        } catch (Exception e){
            ;
        } finally {
            fosObj.flush();
            fosObj.close();
        }

        // 저장된 이미지를 스트림으로 불러오기
        File pathFile = new File(localPath);
        //FileInputStream fisObj = new FileInputStream(pathFile);

        return pathFile;
    }

}