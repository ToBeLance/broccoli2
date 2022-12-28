package com.kun.myapplication.utils.net;

import java.io.StringReader;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface Request {
    @GET("/User/getAllUser")
    Call<ResponseBody> getAllUser();

    @GET("/User/getUser")
    Call<ResponseBody> getUser(@Query("name")String name);

    /**
     * 不带参数的get请求
     * @param url
     * @return
     */
    @GET()
    Call<ResponseBody> get(@Url String url);

    /**
     * 带参数的get请求
     * @param url
     * @param map 参数默认是map
     * @return
     */
    @GET()
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String,String> map);

    /**
     * 不带参数的post请求
     * @param url
     * @return
     */
    @POST()
    Call<ResponseBody> post(@Url String url);

    /**
     * 带参数的post请求
     * @param url
     * @param map
     * @return
     */
    @POST()
    @FormUrlEncoded
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String,String> map);

}
