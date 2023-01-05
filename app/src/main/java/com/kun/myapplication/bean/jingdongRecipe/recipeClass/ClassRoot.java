package com.kun.myapplication.bean.jingdongRecipe.recipeClass;

public class ClassRoot {

    private String code;
    private boolean charge;
    private String msg;
    private ClassRequestResult result;
    private String requestId;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setCharge(boolean charge) {
        this.charge = charge;
    }
    public boolean getCharge() {
        return charge;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setResult(ClassRequestResult result) {
        this.result = result;
    }
    public ClassRequestResult getResult() {
        return result;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public String getRequestId() {
        return requestId;
    }
}
