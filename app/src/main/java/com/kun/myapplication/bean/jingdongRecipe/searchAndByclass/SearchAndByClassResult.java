package com.kun.myapplication.bean.jingdongRecipe.searchAndByclass;

public class SearchAndByClassResult
{
    private int status;

    private String msg;

    private SearchAndByClassRecipeResult result;

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
    public void setRecipeResult(SearchAndByClassRecipeResult result){
        this.result = result;
    }
    public SearchAndByClassRecipeResult getRecipeResult(){
        return this.result;
    }
}
