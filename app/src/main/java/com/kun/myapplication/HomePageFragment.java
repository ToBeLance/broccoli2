package com.kun.myapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.jingdongRecipe.Recipe;
import com.kun.myapplication.bean.jingdongRecipe.recipeClass.ClassResult;
import com.kun.myapplication.bean.jingdongRecipe.recipeClass.ClassRoot;
import com.kun.myapplication.bean.jingdongRecipe.recipeClass.SubClass;
import com.kun.myapplication.bean.jingdongRecipe.searchAndByclass.SearchAndByClassRoot;
import com.kun.myapplication.bean.myServer.Recommend;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Map<String,String> map = new HashMap<>();
    //626为特殊，为子级别分类的最后一个类别数-1，非父级别分类，只是单纯为了方便逻辑代码编写，才这样写的
    private Integer[] favoriteClassId = {1,113,144,213,223,269,301,390,453,524,561,626};
    private String[] favoriteClass = {"功效","人群","疾病","体质","菜系","小吃","菜品","口味","加工工艺","厨房用具","场景"};
    private View rootView;
    private EditText searchBox;
    private RecyclerView rootClass,subClass;
    private TextView recommendTip;
    private RecyclerView recommend;
    private List<ClassResult> classResultList = new ArrayList<>();

    public HomePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
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
        rootView = inflater.inflate(R.layout.fragment_home_page, container, false);
        rootClass = rootView.findViewById(R.id.root_class);
        subClass = rootView.findViewById(R.id.sub_class);
        searchBox = rootView.findViewById(R.id.search_box);
        recommendTip = rootView.findViewById(R.id.recommend_tip);
        recommend = rootView.findViewById(R.id.recommend);
        searchBox.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });

        getRecipeClass();

        getRecommend();

        return rootView;
    }

    private void getRecipeClass() {
        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
        RetrofitUtil.getFromJingDongRecipe("recipe_class", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                ClassRoot classRoot = new ClassRoot();
                classRoot = new Gson().fromJson(resultJsonString,ClassRoot.class);
                classResultList = classRoot.getResult().getResult();
                rootClass.setLayoutManager(new LinearLayoutManager(getActivity()));
                rootClass.setAdapter(new MyRootClassRVAdapter());
                subClass.setLayoutManager(new GridLayoutManager(getActivity(),3));
                subClass.setAdapter(new MySubClassRVAdapter(classResultList.get(0).getList()));
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void getRecommend() {
        map.put("userId",LoginActivity.USER_ID);
        RetrofitUtil.getFromMyServer("/Recommend/getRecommendByUserId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                List<Recommend> recommendList = new ArrayList<>();
                recommendList = new Gson().fromJson(resultJsonString, new TypeToken<ArrayList<Recommend>>(){}.getType());
                Random random = new Random();
                int randomIndex = random.nextInt(recommendList.size());
                for (int i = 0; i < favoriteClassId.length; i++) {
                    if (recommendList.get(randomIndex).getClassId() == favoriteClassId[i]) {
                        recommendTip.setText("" + "为您推荐的" + favoriteClass[i] + "系列菜谱");
                        int randomSubClassId = random.nextInt(favoriteClassId[i + 1] - recommendList.get(randomIndex).getClassId() - 1) + recommendList.get(randomIndex).getClassId() + 1;
                        Log.d("lance", "onSuccess: " + randomSubClassId);
                        map.put("classid", String.valueOf(randomSubClassId));
                        map.put("start","0");
                        map.put("num","10");
                        map.put("appkey","ac3df7dc8589d4a2fac3ece1990f7c14");
                        RetrofitUtil.getFromJingDongRecipe("byclass", map, new RetrofitCallback() {
                            @Override
                            public void onSuccess(String resultJsonString) {
                                Log.d("lance", "onSuccess: " + resultJsonString);
                                SearchAndByClassRoot root = new Gson().fromJson(resultJsonString,SearchAndByClassRoot.class);
                                List<Recipe> recipeList = root.getResult().getRecipeResult().getList();
                                recommend.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recommend.setAdapter(new MyRecommendRVAdapter(recipeList));
                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
                        map.clear();

                    }
                }


            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private class MyRootClassRVAdapter extends RecyclerView.Adapter<MyRootClassRVAdapter.ViewHolder> {

        @NonNull
        @Override
        public MyRootClassRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_class,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyRootClassRVAdapter.ViewHolder holder, int position) {
            ClassResult classResult = classResultList.get(position);
            holder.recipeClassName.setText("" + classResult.getName());
            holder.recipeClassName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    subClass.setAdapter(new MySubClassRVAdapter(classResult.getList()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return classResultList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView recipeClassName;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recipeClassName = itemView.findViewById(R.id.recipe_class_name);
            }
        }
    }

    private class MySubClassRVAdapter extends RecyclerView.Adapter<MySubClassRVAdapter.ViewHolder> {
        private List<SubClass> subClassList = new ArrayList<>();

        public MySubClassRVAdapter(List<SubClass> subClassList) {
            this.subClassList = subClassList;
        }

        @NonNull
        @Override
        public MySubClassRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_class,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MySubClassRVAdapter.ViewHolder holder, int position) {
            SubClass subClass = new SubClass();
            subClass = subClassList.get(position);
            holder.recipeClassName.setText("" + subClass.getName());
            SubClass finalSubClass = subClass;
            holder.recipeClassName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(),SearchActivity.class);
                    intent.putExtra("subClassId","" + finalSubClass.getClassid());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return subClassList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView recipeClassName;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recipeClassName = itemView.findViewById(R.id.recipe_class_name);
            }
        }
    }

    private class MyRecommendRVAdapter extends RecyclerView.Adapter<MyRecommendRVAdapter.ViewHolder> {
        private List<Recipe> recipeList = new ArrayList<>();

        public MyRecommendRVAdapter(List<Recipe> recipeList) {
            this.recipeList = recipeList;
        }

        @NonNull
        @Override
        public MyRecommendRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(R.layout.item_search,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyRecommendRVAdapter.ViewHolder holder, int position) {
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