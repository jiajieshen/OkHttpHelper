# OkHttpHelper
方便使用OkHttp的库

# 用法事例
Get请求

        OkHttpRequest request = new OkHttpRequest.Builder()
                .tag(tag)
                .url(url)
                .appendUrlParam(key, value)
                .appendHeader(line)
                .appendHeader(key, value)
                .responseProgress(new OkHttpProgressListener() {
                    @Override
                    public void onProgress(long currentBytesCount, long totalBytesCount) {

                    }
                })
                .get();

Post请求

        OkHttpRequest request = new OkHttpRequest.Builder()
                .tag(tag)
                .url(url)
                .appendUrlParam(key, value)
                .appendHeader(line)
                .appendHeader(key, value)
                .requestProgress(new OkHttpProgressListener() {
                    @Override
                    public void onProgress(long currentBytesCount, long totalBytesCount) {
                        
                    }
                })
                .responseProgress(new OkHttpProgressListener() {
                    @Override
                    public void onProgress(long currentBytesCount, long totalBytesCount) {

                    }
                })
                .post();

同步响应（三选一）

        InputStream inputStream = request.byteStream();
        String string = request.string();
        User user = request.model(User.class);

异步响应（分ui/worker回调）

        request.uiCallback(new OkHttpModelCallBack<String>() {
            @Override
            public void onResponseSuccess(String result) {
                //可直接更新UI
            }

            @Override
            public void onResponseError(Exception e) {
                //可直接更新UI
            }
        });

        request.workerCallback(new OkHttpModelCallBack<String>() {
            @Override
            public void onResponseSuccess(String result) {
                //不可直接更新UI
            }

            @Override
            public void onResponseError(Exception e) {
                //不可直接更新UI
            }
        });

下载



# 参考
http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0106/2275.html

https://github.com/lizhangqu/CoreProgress.git

https://github.com/hongyangAndroid/okhttp-utils.git
