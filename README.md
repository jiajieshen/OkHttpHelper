# OkHttpHelper
方便使用OkHttp的库，okhttp见：https://github.com/square/okhttp

# 用法示例
构建Get请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .tag(tag)
                .url(url)
                .appendUrlParam(key, value)
                .appendHeader(line)
                .appendHeader(key, value)
                .responseProgress(progressListener)
                .build();

构建Post请求

        OkHttpRequest okHttpRequest = new OkHttpRequest.Builder()
                .tag(tag)
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
        OkHttpFormParams okHttpParams = new OkHttpFormParams();
        okHttpParams.put(key,value);
        
        //多类型参数
        OkHttpMultiTypeParams okHttpParams = new OkHttpMultiTypeParams();
        okHttpParams.put(key,value);
        okHttpParams.put(key,file);
        okHttpParams.put(key,fileName,bytes);
        
        //Json参数
        OkHttpJsonParams okHttpJsonParams = new OkHttpJsonParams(json);

执行同步请求

        InputStream inputStream = okHttpRequest.requestByteStream();
        String string = okHttpRequest.requestString();
        User model = okHttpRequest.requestModel(User.class);

执行异步请求

        okHttpRequest.requestUiCallback(okHttpCallback);//Ui线程回调
        okHttpRequest.requestWorkerCallback(okHttpCallback);//后台线程回调

Model回调

        new OkHttpModelCallBack<String>() {
            @Override
            public void onResponseSuccess(String result) {
            }

            @Override
            public void onResponseError(Exception e) {
            }
        };

下载文件回调

        new OkHttpDownloadCallback(destDir, destFileName) {

            @Override
            public void onResponseSuccess(String fileAbsolutePath) {
            }

            @Override
            public void onResponseError(Exception e) {
            }
        };

进度监听

        new OkHttpUiProgressListener() {
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

PS：
响应进度回调的实现，是采用克隆OkHttpClient单例，然后添加拦截器的方法；

# 参考
OkHttp使用教程
http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

OkHttp上传下载进度监听
https://github.com/lizhangqu/CoreProgress.git

okhttp的辅助类
https://github.com/hongyangAndroid/okhttp-utils.git
