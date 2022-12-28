package com.kun.myapplication.bean.jingdongRecipe;

public class Result
{
    private int status;

    private String msg;

    private RecipeResult recipeResult;

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
    public void setRecipeResult(RecipeResult recipeResult){
        this.recipeResult = recipeResult;
    }
    public RecipeResult getRecipeResult(){
        return this.recipeResult;
    }
}
