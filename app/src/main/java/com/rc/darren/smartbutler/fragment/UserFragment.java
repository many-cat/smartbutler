package com.rc.darren.smartbutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.MyUser;
import com.rc.darren.smartbutler.ui.CourierActivity;
import com.rc.darren.smartbutler.ui.LoginActivity;
import com.rc.darren.smartbutler.ui.PhoneActivity;
import com.rc.darren.smartbutler.utils.L;
import com.rc.darren.smartbutler.utils.UtilTools;
import com.rc.darren.smartbutler.view.CustomDialog;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.fragment
 * 文件名：    ButlerFragment
 * 创建者：    Darren
 * 创建时间：  2017/2/20 15:44
 * 描述：      个人中心
 */

public class UserFragment extends Fragment implements View.OnClickListener {

    private Button btn_update_ok;
    private Button btn_exit_user;
    private TextView edit_user;

    //圆形图片
    private CircleImageView profile_image;
    private CustomDialog dialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;

    private EditText et_user;
    private EditText et_sex;
    private EditText et_age;
    private EditText et_desc;

    private TextView tv_courier;
    private TextView tv_phone;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        btn_exit_user = (Button) view.findViewById(R.id.btn_exit_user);
        btn_exit_user.setOnClickListener(this);
        btn_update_ok = (Button) view.findViewById(R.id.btn_update_ok);
        btn_update_ok.setOnClickListener(this);
        edit_user = (TextView) view.findViewById(R.id.edit_user);
        edit_user.setOnClickListener(this);
        tv_phone= (TextView) view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);

        profile_image = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        UtilTools.getImageToShare(getActivity(),profile_image);

        //初始化dialog
        dialog = new CustomDialog(getActivity(), 0, 0, R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);
        //点击边框外无效
        dialog.setCancelable(false);

        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_camera.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_picture.setOnClickListener(this);

        tv_courier= (TextView) view.findViewById(R.id.tv_courier);
        tv_courier.setOnClickListener(this);

        et_user = (EditText) view.findViewById(R.id.et_username);
        et_age = (EditText) view.findViewById(R.id.et_age);
        et_sex = (EditText) view.findViewById(R.id.et_sex);
        et_desc = (EditText) view.findViewById(R.id.et_desc);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        et_user.setText(userInfo.getUsername());
        et_age.setText(userInfo.getAge() + "");
        et_desc.setText(userInfo.getDesc());
        et_sex.setText(userInfo.getSex() ? getString(R.string.text_boy) : getString(R.string.text_girl));

        setEnabled(false);
    }

    private void setEnabled(boolean is) {
        et_age.setEnabled(is);
        et_user.setEnabled(is);
        et_sex.setEnabled(is);
        et_desc.setEnabled(is);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //退出登录
            case R.id.btn_exit_user:
                MyUser.logOut();   //清除缓存用户对象
                BmobUser currentUser = MyUser.getCurrentUser(); // 现在的currentUser是null了
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            //编辑资料
            case R.id.edit_user:
                setEnabled(true);
                btn_update_ok.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //1 获取信息
                String name = et_user.getText().toString().trim();
                String age = et_age.getText().toString();
                String sex = et_sex.getText().toString().trim();
                String desc = et_desc.getText().toString().trim();
                //2 判断输入框不能为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(sex)) {
                    //3 更新用户
                    MyUser user = new MyUser();
                    user.setUsername(name);
                    user.setAge(Integer.parseInt(age));
                    if (sex.equals(getString(R.string.text_boy))) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc(getString(R.string.text_nothing));
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                UtilTools.showToast(getActivity(), "更新用户信息成功");
                                setEnabled(false);
                                btn_update_ok.setVisibility(View.GONE);
                            } else {
                                UtilTools.showToast(getActivity(), "更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });
                } else {
                    UtilTools.showNotNullToast(getActivity());
                }
                break;
            case R.id.profile_image:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(), CourierActivity.class));
                break;
            case R.id.tv_phone:
                startActivity(new Intent(getActivity(), PhoneActivity.class));
                break;

        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    public static final int CAMERA_REQUEST_CODE = 100;
    public static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;
    private File tempFile = null;

    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用，可用的话就进行储存
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                    break;
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }

    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    //设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            profile_image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        UtilTools.putImageToShare(getActivity(), profile_image);
    }
}
