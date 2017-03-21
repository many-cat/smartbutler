package com.rc.darren.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.MyUser;
import com.rc.darren.smartbutler.utils.UtilTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisteredActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_user;
    private EditText et_age;
    private EditText et_desc;
    private EditText et_pass;
    private EditText et_password;
    private EditText et_email;

    private RadioGroup mRadioGroup;
    private Button btn_registered;

    private boolean isGender = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        initView();
    }

    private void initView() {
        et_user = (EditText) findViewById(R.id.et_user);
        et_age = (EditText) findViewById(R.id.et_age);
        et_desc = (EditText) findViewById(R.id.et_desc);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_password = (EditText) findViewById(R.id.et_password);
        et_email = (EditText) findViewById(R.id.et_email);
        mRadioGroup = (RadioGroup) findViewById(R.id.mRadioGroup);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registered:
                //获取输入框的值
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                //判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age)
                        && !TextUtils.isEmpty(desc) && !TextUtils.isEmpty(pass)
                        && !TextUtils.isEmpty(password)
                        && !TextUtils.isEmpty(email)) {
                    //判断密码是否一直
                    if (pass.equals(password)) {

                        //判断性别
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy) {
                                    isGender = true;
                                } else if (checkedId == R.id.rb_girl) {
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = getString(R.string.text_nothing);
                        }

                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setDesc(desc);
                        user.setSex(isGender);
                        user.setAge(Integer.parseInt(age));
                        user.signUp(new SaveListener<MyUser>() {

                            @Override
                            public void done(MyUser o, BmobException e) {
                                if (e == null) {
                                    UtilTools.showToast(RegisteredActivity.this,R.string.text_registered_successful);
                                    finish();
                                } else {
                                    UtilTools.showToast(RegisteredActivity.this,R.string.text_registered_failure);
                                }
                            }
                        });

                    } else {
                        UtilTools.showToast(RegisteredActivity.this,R.string.text_two_input_not_consistent);
                    }
                } else {
                    UtilTools.showNotNullToast(this);
                }
                break;
        }
    }
}
