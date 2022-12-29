package com.kun.myapplication.bean.jingdongRecipe.detail;

import com.kun.myapplication.bean.jingdongRecipe.Recipe;

public class DetailResult
{
    private int status;

    private String msg;

    private Recipe result;

    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setRecipeResult(Recipe result){
        this.result = result;
    }
    public Recipe getRecipeResult(){
        return this.result;
    }
}
