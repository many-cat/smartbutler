package com.rc.darren.smartbutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.rc.darren.smartbutler.R;

import java.io.File;

public class UpdatActivity extends AppCompatActivity {

    private TextView tv_size;
    private NumberProgressBar bnp;
    private String path;

    private final int HANDLER_LOADING=1;
    private final int HANDLER_OK=2;
    private final int HANDLER_NO=3;


    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_LOADING:
                    Bundle data = msg.getData();
                    long transferredBytes =  data.getLong("transferredBytes");
                    long totalSize = data.getLong("totalSize");
                    tv_size.setText(transferredBytes+"/"+totalSize);
                    bnp.setProgress((int)(((float)(transferredBytes)/(float)(totalSize))*100));
                    break;
                case HANDLER_OK:
                    tv_size.setText(R.string.text_Ddwnload_successful);
                    startInstallApk();
                break;
                case HANDLER_NO:
                    tv_size.setText(R.string.text_dwnload_failure);
                break;
            }
        }
    };

    private void startInstallApk() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updat);

        initView();
    }

    private void initView() {
        bnp = (NumberProgressBar)findViewById(R.id.number_progress_bar);
        tv_size= (TextView) findViewById(R.id.tv_size);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");

        path = FileUtils.getSDCardPath()+"/darren.apk";
        RxVolley.download(path, url ,
                new ProgressListener() {
                    @Override
                    public void onProgress(long transferredBytes, long totalSize) {
                        Message message=new Message();
                        Bundle bundle=new Bundle();
                        bundle.putLong("transferredBytes",transferredBytes);
                        bundle.putLong("totalSize",totalSize);
                        message.what=HANDLER_LOADING;
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                }, new HttpCallback(){
                    @Override
                    public void onSuccess(String t) {
                        mHandler.sendEmptyMessage(HANDLER_OK);
                    }

                    @Override
                    public void onFailure(VolleyError error) {
                        mHandler.sendEmptyMessage(HANDLER_NO);
                    }
                });
    }
}
