package com.rc.darren.smartbutler.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.adapter.WeChatAdapter;
import com.rc.darren.smartbutler.entity.WeChatData;
import com.rc.darren.smartbutler.ui.WebViewActivity;
import com.rc.darren.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.fragment
 * 文件名：    ButlerFragment
 * 创建者：    Darren
 * 创建时间：  2017/2/20 15:44
 * 描述：      微信精选
 */

public class WeChatFragment extends Fragment {

    private ListView mListView;
    private WeChatAdapter mAdapter;
    private List<WeChatData> mList = new ArrayList<>();

    private List<String> itemTitles = new ArrayList<>();
    private List<String> itemUrls = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mListView = (ListView) view.findViewById(R.id.mListView);


        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_KEY + "&ps=100";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                //intent传值两种方式：
//                Bundle bundle=new Bundle();
//                bundle.putString("key","value");
//                intent.putExtras(bundle);
//                intent.putExtra("key","value");
                intent.putExtra("title", itemTitles.get(position));
                intent.putExtra("url", itemUrls.get(position));
                startActivity(intent);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray list = result.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {
                JSONObject json = (JSONObject) list.get(i);
                WeChatData data = new WeChatData();

                String title = json.getString("title");
                data.setTitle(title);
                data.setSource(json.getString("source"));
                data.setImageUrl(json.getString("firstImg"));
                mList.add(data);
                itemTitles.add(title);
                itemUrls.add(json.getString("url"));
            }
            mAdapter = new WeChatAdapter(getActivity(), mList);
            mListView.setAdapter(mAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
