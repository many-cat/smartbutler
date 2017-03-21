package com.rc.darren.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.MyUser;
import com.rc.darren.smartbutler.utils.UtilTools;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_mail;
    private Button btn_forget;

    private EditText et_now;
    private EditText et_new;
    private EditText et_new_password;
    private Button btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView() {
        et_mail = (EditText) findViewById(R.id.et_email);
        btn_forget = (Button) findViewById(R.id.btn_forget_password);
        btn_forget.setOnClickListener(this);

        et_now = (EditText) findViewById(R.id.et_now);
        et_new = (EditText) findViewById(R.id.et_new);
        et_new_password = (EditText) findViewById(R.id.et_new_password);
        btn_update = (Button) findViewById(R.id.btn_update_password);
        btn_update.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forget_password:
                //1 获取邮箱
                final String email = et_mail.getText().toString().trim();
                //2 判断是否为空
                if (!TextUtils.isEmpty(email)) {
                    //3 邮箱重置密码
                    MyUser.resetPasswordByEmail(email, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                UtilTools.showToast(ForgetPasswordActivity.this, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                                finish();
                            } else {
                                UtilTools.showToast(ForgetPasswordActivity.this, "失败:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    UtilTools.showNotNullToast(this);
                }
                break;
            case R.id.btn_update_password:
                //1 获取信息
                String now = et_now.getText().toString().trim();
                String news = et_new.getText().toString().trim();
                String new_password = et_new_password.getText().toString().trim();
                //2 判断是否为空
                if (!TextUtils.isEmpty(now) && !TextUtils.isEmpty(news) && !TextUtils.isEmpty(new_password)) {
                    //3 判断两次密码是否一致
                    if (news.equals(new_password)) {
                        //4 更改密码
                        MyUser.updateCurrentUserPassword(now, new_password, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    UtilTools.showToast(ForgetPasswordActivity.this, "密码修改成功，可以用新密码进行登录啦");
                                    finish();
                                } else {
                                    UtilTools.showToast(ForgetPasswordActivity.this, "失败:" + e.getMessage());
                                }
                            }
                        });
                    } else {
                        UtilTools.showToast(this, R.string.text_two_input_not_consistent);
                    }
                } else {
                    UtilTools.showNotNullToast(this);
                }
                break;
        }
    }
}
