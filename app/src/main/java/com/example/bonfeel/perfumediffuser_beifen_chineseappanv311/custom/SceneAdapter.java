package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;

import java.util.List;

/**
 *
 * Author:FunFun
 * Function:情景列表的item容器
 * Date:2016/4/12 10:32
 */
public class SceneAdapter extends ArrayAdapter<Scene> {
    private int resourceID;
    private Scene scene;

    /**
     * 构造方法名称：	SceneAdapter
     * 创建时间：		2015-1-26下午12:00:12
     */
    public SceneAdapter(Context context, int textViewResourceId,
                        List<Scene> objects) {
        super(context, textViewResourceId, objects);
        resourceID = textViewResourceId;
    }

    private class ViewHolder {
        ImageView ivDeleteIcon;
        ImageView ivSceneIcon;
        TextView tvSceneName;
        TextView tvSceneTitle;
        LinearLayout layoutTitle;
    }

    /*
     * 方法名称：	getView
     * 方法描述：	TODOw.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        scene = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, null);
            viewHolder = new ViewHolder();
            viewHolder.layoutTitle = (LinearLayout) view
                    .findViewById(R.id.layout_scene_item_title);
            viewHolder.ivDeleteIcon = (ImageView) view
                    .findViewById(R.id.iv_delete_icon);
            viewHolder.tvSceneTitle = (TextView) view
                    .findViewById(R.id.tv_scene_title);
            viewHolder.tvSceneName = (TextView) view
                    .findViewById(R.id.tv_scene_name);
            viewHolder.ivSceneIcon = (ImageView) view
                    .findViewById(R.id.iv_scene_icon);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (scene.getId().equals("0")) {
            viewHolder.tvSceneTitle.setText("新增场景");
            viewHolder.tvSceneTitle.setGravity(Gravity.CENTER);
        } else {
            viewHolder.tvSceneTitle.setText("场景" + (position + 1));
            viewHolder.tvSceneTitle.setGravity(Gravity.RIGHT);
        }
        if (scene.isSelect()) {
            viewHolder.layoutTitle
                    .setBackgroundResource(R.drawable.scene_item_title_bg_select);
        } else {
            viewHolder.layoutTitle
                    .setBackgroundResource(R.drawable.scene_item_title_bg);
        }
        viewHolder.tvSceneName.setText(scene.getName());
        // 加载场景头像
        viewHolder.ivSceneIcon.setImageBitmap(Helper.getRoundBitmap(scene
                .getIcon()));
        // 根据操作判断是否显示删除场景图标
        if (scene.isDelete()) {
            viewHolder.ivDeleteIcon.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivDeleteIcon.setVisibility(View.INVISIBLE);
        }
        if (scene.getId().equals("0")) {
            viewHolder.ivDeleteIcon.setVisibility(View.GONE);
        }
        return view;
    }
}
