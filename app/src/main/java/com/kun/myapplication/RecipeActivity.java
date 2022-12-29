package com.kun.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kun.myapplication.bean.jingdongRecipe.Material;
import com.kun.myapplication.bean.jingdongRecipe.Process;
import com.kun.myapplication.bean.jingdongRecipe.Recipe;
import com.kun.myapplication.bean.jingdongRecipe.detail.DetailRoot;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeActivity extends AppCompatActivity {
    private String recipeId;
    private Recipe recipe;
    private List<Material> materialList;
    private List<Process> processList;
    private ImageView back;
    private TextView recipeName,recipePrepareTime,recipeCookingTime,recipePeopleNum;
    private ImageView recipeImg;
    private TextView recipeTag,recipeContent;
    private LinearLayout materialUse,cookingProcess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        recipeId = getIntent().getStringExtra("recipeId");

        back = findViewById(R.id.back);
        materialUse = findViewById(R.id.material_use);
        cookingProcess = findViewById(R.id.cooking_process);
        recipeName = findViewById(R.id.recipe_name);
        recipePrepareTime = findViewById(R.id.recipe_prepare_time);
        recipeCookingTime = findViewById(R.id.recipe_cooking_time);
        recipePeopleNum = findViewById(R.id.recipe_people_num);
        recipeImg = findViewById(R.id.recipe_img);
        recipeTag = findViewById(R.id.recipe_tag);
        recipeContent = findViewById(R.id.recipe_content);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Map<String,String> map = new HashMap<>();
        map.put("id",recipeId);
        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
        RetrofitUtil.getFromJingDongRecipe("detail", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                DetailRoot root = new Gson().fromJson(resultJsonString,DetailRoot.class);
                recipe = root.getResult().getRecipeResult();
                materialList = recipe.getMaterial();
                processList = recipe.getProcess();
                Log.d("lance", "onSuccess: " + root.getResult().getRecipeResult());
                recipeName.setText("" + recipe.getName());
                recipePrepareTime.setText("" + recipe.getPreparetime());
                recipeCookingTime.setText("" + recipe.getCookingtime());
                recipePeopleNum.setText("" + recipe.getPeoplenum());
                Glide.with(RecipeActivity.this)
                        .load(recipe.getPic())
                        .into(recipeImg);
                recipeTag.setText("标签： " + recipe.getTag());
                recipeContent.setText("" + recipe.getContent());


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
                    textView1.setText("" + processList.get(i).getPcontent());
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