package com.rc.darren.smartbutler.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.adapter.BulterAdapter;
import com.rc.darren.smartbutler.entity.BulterData;
import com.rc.darren.smartbutler.utils.ShareUtils;
import com.rc.darren.smartbutler.utils.StaticClass;
import com.rc.darren.smartbutler.utils.UtilTools;

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
 * 描述：      服务管家
 */

public class ButlerFragment extends Fragment implements View.OnClickListener {

    private ListView mListView;
    private BulterAdapter mAdapter;
    private List<BulterData> mList = new ArrayList<>();

    private Button btn_send;
    private EditText et_text;

    //TTS
    private SpeechSynthesizer mTts;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), null);
        //2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        //保存在SD卡需要在AndroidManifest.xml添加写SD卡权限
        //如果不需要保存合成音频，注释该行代码
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");

        btn_send = (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        et_text = (EditText) view.findViewById(R.id.et_text);
        mListView = (ListView) view.findViewById(R.id.mChatListView);

        mAdapter = new BulterAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        addLeftItem(getString(R.string.text_hello_tts));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                /**
                 * 1 获取输入框信息
                 * 2 判断是否为空
                 * 3 是否输入超出范围
                 * 4 清空输入框内容
                 * 5 添加你的内容到listview
                 * 6 发送给服务器
                 * 7 将返回数据输出到listview中
                 */
                String text = et_text.getText().toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    if (text.length() > 30) {
                        UtilTools.showToast(getActivity(), R.string.text_more_length);
                    } else {
                        et_text.setText("");
                        addRightItem(text);
                        String url = "http://op.juhe.cn/robot/index?info=" + text + "&key=" + StaticClass.CHAR_LIST_KEY;
                        RxVolley.get(url, new HttpCallback() {
                            @Override
                            public void onSuccess(String t) {
                                parsingJson(t);
                            }
                        });
                    }
                } else {
                    UtilTools.showNotNullToast(getActivity());
                }
                break;
        }
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonObject1 = jsonObject.getJSONObject("result");
            String text = jsonObject1.getString("text");
            addLeftItem(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addLeftItem(String text) {
        boolean isChecked = ShareUtils.getBoolean(getActivity(), "isSpeak", false);
        if(isChecked){
            startSpeak(text);
        }

        BulterData data = new BulterData();
        data.setType(BulterAdapter.VALUE_LEFT_TEXT);
        data.setText(text);
        mList.add(data);
        //刷新数据
        mAdapter.notifyDataSetChanged();
        //滑到底部
        mListView.setSelection(mListView.getBottom());
    }

    private void addRightItem(String text) {
        BulterData data = new BulterData();
        data.setType(BulterAdapter.VALUE_RIGHT_TEXT);
        data.setText(text);
        mList.add(data);
        //刷新数据
        mAdapter.notifyDataSetChanged();
        //滑到底部
        mListView.setSelection(mListView.getBottom());
    }

    private void startSpeak(String text) {
        mTts.startSpeaking(text, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
