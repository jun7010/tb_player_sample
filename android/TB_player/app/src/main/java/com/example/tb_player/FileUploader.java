package com.example.tb_player;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileUploader {


    public interface UploadCallback {
        void onUploadComplete(String result);
    }

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void uploadFileAsync(File file, UploadCallback callback) {
        executorService.submit(() -> {
            String result = uploadFile(file);
            if (callback != null) {
                callback.onUploadComplete(result);
            }
        });
    }

    public static void shutdownExecutorService() {
        if (!executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
    public static String uploadFile(File file) {
        try {
            Log.d("Upload", "uploadFile Called : " + file.getAbsoluteFile() + " / " + file.length());
            String serverUrl = "http://10.0.2.2:5001/upload";
            // android studio는 localhost 대신 10.0.2.2 사용
            HttpURLConnection connection = (HttpURLConnection) new URL(serverUrl).openConnection();

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("ENCTYPE", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + "*****");

            // 파일 전송 준비
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

            // 파일 필드의 이름을 "myFile"로 지정
            String fieldName = "myFile";

            // 파일 전송 헤더 작성
            String fixedFileName = "sample.png";
            dos.writeBytes("--*****\r\n");
            dos.writeBytes("Content-Disposition: form-data; name=\"" + fieldName + "\";filename=\"" + fixedFileName + "\"" + "\r\n");
            dos.writeBytes("\r\n");

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;

            // 파일을 읽어서 서버로 전송
            while ((bytesRead = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesRead);
            }

            fis.close();

            // 파일 전송 마무리
            dos.writeBytes("\r\n");
            dos.writeBytes("--*****--\r\n");

            dos.flush();
            dos.close();

            // 서버 응답 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 파일이 성공적으로 전송됨
                // 여기에서 서버의 응답을 확인하거나 필요한 처리를 수행할 수 있습니다.

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder responseStringBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseStringBuilder.append(line);
                    }
                    String responseString = responseStringBuilder.toString();
                    Log.d("Upload", "response : " + responseString);
                    return responseString;
                } catch (Exception e) {
                    Log.d("Upload", "response Exception");
                    Log.d("Upload", e.getMessage());
                    return null;
                }

            } else {
                // 서버 응답이 실패인 경우 처리
                Log.d("Upload", "uploadFile HTTP_Fail : " + responseCode);
                return null;
            }
        } catch (Exception e) {
            // 예외 처리
            Log.d("Upload", "uploadFile HTTP_EXCEPTION");
            Log.d("Upload", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}