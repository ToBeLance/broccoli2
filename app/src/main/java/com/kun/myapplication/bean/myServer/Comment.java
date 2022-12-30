package com.kun.myapplication.bean.myServer;


public class Comment {
    private int commentId;
    private int recipeId;
    private String userName;
    private String time;
    private String content;

    public Comment() {
    }

    public Comment(int commentId, int recipeId, String userName, String time, String content) {
        this.commentId = commentId;
        this.recipeId = recipeId;
        this.userName = userName;
        this.time = time;
        this.content = content;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", recipeId=" + recipeId +
                ", userName='" + userName + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
