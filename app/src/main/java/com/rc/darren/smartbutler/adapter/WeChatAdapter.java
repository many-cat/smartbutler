package com.rc.darren.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.WeChatData;
import com.rc.darren.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.adapter
 * 文件名：    WeChatAdapter
 * 创建者：    Darren
 * 创建时间：  2017/3/4 13:27
 * 描述：      TODO
 */

public class WeChatAdapter extends BaseAdapter {

    private Context mContext;
    private List<WeChatData> mList;
    private WeChatData mData;
    private LayoutInflater mInflater;

    private String url;

    public WeChatAdapter(Context mContext, List<WeChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //获取系统服务
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.wechat_item, null);
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        mData = mList.get(position);
        viewHolder.tv_source.setText(mData.getSource());
        viewHolder.tv_title.setText(mData.getTitle());
        if (mData.getImageUrl().trim().length() != 0) {
            url = mData.getImageUrl();
        }
        PicassoUtils.loadImageViewSize(mContext, url, 400, 250, viewHolder.iv_img);
        return convertView;
    }

    class ViewHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;
    }
}
