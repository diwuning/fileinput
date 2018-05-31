package com.h.fileinput.userinfo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sugartest.UserInfo;
import com.example.sugartest.UserInfoDao;
import com.h.fileinput.R;

import java.util.List;

public class UserInfoActivity extends Activity {
    private static final String TAG = "UserInfoActivity";
    private TextView tv_nick,tv_birthday;
    private Button btn_save,btn_cancel;
    UserInfoDao userInfoDao;
    UserInfo passInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        userInfoDao = new UserInfoDao();
        initView();
        initData();
    }

    public void initView(){
        tv_nick = (TextView) findViewById(R.id.tv_nickName);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        //更新数据库
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passInfo.setUserName(tv_nick.getText().toString());
                passInfo.setBirthday(tv_birthday.getText().toString());
                userInfoDao.updateUserInfo(passInfo);

            }
        });
        //添加新成员
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserName(tv_nick.getText().toString());
                userInfo.setBirthday(tv_birthday.getText().toString());
                userInfoDao.addUser(userInfo);
            }
        });
    }

    public void initData(){
        List<UserInfo> userInfoList = userInfoDao.getAllUsers();
        if(userInfoList != null && userInfoList.size() != 0){
            Log.e(TAG,userInfoList.size()+"");
            passInfo = userInfoList.get(0);
            tv_nick.setText(passInfo.getUserName());
            tv_birthday.setText(passInfo.getBirthday());
        }
    }
}
