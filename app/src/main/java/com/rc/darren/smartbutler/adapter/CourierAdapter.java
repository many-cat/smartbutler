package com.rc.darren.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rc.darren.smartbutler.R;
import com.rc.darren.smartbutler.entity.CourierData;

import java.util.List;

/**
 * 项目名：    SmartButler
 * 包名：      com.rc.darren.smartbutler.adapter
 * 文件名：    CourierAdapter
 * 创建者：    Darren
 * 创建时间：  2017/2/28 13:51
 * 描述：      TODO
 */

public class CourierAdapter extends BaseAdapter{

    private Context mContext;
    private List<CourierData> mList;
    //布局加载器
    private LayoutInflater mLayoutInflater;
    private CourierData data;


    public CourierAdapter(Context context,List<CourierData> list){
        mContext=context;
        mList=list;
        //获取系统服务
        mLayoutInflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        ViewHolder viewHolder=null;
        //是否第一次运行
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mLayoutInflater.inflate(R.layout.layout_courier_item,null);
            viewHolder.tv_remark= (TextView) convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone= (TextView) convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime= (TextView) convertView.findViewById(R.id.tv_datetime);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        data=mList.get(position);

        viewHolder.tv_remark.setText(data.getRemark());
        viewHolder.tv_zone.setText(data.getZone());
        viewHolder.tv_datetime.setText(data.getDatetime());

        return convertView;
    }
    class ViewHolder{
        private TextView tv_remark;
        private TextView tv_zone;
        private TextView tv_datetime;
    }

}
