package com.kun.myapplication.bean.myServer;

public class Score {
    private int recipeId;
    private int userId;
    private int score;

    public Score() {
    }

    public Score(int recipeId, int userId, int score) {
        this.recipeId = recipeId;
        this.userId = userId;
        this.score = score;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Score{" +
                "recipeId=" + recipeId +
                ", userId=" + userId +
                ", score=" + score +
                '}';
    }
}
