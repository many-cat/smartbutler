package com.rc.darren.smartbutler.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rc.darren.smartbutler.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.utils
 * 文件名：    UtilTools
 * 创建者：    Darren
 * 创建时间：  2017/2/20 15:16
 * 描述：      工具统一类
 */

public class UtilTools {

    public static void setFont(Context context, TextView textView) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FONT.TTF");
        textView.setTypeface(typeface);
    }

    public static void putImageToShare(Context context, ImageView imageView) {
        //保存
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步 ：将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        //第二步： 利用Base64将我们的字节数组输出流转化成String
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步： 将String保存shareUtils
        ShareUtils.putString(context, "image_title", imgString);
    }

    public static void getImageToShare(Context context, ImageView imageView) {
        //1.拿到string
        String imgString = ShareUtils.getString(context, "image_title", "");
        if (!imgString.equals("")) {
            //2.利用Base64将我们string转换
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
            //3.生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byteStream);
            imageView.setImageBitmap(bitmap);
        }
    }


    public static void showToast(Context context, int text) {
        Toast.makeText(context, context.getString(text), Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showNotNullToast(Context context) {
        Toast.makeText(context, context.getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
    }
}
