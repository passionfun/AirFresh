package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.ChatMsg;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:意见反馈的item容器
 * Date:2016/4/12 10:33
 */
public class SuggestAdapter extends ArrayAdapter<ChatMsg>
{
    private int resourceID;
    /**
     * 构造方法名称：	SuggestAdapter
     * 创建时间：		2015-3-3上午9:21:19
     * 方法描述：		TODO
     */
    public SuggestAdapter(Context context, int textViewResourceId,
                          List<ChatMsg> objects)
    {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }
    private class ViewHolder
    {
        ImageView ivServer;
        ImageView ivUser;
        TextView tvMessage;
    }
    /* (非Javadoc)
     * 方法名称：	getView
     * 方法描述：	TODO
     * 重写部分：	@see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ChatMsg chatMsg = getItem(position);
        View view;
        ViewHolder viewHolder;
        if ( convertView == null )
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.ivServer = (ImageView)view.findViewById(R.id.iv_server);
            viewHolder.ivUser = (ImageView)view.findViewById(R.id.iv_user);
            viewHolder.tvMessage = (TextView)view.findViewById(R.id.tv_message);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.tvMessage.setText(chatMsg.getMsg());
        if ( chatMsg.getWho() == 0 )
        {
            viewHolder.ivUser.setVisibility(View.INVISIBLE);
            viewHolder.ivServer.setVisibility(View.VISIBLE);
            viewHolder.tvMessage.setBackgroundResource(R.drawable.chat_box_left);
        }
        else
        {
            viewHolder.ivServer.setVisibility(View.INVISIBLE);
            viewHolder.ivUser.setVisibility(View.VISIBLE);
            viewHolder.tvMessage.setBackgroundResource(R.drawable.chatto_bg_normal);
        }
        return view;
    }

}
