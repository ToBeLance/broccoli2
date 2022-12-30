package com.kun.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.jingdongRecipe.Material;
import com.kun.myapplication.bean.jingdongRecipe.Process;
import com.kun.myapplication.bean.jingdongRecipe.Recipe;
import com.kun.myapplication.bean.jingdongRecipe.detail.DetailRoot;
import com.kun.myapplication.bean.myServer.Score;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {
    private Map<String,String> map = new HashMap<>();
    private String userId = LoginActivity.USER_ID;
    private Boolean isScore = false;
    private String recipeId;
    private Recipe recipe;
    private List<Material> materialList;
    private List<Process> processList;
    private ImageView back;
    private TextView recipeName,recipePrepareTime,recipeCookingTime,recipeScore,recipeScorePeopleNum,recipePeopleNum;
    private ImageView firstStar,secondStar,thirdStar,fourthStar,fifthStar;
    private LinearLayout recipeNote;
    private ImageView recipeImg;
    private TextView recipeTag,recipeContent;
    private LinearLayout materialUse,cookingProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipeId = getIntent().getStringExtra("recipeId");

        initView();

        refreshScore();

        setIsScore();

        initRecipe();

    }

    private void initView() {
        back = findViewById(R.id.back);
        materialUse = findViewById(R.id.material_use);
        cookingProcess = findViewById(R.id.cooking_process);
        recipeName = findViewById(R.id.recipe_name);
        recipePrepareTime = findViewById(R.id.recipe_prepare_time);
        recipeCookingTime = findViewById(R.id.recipe_cooking_time);
        recipeScore = findViewById(R.id.recipe_score);
        recipeScorePeopleNum = findViewById(R.id.recipe_score_people_number);
        recipePeopleNum = findViewById(R.id.recipe_people_num);
        firstStar = findViewById(R.id.first_star);
        secondStar = findViewById(R.id.second_star);
        thirdStar = findViewById(R.id.third_star);
        fourthStar = findViewById(R.id.fourth_star);
        fifthStar = findViewById(R.id.fifth_star);
        recipeNote = findViewById(R.id.recipe_note);
        recipeImg = findViewById(R.id.recipe_img);
        recipeTag = findViewById(R.id.recipe_tag);
        recipeContent = findViewById(R.id.recipe_content);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score(0,isScore,"20");
            }
        });
        secondStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score(1,isScore,"40");
            }
        });
        thirdStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score(2,isScore,"60");
            }
        });
        fourthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score(3,isScore,"80");
            }
        });
        fifthStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                score(4,isScore,"100");
            }
        });
        recipeNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeActivity.this,NoteActivity.class);
                intent.putExtra("recipeId",recipeId);
                startActivity(intent);
            }
        });
    }

    private void refreshScore() {
        map.put("recipeId",recipeId);
        RetrofitUtil.getFromMyServer("/Score/getScoreByRecipeId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                List<Score> scoreList = new ArrayList<>();
                //json --> list :new TypeToken<ArrayList<Score>>(){}.getType()
                scoreList = new Gson().fromJson(resultJsonString, new TypeToken<ArrayList<Score>>(){}.getType());
                //Log.d("lance", "onSuccess: " + resultJsonString);
                int sum = 0;
                for (int i = 0; i < scoreList.size(); i++) {
                    sum += scoreList.get(i).getScore();
                }
                recipeScore.setText("" + (sum / scoreList.size()) + "分");
                recipeScorePeopleNum.setText("" + scoreList.size() + "人");
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void setIsScore() {
        //Log.d("lance", "onCreate: " + LoginActivity.USER_ID);
        map.put("recipeId",recipeId);
        map.put("userId",userId);
        RetrofitUtil.getFromMyServer("/Score/getRecipeScoreByRecipeIdAndUserId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                List<Score> scoreList = new Gson().fromJson(resultJsonString,new TypeToken<ArrayList<Score>>(){}.getType());
                //Log.d("lance", "onSuccess: " + scoreList);
                if (scoreList.size() > 0 ) {
                    ///Log.d("lance", "onSuccess: " + "yes");
                    isScore = true;
                }
                if (scoreList.size() == 0) {
                    isScore = false;
                    //Log.d("lance", "onSuccess: " + "no");
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void score(int grade,boolean isScore,String score) {
        switch (grade) {
            case 0:
                if (isScore) {
                    updateScore(score);
                }
                if (!isScore) {
                    insertScore(score);
                }
                firstStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                secondStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                thirdStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                fourthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                fifthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                break;
            case 1:
                if (isScore) {
                    updateScore(score);
                }
                if (!isScore) {
                    insertScore(score);
                }
                firstStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                secondStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                thirdStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                fourthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                fifthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                break;
            case 2:
                if (isScore) {
                    updateScore(score);
                }
                if (!isScore) {
                    insertScore(score);
                }
                firstStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                secondStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                thirdStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                fourthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                fifthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                break;
            case 3:
                if (isScore) {
                    updateScore(score);
                }
                if (!isScore) {
                    insertScore(score);
                }
                firstStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                secondStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                thirdStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                fourthStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                fifthStar.setImageResource(R.drawable.recipe_activity_dark_star_icon);
                break;
            case 4:
                if (isScore) {
                    updateScore(score);
                }
                if (!isScore) {
                    insertScore(score);
                }
                firstStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                secondStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                thirdStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                fourthStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                fifthStar.setImageResource(R.drawable.recipe_activity_light_star_icon);
                break;
            default:
                break;
        }
    }

    private void insertScore(String score) {
        map.put("recipeId",recipeId);
        map.put("userId",userId);
        map.put("score",score);
        RetrofitUtil.getFromMyServer("/Score/insertRecipeScore", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                Toast.makeText(RecipeActivity.this,"加入评分" + score + "分",Toast.LENGTH_SHORT).show();
                setIsScore();
                refreshScore();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void updateScore(String score) {
        map.put("recipeId",recipeId);
        map.put("userId",userId);
        map.put("score",score);
        RetrofitUtil.getFromMyServer("Score/updateRecipeScore", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                Toast.makeText(RecipeActivity.this,"修改评分为" + score + "分",Toast.LENGTH_SHORT).show();
                refreshScore();
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void initRecipe() {
        map.put("id",recipeId);
        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
        RetrofitUtil.getFromJingDongRecipe("detail", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                DetailRoot root = new Gson().fromJson(resultJsonString,DetailRoot.class);
                recipe = root.getResult().getRecipeResult();
                materialList = recipe.getMaterial();
                processList = recipe.getProcess();
                //Log.d("lance", "onSuccess: " + root.getResult().getRecipeResult());
                recipeName.setText("" + recipe.getName());
                recipePrepareTime.setText("" + recipe.getPreparetime());
                recipeCookingTime.setText("" + recipe.getCookingtime());
                recipePeopleNum.setText("" + recipe.getPeoplenum());
                Glide.with(RecipeActivity.this)
                        .load(recipe.getPic())
                        .into(recipeImg);
                recipeTag.setText("标签： " + recipe.getTag());
                recipeContent.setText("" + recipe.getContent().replace("<br />",System.getProperty("line.separator")));


                for (int i = 0; i < materialList.size(); i++) {
                    LinearLayout layout = new LinearLayout(RecipeActivity.this);
                    TextView textView = new TextView(RecipeActivity.this);
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(14f);
                    textView.setText("" + materialList.get(i).getMname());
                    //此处我需要均分高度就在heignt处设0,1.0f即设置权重是1，页面还有其他一个控件,1：1高度就均分了
                    LinearLayout.LayoutParams topContentTextView_lp=new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT,1.0f);
                    layout.addView(textView,topContentTextView_lp);

                    TextView textView1 = new TextView(RecipeActivity.this);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setTextSize(14f);
                    textView1.setText("" + materialList.get(i).getAmount());
                    layout.addView(textView1);
                    materialUse.addView(layout);
                }

                for (int i = 0; i < processList.size(); i++) {
                    TextView textView = new TextView(RecipeActivity.this);
                    textView.setText("步骤" + (i+1));
                    textView.setTextColor(Color.BLACK);
                    textView.setTextSize(16f);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView.setLayoutParams(layoutParams);
                    LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) textView.getLayoutParams();
                    layoutParams1.setMargins(0,60,0,0);
                    textView.setLayoutParams(layoutParams1);
                    cookingProcess.addView(textView);

                    CardView cardView = new CardView(RecipeActivity.this);
                    LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    cardView.setLayoutParams(cardViewParams);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) cardView.getLayoutParams();
                    params.setMargins(0,20,0,0);
                    cardView.setLayoutParams(params);
                    params.gravity = Gravity.CENTER;
                    cardView.setRadius(25f);
                    cookingProcess.addView(cardView,cardViewParams);
                    ImageView imageView = new ImageView(RecipeActivity.this);
                    LinearLayout.LayoutParams imageViewParams = new LinearLayout.LayoutParams(985, 985);
                    Glide.with(RecipeActivity.this)
                            .load(processList.get(i).getPic())
                            .into(imageView);
                    cardView.addView(imageView,imageViewParams);

                    TextView textView1 = new TextView(RecipeActivity.this);
                    textView1.setTextSize(14f);
                    textView1.setTextColor(Color.BLACK);
                    textView1.setText("" + processList.get(i).getPcontent().replace("<br />",System.getProperty("line.separator")));
                    LinearLayout.LayoutParams layoutParams3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    textView1.setLayoutParams(layoutParams3);
                    LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) textView1.getLayoutParams();
                    layoutParams2.setMargins(0,20,0,0);
                    textView1.setLayoutParams(layoutParams2);
                    cookingProcess.addView(textView1);
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }
}