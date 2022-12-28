package com.kun.myapplication.utils.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public enum RetrofitManager {
    /**
     * RetrofitManager的实例
     */
    INSTANCE;

    /**
     *
     * 后端接口的baseUrl，且只考虑一个url的情况（ip+端口，或者域名）
     */
    private static final String MY_SERVER_BASE_URL = "http://10.219.98.16:8001";
    private static final String JING_DONG_RECIPE_BASE_URL = "https://way.jd.com/jisuapi/";

    private Retrofit retrofit;

    /**
     * 返回Retrofit实例，不添加转换器
     * @return
     */
    public Retrofit getMyServerRetrofit(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(MY_SERVER_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public Retrofit getJingDongRecipeRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(JING_DONG_RECIPE_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
