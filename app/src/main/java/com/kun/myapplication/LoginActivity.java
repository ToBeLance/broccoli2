package com.kun.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kun.myapplication.bean.myServer.User;
import com.kun.myapplication.utils.net.RetrofitCallback;
import com.kun.myapplication.utils.net.RetrofitUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText name,password;
    private Button login,register;
    public static String USER_ID = "3";
    public static String USER_NAME = "king";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        register = findViewById(R.id.register);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() < 1 || name.length() > 10) {
                    Toast.makeText(LoginActivity.this,"name's length isn't right",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map.put("name",name.getText().toString());
                RetrofitUtil.getFromMyServer("/User/getUser", map, new RetrofitCallback() {
                    @Override
                    public void onSuccess(String resultJsonString) {
                        List<User> users = new ArrayList<>();
                        users = new Gson().fromJson(resultJsonString, new TypeToken<List<User>>() {}.getType());
                        if (users.size() != 0) {
                            User user = users.get(0);
                            if (!user.getPassword().equals(password.getText().toString())) {
                                Toast.makeText(LoginActivity.this,"password error",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            USER_ID = String.valueOf(user.getId());
                            USER_NAME = user.getName();
                            Toast.makeText(LoginActivity.this,"login success " + user.getName() ,Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            return;
                        }
                        Toast.makeText(LoginActivity.this,"not this user,please register",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });

            }
        });
        //
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.length() < 1 || name.length() > 10) {
                    Toast.makeText(LoginActivity.this,"name's length isn't right",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 6 || password.length() > 10) {
                    Toast.makeText(LoginActivity.this,"password's length isn't right",Toast.LENGTH_SHORT).show();
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map.put("name",name.getText().toString());
                RetrofitUtil.getFromMyServer("/User/getUser", map, new RetrofitCallback() {
                    @Override
                    public void onSuccess(String resultJsonString) {
                        List<User> users = new ArrayList<>();
                        users = new Gson().fromJson(resultJsonString, new TypeToken<List<User>>() {}.getType());
                        if (users.size() != 0) {
                            Toast.makeText(LoginActivity.this,users.get(0).getName() + " is exist ,please use other name",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Map<String,String> map1 = new HashMap<>();
                        map1.put("name",name.getText().toString());
                        map1.put("password",password.getText().toString());
                        RetrofitUtil.getFromMyServer("/User/insertUser", map1, new RetrofitCallback() {
                            @Override
                            public void onSuccess(String resultJsonString) {
                                Toast.makeText(LoginActivity.this,"not this user,register success!!!",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable t) {

                    }
                });
            }
        });
    }
}