package com.kun.myapplication.bean.jingdongRecipe.detail;

public class DetailRoot {
    private String code;

    private boolean charge;

    private String msg;

    private DetailResult result;

    private String requestId;

    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return this.code;
    }
    public void setCharge(boolean charge){
        this.charge = charge;
    }
    public boolean getCharge(){
        return this.charge;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setResult(DetailResult result){
        this.result = result;
    }
    public DetailResult getResult(){
        return this.result;
    }
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }
    public String getRequestId(){
        return this.requestId;
    }
}
