package com.kun.myapplication;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.myServer.Comment;
import com.kun.myapplication.bean.myServer.Recommend;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MinePageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MinePageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View rootView;
    private TextView userName;
    private RecyclerView favoriteConfiguration;
    private Map<String,String> map = new HashMap<>();
    private List<Integer> recommendClassIdList = new ArrayList<>();
    private String[] favoriteClass = {"功效","人群","疾病","体质","菜系","小吃","菜品","口味","加工工艺","厨房用具","场景"};
    private Integer[] favoriteClassId = {1,113,144,213,223,269,301,390,453,524,561};
    private Boolean[] isSelectList = {false,false,false,false,false,false,false,false,false,false,false};

    public MinePageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MinePageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MinePageFragment newInstance(String param1, String param2) {
        MinePageFragment fragment = new MinePageFragment();
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
        rootView = inflater.inflate(R.layout.fragment_mine_page, container, false);
        userName = rootView.findViewById(R.id.user_name);
        favoriteConfiguration = rootView.findViewById(R.id.favorite_configuration);

        userName.setText("" + LoginActivity.USER_NAME);
        initRecommend();
        return rootView;
    }

    private void initRecommend() {
        map.put("userId",LoginActivity.USER_ID);
        RetrofitUtil.getFromMyServer("/Recommend/getRecommendByUserId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                List<Recommend> recommendList = new ArrayList<>();
                recommendList = new Gson().fromJson(resultJsonString, new TypeToken<ArrayList<Recommend>>(){}.getType());
                for (int i = 0; i < recommendList.size(); i++) {
                    recommendClassIdList.add(recommendList.get(i).getClassId());
                }
                favoriteConfiguration.setLayoutManager(new LinearLayoutManager(getContext()));
                favoriteConfiguration.setAdapter(new MyFavoriteConfigurationAdapter());
            }

            @Override
            public void onError(Throwable t) {
            }
        });
        map.clear();
    }

    private void insertRecommend(String classId) {
        map.put("userId",LoginActivity.USER_ID);
        map.put("classId",classId);
        RetrofitUtil.getFromMyServer("/Recommend/insertRecommend", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private void deleteRecommend(String classId) {
        map.put("userId",LoginActivity.USER_ID);
        map.put("classId",classId);
        RetrofitUtil.getFromMyServer("/Recommend/deleteRecommend", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {

            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private class MyFavoriteConfigurationAdapter extends RecyclerView.Adapter<MyFavoriteConfigurationAdapter.ViewHolder> {

        public MyFavoriteConfigurationAdapter() {

        }

        @NonNull
        @Override
        public MyFavoriteConfigurationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_favorite_configuration,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyFavoriteConfigurationAdapter.ViewHolder holder, int position) {
            int pos = position;
            if (recommendClassIdList.contains(favoriteClassId[pos])) {
                holder.select.setImageResource(R.drawable.minepage_fragment_light_select);
                isSelectList[pos] = true;
            }
            holder.favoriteClass.setText(favoriteClass[pos]);
            holder.select.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public void onClick(View view) {
                    if (isSelectList[pos]) {
                        deleteRecommend(String.valueOf(favoriteClassId[pos]));
                        holder.select.setImageResource(R.drawable.minepage_fragment_dark_select);
                        isSelectList[pos] = false;
                        return;
                    }
                    if (!isSelectList[pos]) {
                        insertRecommend(String.valueOf(favoriteClassId[pos]));
                        holder.select.setImageResource(R.drawable.minepage_fragment_light_select);
                        isSelectList[pos] = true;
                        return;
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return favoriteClass.length;
        }


        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView favoriteClass;
            private ImageView select;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                favoriteClass = itemView.findViewById(R.id.favorite_class);
                select = itemView.findViewById(R.id.select);
            }
        }
    }
}