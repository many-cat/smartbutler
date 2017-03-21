package com.rc.darren.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.rc.darren.smartbutler.MainActivity;
import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.MyUser;
import com.rc.darren.smartbutler.utils.ShareUtils;
import com.rc.darren.smartbutler.utils.UtilTools;
import com.rc.darren.smartbutler.view.CustomDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_password;
    private Button btn_login;
    private Button btn_registered;
    private CheckBox is_check;
    private TextView tv_forget;

    private CustomDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        btn_registered = (Button) findViewById(R.id.btn_registered);
        btn_registered.setOnClickListener(this);
        is_check = (CheckBox) findViewById(R.id.is_check);
        tv_forget = (TextView) findViewById(R.id.tv_forget);
        tv_forget.setOnClickListener(this);

        mDialog = new CustomDialog(this, 100, 100, R.layout.dialog_loding,R.style.dialog, Gravity.CENTER,R.style.dialog);
        //不可点击pop_anim_style
        mDialog.setCancelable(false);

        boolean isKeep = ShareUtils.getBoolean(this, "keeppass", false);
        is_check.setChecked(isKeep);

        if (isKeep) {
            et_name.setText(ShareUtils.getString(this, "name", ""));
            et_password.setText(ShareUtils.getString(this, "password", ""));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.btn_login:
                //1 获取账号密码
                String name = et_name.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                //2 判断是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    mDialog.show();
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setPassword(password);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser user, BmobException e) {
                            mDialog.dismiss();
                            //判断结果
                            if (e == null) {
                                //通过BmobUser user = BmobUser.getCurrentUser()获取登录成功后的本地用户信息
                                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(MyUser.class)获取自定义用户信息
                                //判断邮箱是否验证
                                if (user.getEmailVerified()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    UtilTools.showToast(LoginActivity.this, R.string.text_register_remind);
                                }
                            } else {
                                UtilTools.showToast(LoginActivity.this,  "登录失败：" + e.toString());
                            }
                        }
                    });
                } else {
                    UtilTools.showNotNullToast(this);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ShareUtils.putBoolean(this, "keeppass", is_check.isChecked());

        if (is_check.isChecked()) {
            ShareUtils.putString(this, "name", et_name.getText().toString().trim());
            ShareUtils.putString(this, "password", et_password.getText().toString().trim());
        } else {
            ShareUtils.deleShare(this, "name");
            ShareUtils.deleShare(this, "password");
        }
    }
}
