package com.kun.myapplication.bean.jingdongRecipe.recipeClass;

import java.util.List;

public class ClassResult {
    private int classid;
    private String name;
    private int parentid;
    private List<SubClass> list;
    public void setClassid(int classid) {
        this.classid = classid;
    }
    public int getClassid() {
        return classid;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
    public int getParentid() {
        return parentid;
    }

    public void setList(List<SubClass> list) {
        this.list = list;
    }
    public List<SubClass> getList() {
        return list;
    }
}
