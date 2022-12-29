package com.kun.myapplication.bean.jingdongRecipe.searchAndByclass;

public class SearchAndByClassRoot {
    private String code;

    private boolean charge;

    private String msg;

    private SearchAndByClassResult result;

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
    public void setResult(SearchAndByClassResult result){
        this.result = result;
    }
    public SearchAndByClassResult getResult(){
        return this.result;
    }
    public void setRequestId(String requestId){
        this.requestId = requestId;
    }
    public String getRequestId(){
        return this.requestId;
    }
}
