package com.kun.myapplication.utils.net;

public abstract class RetrofitCallback {
    /**
     * 开始执行的方法
     */
    public void onStart(){
        //开启loading
    }

    /**
     * 结束执行的方法
     */
    public void onCompleted(){
        //关闭loading
    }

    /**
     * 执行成功
     * @param resultJsonString  返回的json字符串
     */
    public abstract void onSuccess(String resultJsonString);

    /**
     * 失败
     * @param t 异常
     */
    public abstract void onError(Throwable t);

    /**
     * 提示：服务异常
     */
    public void serverErrMsg(){
        //xxx
    }

    /**
     * 提示：请求失败
     */
    public void reqErrMsg(){
        //xxx
    }


    /**
     * 提示：成功
     */
    public void okMsg(){
        //xxx
    }

}
