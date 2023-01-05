package com.kun.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.myServer.Comment;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {
    private String recipeId;
    private List<Comment> commentList = new ArrayList<>();
    private Map<String,String> map = new HashMap<>();
    private ImageView back;
    private ImageView noComment;
    private RecyclerView comment;
    private ImageView closeKeyboard;
    private EditText sendNote;
    private ImageView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Log.d("lance", "onCreate: ");

        recipeId = getIntent().getStringExtra("recipeId");

        back = findViewById(R.id.back);
        noComment = findViewById(R.id.no_comment);
        comment = findViewById(R.id.comment);
        closeKeyboard = findViewById(R.id.close_keyboard);
        sendNote = findViewById(R.id.send_note);
        send = findViewById(R.id.send);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        closeKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKey();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSoftKey();
                send();
            }
        });

        refreshComment();

    }

    private void send() {

        if (!"".contentEquals(sendNote.getText())){
            map.put("recipeId",recipeId);
            map.put("userName",LoginActivity.USER_NAME);
            Calendar calendar = Calendar.getInstance(); // get current instance of the calendar
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            map.put("time",formatter.format(calendar.getTime()));
            map.put("content",sendNote.getText().toString());
            RetrofitUtil.getFromMyServer("/Comment/insertComment", map, new RetrofitCallback() {
                @Override
                public void onSuccess(String resultJsonString) {
                    sendNote.setText("");
                    refreshComment();
                }

                @Override
                public void onError(Throwable t) {

                }
            });
            map.clear();
        } else {
            Toast.makeText(NoteActivity.this,"发送内容不能为空！",Toast.LENGTH_SHORT).show();
        }

    }

    private void refreshComment() {
        map.put("recipeId",recipeId);
        RetrofitUtil.getFromMyServer("/Comment/getCommentByRecipeId", map, new RetrofitCallback() {
            @Override
            public void onSuccess(String resultJsonString) {
                commentList = new Gson().fromJson(resultJsonString,new TypeToken<ArrayList<Comment>>(){}.getType());
                if (commentList.size() == 0) {
                    noComment.setVisibility(View.VISIBLE);
                } else {
                    noComment.setVisibility(View.GONE);
                }
                comment.setLayoutManager(new LinearLayoutManager(NoteActivity.this));
                comment.setAdapter(new MyCommentRVAdapter());
                //Log.d("lance", "onSuccess: " + commentList);
            }

            @Override
            public void onError(Throwable t) {

            }
        });
        map.clear();
    }

    private class MyCommentRVAdapter extends RecyclerView.Adapter<MyCommentRVAdapter.ViewHolder> {

        @NonNull
        @Override
        public MyCommentRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(NoteActivity.this).inflate(R.layout.item_comment,null));
        }

        @Override
        public void onBindViewHolder(@NonNull MyCommentRVAdapter.ViewHolder holder, int position) {
            Comment comment = commentList.get(position);
            holder.userName.setText("" + comment.getUserName());
            holder.date.setText("" + comment.getTime());
            holder.content.setText("" + comment.getContent());
        }

        @Override
        public int getItemCount() {
            return commentList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView userName,date,content;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                userName = itemView.findViewById(R.id.user_name);
                date = itemView.findViewById(R.id.comment_date);
                content = itemView.findViewById(R.id.comment_content);
            }
        }
    }


    private void hideSoftKey(){
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (methodManager.isActive()){
            methodManager.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),0);
        }
    }

}