package com.fubaisum.okhttphelper.callback;

import android.accounts.NetworkErrorException;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;

/**
 * PS：如果下载文件成功，返回参数为文件的绝对路径
 * Created by sum on 15-10-2.
 */
public abstract class DownloadCallback extends Callback<String> {

    private File destFile;

    public DownloadCallback(@NonNull File destFile) {
        this.destFile = destFile;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        sendFailureCallback(e);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (!response.isSuccessful()) {
            sendFailureCallback(new NetworkErrorException(response.toString()));
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
}
