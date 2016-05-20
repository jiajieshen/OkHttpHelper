# OkHttpHelper
方便使用OkHttp的库，okhttp见：https://github.com/square/okhttp

## 在module的build.gradle添加依赖
<pre><code>
repositories {
    maven {
        url 'https://dl.bintray.com/fubaisum/maven/'
    }
}

dependencies {
    compile 'com.scausum.okhttphelper:okhttphelper:2.0.5'
    compile 'com.scausum.okhttphelper:converter-gson:2.0.5'
    compile 'com.squareup.okhttp3:okhttp:3.2.0'
    compile 'com.google.code.gson:gson:2.6.2'
}

</code></pre>

## 用法示例

初始化使用GSON来解析json
<p><code>
        OkHttpRequest.setConverterFactory(GsonConverterFactory.create());
</code></p>

构建Get请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .url(url)
                .appendUrlParam(key, value)
                .appendHeader(line)
                .appendHeader(key, value)
                .responseProgress(progressListener)
                .build();

构建Post请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .url(url)
                .appendUrlParam(key, value)
                .appendHeader(line)
                .appendHeader(key, value)
                .post(okHttpParams)
                .requestProgress(requestProgressListener)
                .responseProgress(responseProgressListener)
                .build();

Post参数构建

        //普通表单参数
        FormParams params = new FormParams();
        params.put(key,value);
        
        //多类型参数
        MultiTypeParams params = new MultiTypeParams();
        params.put(key,value);
        params.put(key,file);
        params.put(key,fileName,bytes);
        
        //Json参数
        JsonParams jsonParams = new JsonParams(json);

执行同步请求

        InputStream inputStream = okHttpRequest.byteStream();
        String string = okHttpRequest.string();
        User model = okHttpRequest.model();

执行异步请求

        okHttpRequest.threadMode(ThreadMode.MAIN)//default
                uiCallback(okHttpCallback);//Ui线程回调
                
        okHttpRequest.threadMode(ThreadMode.BACKGROUND)
                uiCallback(okHttpCallback);//后台线程回调

Model回调

        new ModelCallBack<T>() {
            @Override
            public void onResponseSuccess(T result) {
            }

            @Override
            public void onResponseError(Exception e) {
            }
        };

下载文件回调

        new DownloadCallback(destFile) {

            @Override
            public void onResponseSuccess(String fileAbsolutePath) {
            }

            @Override
            public void onResponseError(Exception e) {
            }
        };

进度监听

        new UiProgressListener() {
            @Override
            protected void onUiProgress(long currentBytesCount, long totalBytesCount) {
                if (totalBytesCount == -1) {
                    // unknown progress
                }else{
                    float progress = currentBytesCount * 1.0f / totalBytesCount * 100;
                }
            }
        };

取消请求

        okHttpRequest.cancel();

全局配置

        OkHttpClient okHttpClient = OkHttpClientHolder.getOkHttpClient();
        // eg.
        okHttpClient.networkInterceptors().add(new StethoInterceptor());
        // eg.
        okHttpClient.setConnectTimeout(10000, TimeUnit.SECONDS);


# 参考
OkHttp使用教程
http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

OkHttp上传下载进度监听
https://github.com/lizhangqu/CoreProgress.git

OkHttp的辅助类
https://github.com/hongyangAndroid/okhttp-utils.git
