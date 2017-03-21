package com.rc.darren.smartbutler.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.adapter.CourierAdapter;
import com.rc.darren.smartbutler.entity.CourierData;
import com.rc.darren.smartbutler.utils.L;
import com.rc.darren.smartbutler.utils.StaticClass;
import com.rc.darren.smartbutler.utils.UtilTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CourierActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_name;
    private EditText et_number;
    private Button btn_get_courer;
    private ListView mListView;

    private List<CourierData> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);

        initView();
    }

    private void initView() {
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_get_courer = (Button) findViewById(R.id.btn_get_courier);
        btn_get_courer.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.mListView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_courier:
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求数据（Json）
                 * 4.解析Json
                 * 5.listview适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */
                String name = et_name.getText().toString().trim();
                String number = et_number.getText().toString().trim();
                //字符串拼接
                String url = "http://v.juhe.cn/exp/index?key=+" + StaticClass.JUHE_APP_KEY + "&com=" + name + "&no=" + number;

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)) {
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
//                            UtilTools.showToast(CourierActivity.this,t);
                            L.i(t);
                            pasingJson(t);
                        }
                    });

                } else {
                    UtilTools.showNotNullToast(this);
                }

                break;
        }
    }

    private void pasingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);

                CourierData data = new CourierData();
                data.setDatetime(json.getString("datetime"));
                data.setRemark(json.getString("remark"));
                data.setZone(json.getString("zone"));
                mList.add(data);
            }
            CourierAdapter adapter = new CourierAdapter(this, mList);
            mListView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
