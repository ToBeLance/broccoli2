package com.kun.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.kun.myapplication.bean.jingdongRecipe.Recipe;
import com.kun.myapplication.bean.jingdongRecipe.searchAndByclass.SearchAndByClassRoot;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {
    private String subClassId;
    private EditText searchBox;
    private ImageView back;
    private RecyclerView rvRecommend,rvSearch;
    private  String[] recommends = {"减肥","美白","祛痘","孕妇","儿童","空气炸锅","学生"};
    private String[] recommendsId = {"2","8","11","114","127","537","129"};
    private List<Recipe> recipeList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        subClassId = getIntent().getStringExtra("subClassId");

        searchBox = findViewById(R.id.search_box);
        back = findViewById(R.id.back);
        rvRecommend = findViewById(R.id.rv_recommend);
        rvSearch = findViewById(R.id.rv_search);

        searchBox.setFocusable(true);
        searchBox.setFocusableInTouchMode(true);
        searchBox.requestFocus();
        //显示软键盘
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        if (null != subClassId) {
            startRecommendSearch(subClassId);
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SearchActivity.this,MainActivity.class));
            }
        });

        searchBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(textView.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        startSearch();
                    }
                    return true;
                }
                return false;
            }
        });

        rvRecommend.setLayoutManager(new GridLayoutManager(this,3));
        rvRecommend.setAdapter(new MyRecommendRVAdapter());
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
    }

    private void startSearch() {
        rvRecommend.setVisibility(View.GONE);
        Map<String,String> map = new HashMap<>();
        map.put("keyword",searchBox.getText().toString());
        map.put("num","10");
        map.put("start","0");
        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
        RetrofitUtil.getFromJingDongRecipe("search", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                SearchAndByClassRoot root = new Gson().fromJson(resultJsonString, SearchAndByClassRoot.class);
                recipeList = root.getResult().getRecipeResult().getList();
                //Log.d("lance", "onSuccess: " + recipeList);
                rvSearch.setAdapter(new MySearchRVAdapter());
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private void startRecommendSearch(String recommendsId) {
        rvRecommend.setVisibility(View.GONE);
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (methodManager.isActive()){
            methodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
        Map<String,String> map = new HashMap<>();
        map.put("classid",recommendsId);
        map.put("start","0");
        map.put("num","10");
        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
        RetrofitUtil.getFromJingDongRecipe("byclass", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                SearchAndByClassRoot root = new Gson().fromJson(resultJsonString, SearchAndByClassRoot.class);
                recipeList = root.getResult().getRecipeResult().getList();
                //Log.d("lance", "onSuccess: " + recipeList);
                rvSearch.setAdapter(new MySearchRVAdapter());
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private class MyRecommendRVAdapter extends RecyclerView.Adapter<MyRecommendRVAdapter.ViewHolder> {

        @NonNull
        @Override
        public MyRecommendRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_recommend,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecommendRVAdapter.ViewHolder holder, int position) {
            int pos = position;
            holder.textView.setText("" + recommends[pos]);
            holder.itemSearchRecommend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startRecommendSearch(recommendsId[pos]);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recommends.length;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView textView;
            private LinearLayout itemSearchRecommend;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.tv_recommend);
                itemSearchRecommend = itemView.findViewById(R.id.item_search_recommend);
            }
        }
    }

    private class MySearchRVAdapter extends RecyclerView.Adapter<MySearchRVAdapter.ViewHolder> {

        @NonNull
        @Override
        public MySearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MySearchRVAdapter.ViewHolder holder, int position) {
            Recipe recipe = new Recipe();
            recipe = recipeList.get(position);
            Glide.with(SearchActivity.this)
                    .load(recipe.getPic())
                    .into(holder.recipePic);
            holder.recipeName.setText("" + recipe.getName());
            holder.recipeTag.setText("" + recipe.getTag());
            holder.recipeContent.setText("" + recipe.getContent().replace("<br />",System.getProperty("line.separator")));
            Recipe finalRecipe = recipe;
            holder.itemSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchActivity.this,RecipeActivity.class);
                    intent.putExtra("recipeId", "" + finalRecipe.getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView recipePic;
            private TextView recipeName,recipeTag,recipeContent;
            private LinearLayout itemSearch;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recipePic = itemView.findViewById(R.id.recipe_pic);
                recipeName = itemView.findViewById(R.id.recipe_name);
                recipeTag = itemView.findViewById(R.id.recipe_tag);
                recipeContent = itemView.findViewById(R.id.recipe_content);
                itemSearch = itemView.findViewById(R.id.item_search);
            }
        }
    }
}