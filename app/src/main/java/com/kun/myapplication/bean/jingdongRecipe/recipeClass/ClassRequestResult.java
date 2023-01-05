package com.kun.myapplication.bean.jingdongRecipe.recipeClass;

import java.util.List;

public class ClassRequestResult {
    private int status;
    private String msg;
    private List<ClassResult> result;
    public void setStatus(int status) {
        this.status = status;
    }
    public int getStatus() {
        return status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }

    public void setResult(List<ClassResult> result) {
        this.result = result;
    }
    public List<ClassResult> getResult() {
        return result;
    }
}
