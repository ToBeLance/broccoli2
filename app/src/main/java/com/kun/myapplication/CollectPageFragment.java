package com.kun.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.jingdongRecipe.Recipe;
import com.kun.myapplication.bean.jingdongRecipe.detail.DetailRoot;
import com.kun.myapplication.bean.myServer.Collect;
import com.kun.myapplication.bean.myServer.Score;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CollectPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private Map<String,String> map = new HashMap<>();
    private TextView delete;
    private RecyclerView collectRecipe;
    private MyCollectRecipeRVAdapter myCollectRecipeRVAdapter;
    private Boolean isDeleting = false;

    public CollectPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CollectPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectPageFragment newInstance(String param1, String param2) {
        CollectPageFragment fragment = new CollectPageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_collect_page, container, false);
        delete = rootView.findViewById(R.id.delete);
        collectRecipe = rootView.findViewById(R.id.collect_recipe);
        getCollectRecipe();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (delete.getText().equals("删除收藏")) {
                    delete.setText("" + "结束删除");
                    isDeleting = true;
                    myCollectRecipeRVAdapter.notifyDataSetChanged();
                    return;
                }
                if (delete.getText().equals("结束删除")) {
                    delete.setText("" + "删除收藏");
                    isDeleting = false;
                    myCollectRecipeRVAdapter.notifyDataSetChanged();
                    return;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCollectRecipe();
    }

    private void getCollectRecipe() {
        map.clear();
        map.put("userId",LoginActivity.USER_ID);
        RetrofitUtil.getFromMyServer("/Collect/getCollectByUserId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                //Log.d("lance", "onSuccess: " + resultJsonString);
                if (collectRecipe.getVisibility() == View.GONE) {
                    collectRecipe.setVisibility(View.VISIBLE);
                }
                List<Collect> collectList = new Gson().fromJson(resultJsonString,new TypeToken<ArrayList<Collect>>(){}.getType());
                List<Recipe> recipeList = new ArrayList<>();
                for (int i = 0; i < collectList.size(); i++) {
                    map.clear();
                    map.put("id", String.valueOf(collectList.get(i).getRecipeId()));
                    map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
                    RetrofitUtil.getFromJingDongRecipe("detail", map, new RetrofitCallback() {
                        @Override
                        public void onSuccess(String resultJsonString) {
                            Log.d("lance", "onSuccess: " + resultJsonString);
                            DetailRoot root = new Gson().fromJson(resultJsonString,DetailRoot.class);
                            Recipe recipeResult = root.getResult().getRecipeResult();
                            recipeList.add(recipeResult);
                            collectRecipe.setLayoutManager(new LinearLayoutManager(getActivity()));
                            myCollectRecipeRVAdapter = new MyCollectRecipeRVAdapter(recipeList);
                            collectRecipe.setAdapter(myCollectRecipeRVAdapter);
                        }

                        @Override
                        public void onError(Throwable t) {

                        }
                    });
                }
                if (collectList.size() == 0) {
                    collectRecipe.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable t) {

            }
        });
    }

    private class MyCollectRecipeRVAdapter extends RecyclerView.Adapter<MyCollectRecipeRVAdapter.ViewHolder> {
        List<Recipe> recipeList = new ArrayList<>();

        public MyCollectRecipeRVAdapter() {
        }

        public MyCollectRecipeRVAdapter(List<Recipe> recipeList) {
            this.recipeList = recipeList;
        }

        @NonNull
        @Override
        public MyCollectRecipeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_search,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCollectRecipeRVAdapter.ViewHolder holder, int position) {
            Recipe recipe = new Recipe();
            recipe = recipeList.get(position);
            Glide.with(getActivity())
                    .load(recipe.getPic())
                    .into(holder.recipePic);
            holder.recipeName.setText("" + recipe.getName());
            holder.recipeTag.setText("" + recipe.getTag());
            holder.recipeContent.setText("" + recipe.getContent().replace("<br />",System.getProperty("line.separator")));
            Recipe finalRecipe = recipe;
            holder.itemSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),RecipeActivity.class);
                    intent.putExtra("recipeId", "" + finalRecipe.getId());
                    startActivity(intent);
                }
            });
            if (isDeleting) {
                holder.delete.setVisibility(View.VISIBLE);
                Recipe finalRecipe1 = recipe;
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        map.clear();
                        map.put("userId",LoginActivity.USER_ID);
                        map.put("recipeId","" + finalRecipe1.getId());
                        RetrofitUtil.getFromMyServer("/Collect/deleteCollectByUserIdAndRecipeId", map, new RetrofitCallback() {
                            @Override
                            public void onSuccess(String resultJsonString) {
                                Toast.makeText(getActivity(),"已经删除改收藏的菜谱",Toast.LENGTH_SHORT).show();
                                getCollectRecipe();
                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
                    }
                });
            }
            if (!isDeleting) {
                holder.delete.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return recipeList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView recipePic;
            private TextView recipeName,recipeTag,recipeContent;
            private LinearLayout itemSearch;
            private ImageView delete;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recipePic = itemView.findViewById(R.id.recipe_pic);
                recipeName = itemView.findViewById(R.id.recipe_name);
                recipeTag = itemView.findViewById(R.id.recipe_tag);
                recipeContent = itemView.findViewById(R.id.recipe_content);
                itemSearch = itemView.findViewById(R.id.item_search);
                delete = itemView.findViewById(R.id.delete);
            }
        }
    }
}