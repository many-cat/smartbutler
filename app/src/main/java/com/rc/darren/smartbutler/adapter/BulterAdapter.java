package com.rc.darren.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.BulterData;

import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.adapter
 * 文件名：    BulterAdapter
 * 创建者：    Darren
 * 创建时间：  2017/3/1 14:25
 * 描述：      适配器
 */

public class BulterAdapter extends BaseAdapter {

    public static final int VALUE_LEFT_TEXT = 1;
    public static final int VALUE_RIGHT_TEXT = 2;

    private Context mContext;
    private List<BulterData> mList;
    private LayoutInflater mLayoutInflater;

    public BulterAdapter(Context context, List<BulterData> list) {
        mContext = context;
        mList = list;
        //获取系统服务
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = new ViewHolderLeft();
                    convertView = mLayoutInflater.inflate(R.layout.left_item, null);
                    viewHolderLeft.tv_left_text = (TextView) convertView.findViewById(R.id.tv_left_text);
                    convertView.setTag(viewHolderLeft);
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = new ViewHolderRight();
                    convertView = mLayoutInflater.inflate(R.layout.right_item, null);
                    viewHolderRight.tv_right_text = (TextView) convertView.findViewById(R.id.tv_right_text);
                    convertView.setTag(viewHolderRight);
                    break;
            }
        } else {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }

        BulterData data = mList.get(position);
        switch (type) {
            case VALUE_LEFT_TEXT:
                viewHolderLeft.tv_left_text.setText(data.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRight.tv_right_text.setText(data.getText());
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        BulterData data = mList.get(position);
        int type = data.getType();
        return type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;//
    }

    class ViewHolderLeft {
        private TextView tv_left_text;
    }

    class ViewHolderRight {
        private TextView tv_right_text;
    }
}
