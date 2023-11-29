package com.example.tb_player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.TextureView;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenCaptureUtil {

    public static void captureAndSave(final PlayerActivity context, TextureView textureView) {
        textureView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("thread", "start");
                Bitmap bitmap = textureViewToBitmap(textureView);
                Log.d("thread", "bitmap convert");

                if (bitmap != null) {
                    Log.d("thread", "bitmap start");
                    final String filePath = saveBitmapToFile(context, bitmap);
                    Log.d("thread", "bitmap filePath : " + filePath);
                    if (filePath != null) {
                        // 파일 저장이 성공한 경우
                        // 업로드를 처리하거나 다른 작업을 수행할 수 있습니다.
                        FileUploader.uploadFileAsync(new File(filePath), new FileUploader.UploadCallback() {
                            @Override
                            public void onUploadComplete(String result) {
                                if (result != null) {
                                    // 서버 응답 처리
                                    Log.d("thread", "Server Response : " + result);
                                    context.setJson(result);
                                } else {
                                    Log.d("thread", "Server Response error");
                                }
                            }
                        });
                    } else {
                        // 파일 저장에 실패한 경우
                        Log.d("thread", "Failed to save screenshot");
                    }
                } else {
                    Log.d("thread", "bitmap null");
                }
            }
        }, 1000); // 1초 딜레이
    }

    private static Bitmap textureViewToBitmap(TextureView textureView) {
        Bitmap bitmap = textureView.getBitmap(textureView.getWidth(), textureView.getHeight());
        //Bitmap bitmap = Bitmap.createBitmap(textureView.getWidth(), textureView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        textureView.draw(canvas);
        return bitmap;
    }

    private static String saveBitmapToFile(Context context, Bitmap bitmap) {
        File directory = new File(context.getFilesDir(), "ExoPlayerScreenshots");

        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "screenshot_1.png";

        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}