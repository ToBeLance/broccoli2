package com.kun.myapplication.bean.jingdongRecipe;

import java.util.List;

public class Recipe
{
    private int id;

    private int classid;

    private String name;

    private String peoplenum;

    private String preparetime;

    private String cookingtime;

    private String content;

    private String pic;

    private String tag;

    private List<Material> material;

    private List<Process> process;

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    public void setClassid(int classid){
        this.classid = classid;
    }
    public int getClassid(){
        return this.classid;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setPeoplenum(String peoplenum){
        this.peoplenum = peoplenum;
    }
    public String getPeoplenum(){
        return this.peoplenum;
    }
    public void setPreparetime(String preparetime){
        this.preparetime = preparetime;
    }
    public String getPreparetime(){
        return this.preparetime;
    }
    public void setCookingtime(String cookingtime){
        this.cookingtime = cookingtime;
    }
    public String getCookingtime(){
        return this.cookingtime;
    }
    public void setContent(String content){
        this.content = content;
    }
    public String getContent(){
        return this.content;
    }
    public void setPic(String pic){
        this.pic = pic;
    }
    public String getPic(){
        return this.pic;
    }
    public void setTag(String tag){
        this.tag = tag;
    }
    public String getTag(){
        return this.tag;
    }
    public void setMaterial(List<Material> material){
        this.material = material;
    }
    public List<Material> getMaterial(){
        return this.material;
    }
    public void setProcess(List<Process> process){
        this.process = process;
    }
    public List<Process> getProcess(){
        return this.process;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "id=" + id +
                ", classid=" + classid +
                ", name='" + name + '\'' +
                ", peoplenum='" + peoplenum + '\'' +
                ", preparetime='" + preparetime + '\'' +
                ", cookingtime='" + cookingtime + '\'' +
                ", content='" + content + '\'' +
                ", pic='" + pic + '\'' +
                ", tag='" + tag + '\'' +
                ", material=" + material +
                ", process=" + process +
                '}';
    }
}
