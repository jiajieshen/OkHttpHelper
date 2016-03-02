package com.fubaisum.okhttphelper.callback;

import android.support.annotation.NonNull;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * PS：如果下载文件成功，返回参数为文件的绝对路径
 * Created by sum on 15-10-2.
 */
public abstract class OkHttpDownloadCallback extends OkHttpCallback<String> {

    private File destFile;

    public OkHttpDownloadCallback(@NonNull File destFile) {
        this.destFile = destFile;
    }

    @Override
    public void onFailure(Request request, IOException e) {
        sendFailureCallback(e);
    }

    @Override
    public void onResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            sendFailureCallback(new RuntimeException(response.toString()));
        } else {
            InputStream inputStream = response.body().byteStream();
            byte[] buffer = new byte[2048];
            int readBytesCount;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(destFile);
                while ((readBytesCount = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, readBytesCount);
                }
                fileOutputStream.flush();
                // 下载文件成功，返回参数为文件的绝对路径
                sendSuccessCallback(destFile.getAbsolutePath());
            } catch (IOException e) {
                sendFailureCallback(e);
            } finally {
                try {
                    if (null != inputStream) inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (null != fileOutputStream) fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public abstract void onResponseSuccess(String fileAbsolutePath);
}
