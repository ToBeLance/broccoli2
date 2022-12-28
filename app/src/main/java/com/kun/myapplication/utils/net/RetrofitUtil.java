package com.kun.myapplication.utils.net;


import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class RetrofitUtil {
    //private Retrofit(){}

    /**
     * 无参的get请求
     * @param url
     * @param callback
     */
    public static void getFromMyServer(String url, RetrofitCallback callback){
        sendRequest(getMyServerRequest().get(url),callback);
    }

    /**
     * 有参的get请求
     * @param url  请求的url
     * @param map  参数
     * @param callback  请求结束的回调
     */
    public static void getFromMyServer(String url, Map<String,String> map, RetrofitCallback callback){
        sendRequest(getMyServerRequest().get(url,map),callback);
    }

    /**
     * 无参的post请求
     * @param url
     * @param callback
     */
    public static void postFromMyServer(String url, RetrofitCallback callback){
        sendRequest(getMyServerRequest().post(url), callback);
    }

    /**
     * 有参的post请求
     * @param url
     * @param map
     * @param callback
     */
    public static void postFromMyServer(String url, Map<String,String> map, RetrofitCallback callback){
        sendRequest(getMyServerRequest().post(url,map), callback);
    }

    /**
     * 无参的get请求
     * @param url
     * @param callback
     */
    public static void getFromJingDongRecipe(String url, RetrofitCallback callback){
        sendRequest(getJingDongRecipeRequest().get(url),callback);
    }

    /**
     * 有参的get请求
     * @param url  请求的url
     * @param map  参数
     * @param callback  请求结束的回调
     */
    public static void getFromJingDongRecipe(String url, Map<String,String> map, RetrofitCallback callback){
        sendRequest(getJingDongRecipeRequest().get(url,map),callback);
    }

    /**
     * 无参的post请求
     * @param url
     * @param callback
     */
    public static void postFromJingDongRecipe(String url, RetrofitCallback callback){
        sendRequest(getJingDongRecipeRequest().post(url), callback);
    }

    /**
     * 有参的post请求
     * @param url
     * @param map
     * @param callback
     */
    public static void postFromJingDongRecipe(String url, Map<String,String> map, RetrofitCallback callback){
        sendRequest(getJingDongRecipeRequest().post(url,map), callback);
    }


    /**
     * 获取Request实例
     * @return
     */
    private static Request getMyServerRequest(){
        Retrofit retrofit = RetrofitManager.INSTANCE.getMyServerRetrofit();
        return retrofit.create(Request.class);
    }
    private static Request getJingDongRecipeRequest() {
        Retrofit retrofit = RetrofitManager.INSTANCE.getJingDongRecipeRetrofit();
        return retrofit.create(Request.class);
    }

    /**
     * 发送请求的共通方法，并对响应结果进行处理
     * @param call
     * @param callback 自定义的Callback
     */
    private static void sendRequest(Call<ResponseBody> call, RetrofitCallback callback) {

        //开启loading
        callback.onStart();
        //异步请求
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //关闭loading
                callback.onCompleted();
                if (response.isSuccessful()) {
                    //执行RetrofitCallback的onSuccess方法，获取响应结果的json字符串
                    try {
                        String result = response.body().string();
                        callback.onSuccess(result);
                        //响应成功
//                        if(StringUtils.equals(result,Constant.SUCCESS)){
//                            callback.okMsg();
//                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //服务异常
                    callback.serverErrMsg();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onCompleted();
                //请求失败
                callback.onError(t);

                callback.reqErrMsg();
            }
        });
    }
}
