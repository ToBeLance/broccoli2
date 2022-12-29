package com.kun.myapplication.bean.jingdongRecipe.searchAndByclass;

import com.kun.myapplication.bean.jingdongRecipe.Recipe;

import java.util.List;

public class SearchAndByClassRecipeResult
{
    private int total;

    private int num;

    private List<Recipe> list;

    public void setTotal(int total){
        this.total = total;
    }
    public int getTotal(){
        return this.total;
    }
    public void setNum(int num){
        this.num = num;
    }
    public int getNum(){
        return this.num;
    }
    public void setList(List<Recipe> list){
        this.list = list;
    }
    public List<Recipe> getList(){
        return this.list;
    }
}
