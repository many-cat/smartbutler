package com.rc.darren.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.adapter.GrilAdapter;
import com.rc.darren.smartbutler.entity.GrilData;
import com.rc.darren.smartbutler.utils.PicassoUtils;
import com.rc.darren.smartbutler.utils.StaticClass;
import com.rc.darren.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.fragment
 * 文件名：    ButlerFragment
 * 创建者：    Darren
 * 创建时间：  2017/2/20 15:44
 * 描述：      TODO
 */

public class GrilFragment extends Fragment {

    private GridView mGridView;
    private GrilData mData;
    private GrilAdapter mAdapter;
    private List<GrilData> mList = new ArrayList<>();

    private CustomDialog dialog;
    private List<String> mImgUrls=new ArrayList<>();
    private ImageView photoImg;
    private PhotoViewAttacher mAttacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        //初始化提示框
        dialog = new CustomDialog(getActivity(), LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, R.layout.dialog_item,
                R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        photoImg = (ImageView) dialog.findViewById(R.id.iv_img);

        RxVolley.get(StaticClass.GRIL_URL, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PicassoUtils.loadImaheView(getActivity(),mImgUrls.get(position),photoImg);
                mAttacher=new PhotoViewAttacher(photoImg);
                mAttacher.update();
                dialog.show();
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray array = jsonObject.getJSONArray("results");
            for (int i = 0; i < array.length(); i++) {
                JSONObject json = (JSONObject) array.get(i);
                mData=new GrilData();
                mImgUrls.add(json.getString("url"));
                mData.setImgUrl(json.getString("url"));
                mList.add(mData);
                mAdapter = new GrilAdapter(getActivity(), mList);
                mGridView.setAdapter(mAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
