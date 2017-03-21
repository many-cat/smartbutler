package com.rc.darren.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.GrilData;
import com.rc.darren.smartbutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.adapter
 * 文件名：    GrilAdapter
 * 创建者：    Darren
 * 创建时间：  2017/3/4 19:26
 * 描述：      TODO
 */

public class GrilAdapter extends BaseAdapter {

    private Context mContext;
    private List<GrilData> mList;
    private LayoutInflater mInflater;
    private GrilData mData;

    private WindowManager mManager;
    private int width;

    public GrilAdapter(Context mContext, List<GrilData> mList) {
        this.mContext = mContext;
        this.mList = mList;

        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = mManager.getDefaultDisplay().getWidth();
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
            convertView = mInflater.inflate(R.layout.gril_item, null);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.gril_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        GrilData data = mList.get(position);
        PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), width / 2, 300, viewHolder.imageView);

        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
    }
}
