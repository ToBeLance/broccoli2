package com.kun.myapplication.bean.jingdongRecipe;

public class Material
{
    private String mname;

    private int type;

    private String amount;

    public void setMname(String mname){
        this.mname = mname;
    }
    public String getMname(){
        return this.mname;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getType(){
        return this.type;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }
    public String getAmount(){
        return this.amount;
    }

    @Override
    public String toString() {
        return "Material{" +
                "mname='" + mname + '\'' +
                ", type=" + type +
                ", amount='" + amount + '\'' +
                '}';
    }
}
