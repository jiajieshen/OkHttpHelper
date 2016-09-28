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
    compile 'com.scausum.okhttphelper:okhttphelper:2.1.3'
    compile 'com.scausum.okhttphelper:converter-gson:2.1.3'
    compile 'com.squareup.okhttp3:okhttp:3.4.1'
    compile 'com.google.code.gson:gson:2.7'
}

</code></pre>

## 用法示例

构建Get请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .setTag(tag)
                .setUrl(url)
                .appendUrlPath(path)
                .addUrlQuery(key,value)
                .addHeader(line)
                .addHeader(key, value)
                .setResponseProgressListener(progressListener)
                .get()
                .build();

构建Post请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .setTag(tag)
                .setUrl(url)
                .appendUrlPath(path)
                .addUrlQuery(key,value)
                .addHeader(line)
                .addHeader(key, value)
                .setRequestProgressListener(requestProgressListener)
                .setResponseProgressListener(responseProgressListener)
                .post(okHttpParams)
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
        T model = okHttpRequest.model();

执行异步请求

        okHttpRequest.threadMode(ThreadMode.MAIN)//default
                .uiCallback(okHttpCallback);//Ui线程回调
                
        okHttpRequest.threadMode(ThreadMode.BACKGROUND)
                .uiCallback(okHttpCallback);//后台线程回调

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

初始化使用GSON来解析json
<p><code>
        OkHttpHelper.setConverterFactory(GsonConverterFactory.create());
</code></p>

全局配置

        // 添加Stetho网络拦截器
        Stetho.initializeWithDefaults(this);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        OkHttpHelper.setOkHttpClient(okHttpClient);


# 参考
OkHttp使用教程
http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

OkHttp上传下载进度监听
https://github.com/lizhangqu/CoreProgress.git

OkHttp的辅助类
https://github.com/hongyangAndroid/okhttp-utils.git

采用Converter.Factory类分离Model层的解析实现
https://github.com/square/retrofit.git
