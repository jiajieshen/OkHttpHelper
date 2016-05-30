package com.fubaisum.okhttphelper.callback;

import android.accounts.NetworkErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * PS：如果下载文件成功，返回参数为文件的绝对路径
 * Created by sum on 15-10-2.
 */
public abstract class DownloadCallback extends Callback<String> {

    private File destFile;

    public DownloadCallback(File destFile) {
        this.destFile = destFile;
    }

    @Override
    public final void onFailure(Call call, IOException e) {
        sendFailureCallback(e);
    }

    @Override
    public final void onResponse(Call call, Response response) throws IOException {
        if (destFile == null) {
            sendFailureCallback(new IllegalArgumentException("The destFile cannot be null."));
        }
        if (!response.isSuccessful()) {
            sendFailureCallback(new NetworkErrorException(response.toString()));
        } else {
            writeToDestFile(response);
        }
    }

    private void writeToDestFile(Response response) {
        ResponseBody responseBody = response.body();
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        byte[] buffer = new byte[2048];
        int readBytesCount;
        try {
            inputStream = responseBody.byteStream();
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
            // 关闭responseBody
            responseBody.close();
            // 关闭输入输出流
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    protected abstract void onResponseSuccess(String absolutePath);
}
