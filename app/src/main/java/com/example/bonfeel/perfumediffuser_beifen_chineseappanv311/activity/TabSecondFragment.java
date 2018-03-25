package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.SceneAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.SwitchButton;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Scene;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.GetSceneIconTask;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.SceneManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ToastUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 13:49
 */
public class TabSecondFragment extends Fragment {
    private String tag = "场景界面";
    private View tabLayout;
    private GridView gvSceneList;
    private ImageView ivDelete;

    private List<Scene> scenes;
    private Scene scene;
    private SceneAdapter adapter;
    private boolean isPower;
    private boolean isDelete;

    private static Bitmap defaultIcon;

    private final int ADD_SCENE_SUCCESS = 1;
    private final int ADD_SCENE_FAIL = 2;
    private final int ADD_SCENE_ERROR = 3;
    private final int DEL_SCENE_SUCCESS = 4;
    private final int DEL_SCENE_FAIL = 5;
    private final int DEL_SCENE_ERROR = 6;
    private final int EXCE_SCENE_SUCCESS = 7;
    private final int EXCE_SCENE_FAIL = 8;
    private final int EXCE_SCENE_ERROR = 9;
    private final int GET_SCENE_SUCCESS = 10;
    private final int GET_SCENE_FAIL = 11;
    private final int GET_SCENE_ERROR = 12;
    private final int GET_SCENE_ICON_SUCCESS = 13;
    private final int CONTROL_ALL_SUCCESS = 14;
    private final int CONTROL_ALL_FAIL = 15;
    private final int CONTROL_ALL_ERROR = 16;
    private final int GET_DETAIL_SUCCESS = 17;
    private final int GET_DETAIL_FAIL = 18;
    private final int GET_DETAIL_ERROR = 19;

    private Dialog dialog;

    private CustomDialog configDialog;
    private View layoutHint;
    private TextView tvHintContent;
    private int configIndex;

    private CustomDialog execDialog;
    private View layoutTip;
    private TextView tvTipContent;

    private SwitchButton sbEasyControl;
    private int reControlTime;

    private long waitTime;

    /*
     * 方法名称： onCreate 方法描述： TODO
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialData();
    }

    /**
     * 方法名称： initialData 方法描述： 初始化数据 参数： 返回值类型： void 创建时间： 2015-1-26下午12:09:52
     */
    private void initialData() {
        if (UserManager.getInstance().get(0) == null) {
            return;
        }
        dialogShow("正在加载...");
        // 获取场景列表
        getScenes();

        defaultIcon = ((BitmapDrawable) getResources().getDrawable(
                R.drawable.scene_default)).getBitmap();
        scenes = new ArrayList<Scene>();
        isPower = false;
        isDelete = false;
        reControlTime = 0;
        // waitTime = DeviceManager.getInstance().getOnlineCount() * 100 + 1000;
        waitTime = 2000;
    }

    /*
     * 方法名称： onCreateView 方法描述：
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabLayout = inflater.inflate(R.layout.fragment_tab2, container, false);
        initialView(tabLayout);
        initialHandler();
        return tabLayout;
    }

    /**
     * 方法名称： initialView 方法描述： 初始化控件 参数： @param v 返回值类型： void 创建时间：
     * 2015-1-26下午1:22:49
     */
    private void initialView(View v) {
        gvSceneList = (GridView) v.findViewById(R.id.gv_scenes);
        ivDelete = (ImageView) v.findViewById(R.id.iv_delete_scene);
        sbEasyControl = (SwitchButton) v.findViewById(R.id.sb_easy_control);
        sbEasyControl.setImageResource(R.drawable.switch_buttom);
    }

    /**
     * 方法名称： initialHandler 方法描述： TODO 参数： 返回值类型： void 创建时间： 2015-1-26下午1:46:43
     */
    private void initialHandler() {
        // 一键控制
        sbEasyControl.setOnChangeListener(new SwitchButton.OnChangeListener() {
            @Override
            public void OnChanged(boolean state) {
                if (DeviceManager.getInstance().getCount() == 0) {
                    Toast.makeText(ApplicationUtil.getContext(),
                            "设备列表中没有设备，请添加设备!", Toast.LENGTH_LONG).show();
                    return;
                }
                oneShoot(state);
            }
        });
        // 情景配置
        gvSceneList.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                if (!isDelete && (position != (scenes.size() - 1))) {
                    ApplicationUtil.vibrate(getActivity(), 30);
                    configIndex = position;
                    showCofigDialog();
                }
                return true;
            }
        });
        // 场景的启动和删除操作
        gvSceneList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                scene = scenes.get(position);
                if (isDelete) {
                    if (position != (scenes.size() - 1)) {
                        dialogShow("正在删除...");
						/*
						 * 调用I_DelScene(String sceneid) 返回ResCode:0/1
						 */
                        deleteScene();
                    }
                } else {
                    if (position == (scenes.size() - 1)) {
                        // 新增场景
                        dialogShow("正在加载...");
                        if (checkSize()) {
                            createNewScene();
                        }
                    } else {
                        // 弹出提示框,提示是否需要执行场景
                        showExecDialog();
                    }
                }
            }
        });
        // 删除场景图标
        ivDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeMode();
            }
        });
    }
    /**
     * 点击创建场景，跳转到场景属性界面
     *
     */
    protected void toConfigActivity() {
        if (!isDelete ) {
            Intent intent = new Intent(getActivity(),
                    SceneConfigActivity.class);
            intent.putExtra("id", scenes.get(scenes.size()-1).getId());
            Log.i("sizescene", "scenesize:" + scenes.size());
            startActivity(intent);
        }
    }

    /**
     * 方法名称： showExecDialog 方法描述： 执行场景的提示框 参数： 返回值类型： void 创建时间：
     * 2015-5-5下午2:41:19
     */
    private void showExecDialog() {
        layoutTip = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_hint_control, null);
        tvTipContent = (TextView) layoutTip.findViewById(R.id.tv_hint_control);
        tvTipContent.setText("确认执行该场景吗?");

        execDialog = new CustomDialog(getActivity()).builder().setTitle("提示")
                .setView(layoutTip)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogShow("正在执行...");
                        execScene();
                    }
                });
        execDialog.show();
    }

    /**
     * 方法名称： checkSize 方法描述： 判断场景个数是否为max_size 参数： @return 返回值类型： boolean 创建时间：
     * 2015-4-11上午9:25:24
     */
    private boolean checkSize() {
        // 首先检查场景的个数是否超过max
        // 再检查是否有设备可以配置，若无则无法进行场景添加
        if ((scenes.size() - 1) < NetElectricsConst.MAX_SCENE_COUNT) {
            if (DeviceManager.getInstance().getCount() == 0) {
                CustomToast("请先添加至少一个设备");
                dialog.dismiss();
                return false;
            }
            return true;
        } else {
            CustomToast("场景最多为" + NetElectricsConst.MAX_SCENE_COUNT + "个");
            dialog.dismiss();
            return false;
        }
    }

    /**
     * 方法名称： showCofigDialog 方法描述： 弹出进入配置提示框 参数： 返回值类型： void 创建时间：
     * 2015-4-13上午9:09:16
     */
    private void showCofigDialog() {
        layoutHint = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_hint_control, null);
        tvHintContent = (TextView) layoutHint
                .findViewById(R.id.tv_hint_control);
        tvHintContent.setText("确认进入场景设置?");

        configDialog = new CustomDialog(getActivity()).builder().setTitle("提示")
                .setView(layoutHint)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(),
                                SceneConfigActivity.class);
                        intent.putExtra("id", scenes.get(configIndex).getId());
                        //Toast.makeText(getActivity(), "pos+id:"+configIndex+","+scenes.get(configIndex).getId(), Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                });
        configDialog.show();
    }

    /**
     * 方法名称： createNewScene 方法描述： 创建新场景 参数： 返回值类型： void 创建时间： 2015-4-11上午9:24:44
     */
    private void createNewScene() {
        // 新增一个场景，初值为默认
        scene = new Scene();
        scene.setName("场景" + (scenes.size()));
        scene.setIcon(defaultIcon);
		/*
		 * 调用I_AddScene(String mobileid, String name, byte[] image, String
		 * devicelistid) 返回ResCode:0/scene_id
		 */
        addScene();
//		if (!isDelete ) {
//			Intent intent = new Intent(getActivity(),
//					SceneConfigActivity.class);
//			intent.putExtra("id", scenes.get(scenes.size()-1).getId());
//			Log.i("sizescene", "scenesize:"+scenes.size());
//			startActivity(intent);
//		}

    }

    /**
     * 方法名称： changeMode 方法描述： 切换到删除场景模式 参数： 返回值类型： void 创建时间： 2015-4-11上午9:30:04
     */
    private void changeMode() {
        //fun add
        if(scenes.size() == 0){
            //Toast.makeText(ApplicationUtil.getContext(), "场景列表为空，进入删除模式失败!", Toast.LENGTH_LONG).show();
            ToastUtil.showToast(getActivity(), "场景列表为空，进入删除模式失败！");
            return;
        }
        if (isDelete) {
            ivDelete.setImageResource(R.drawable.delete);
            isDelete = false;
        } else {
            ivDelete.setImageResource(R.drawable.finish_delete);
            isDelete = true;
        }
        for (int i = 0; i < scenes.size() - 1; i++) {
            scenes.get(i).setDelete(isDelete);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 方法名称： oneShoot 方法描述： 一键开关机 参数： 返回值类型： void 创建时间： 2015-3-17上午10:34:02
     */
    private void oneShoot(boolean state) {
        dialogShow("正在执行...");
        isPower = state;
        NetCommunicationUtil.setSingle(false);
        controlAll();
    }

    /**
     * 方法名称： controlAll 方法描述： 控制所有设备的开关机 参数： 返回值类型： void 创建时间：
     * 2015-4-22下午8:11:13
     */
    private void controlAll() {
        NetCommunicationUtil.httpConnect(getControlInfos(),
                NetElectricsConst.METHOD_EASY_CONTROL,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            Log.i("controll", bundle.getString("result"));
                            message.what = CONTROL_ALL_SUCCESS;
                        } else {
                            message.what = CONTROL_ALL_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        LogUtil.logDebug("一键控制", bundle.getString("message"));
                        Log.i("controllerror", bundle.getString("message"));
                        Message message = handler.obtainMessage();
                        message.what = CONTROL_ALL_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    private List<PropertyInfo> getControlInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userInfo = new PropertyInfo();
        userInfo.setName(NetElectricsConst.USER_NAME);
        userInfo.setValue(((User) (UserManager.getInstance().get(0)))
                .getPhone());

        PropertyInfo state = new PropertyInfo();
        state.setName(NetElectricsConst.STATE);
        state.setValue(isPower ? "1" : "0");

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(userInfo);
        infos.add(state);
        infos.add(token);

        return infos;
    }

    /**
     * 方法名称： addScene 方法描述： 添加场景 参数： 返回值类型： void 创建时间： 2015-3-24下午7:57:25
     */
    private void addScene() {
        NetCommunicationUtil.httpConnect(getAddSceneInfos(),
                NetElectricsConst.METHOD_ADD_SCENE,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = ADD_SCENE_SUCCESS;
                            message.arg1 = getSceneId(bundle);
                        } else {
                            message.what = ADD_SCENE_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = ADD_SCENE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称： getSceneId 方法描述： 获取新增场景的编号 参数： @return 返回值类型： int 创建时间：
     * 2015-4-11上午9:14:22
     */
    private int getSceneId(Bundle bundle) {
        int index = 0;
        JSONTokener jsonTokener = new JSONTokener(bundle.getString("result"));
        try {
            JSONObject jsonObject = (JSONObject) jsonTokener.nextValue();
            index = Integer.parseInt(jsonObject.getString("ResCode"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return index;
    }

    /**
     * 方法名称： getAddSceneInfos 方法描述： 添加设备的参数集 参数： @param scene 参数： @return 返回值类型：
     * List<PropertyInfo> 创建时间： 2015-3-25上午9:57:15
     */
    private List<PropertyInfo> getAddSceneInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userInfo = new PropertyInfo();
        userInfo.setName(NetElectricsConst.USER_NAME);
        userInfo.setValue(((User) (UserManager.getInstance().get(0)))
                .getPhone());

        PropertyInfo name = new PropertyInfo();
        name.setName(NetElectricsConst.SCENE_NAME);
        name.setValue(scene.getName());

        PropertyInfo image = new PropertyInfo();
        image.setName(NetElectricsConst.SCENE_IMAGE);
        image.setValue(Helper.bitmap2String(scene.getIcon()));

        PropertyInfo listid = new PropertyInfo();
        listid.setName(NetElectricsConst.DEVICE_LIST);
        listid.setValue(getDeviceList());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(userInfo);
        infos.add(name);
        infos.add(image);
        infos.add(listid);
        infos.add(token);

        return infos;
    }

    /**
     * 方法名称： getDeviceList 方法描述： 设备编号列表 参数： @return 返回值类型： String 创建时间：
     * 2015-4-6下午5:09:54
     */
    private String getDeviceList() {
        String list = "";
        for (int i = 0; i < DeviceManager.getInstance().getCount(); i++) {
            list = list + ((Device) DeviceManager.getInstance().get(i)).getId();
            if (i < DeviceManager.getInstance().getCount() - 1) {
                list = list + ",";
            }
        }
        return list;
    }

    /**
     * 方法名称： getScenes 方法描述： 获取场景列表 参数： 返回值类型： void 创建时间： 2015-3-25上午10:25:06
     */
    private void getScenes() {
        NetCommunicationUtil.httpConnect(getSceneInfos(),
                NetElectricsConst.METHOD_GET_SCENE,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            NetCommunicationUtil.getHttpResultInfo(
                                    NetElectricsConst.METHOD_TAG.GetScene,
                                    bundle.getString("result"));
                            message.what = GET_SCENE_SUCCESS;
                        } else {
                            message.what = GET_SCENE_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = GET_SCENE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称： getSceneInfos 方法描述： 获取场景列表的参数集 参数： @return 返回值类型：
     * List<PropertyInfo> 创建时间： 2015-3-25下午2:22:49
     */
    private List<PropertyInfo> getSceneInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userInfo = new PropertyInfo();
        userInfo.setName(NetElectricsConst.USER_NAME);
        userInfo.setValue(((User) (UserManager.getInstance().get(0)))
                .getPhone());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(userInfo);
        infos.add(token);

        return infos;
    }

    /**
     * 方法名称： deleteScene 方法描述： 删除场景 参数： 返回值类型： void 创建时间： 2015-3-25下午2:24:03
     */
    private void deleteScene() {
        NetCommunicationUtil.httpConnect(getSceneIdInfos(),
                NetElectricsConst.METHOD_DEL_SCENE,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = DEL_SCENE_SUCCESS;
                        } else {
                            message.what = DEL_SCENE_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = DEL_SCENE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称： getDeleteInfos 方法描述： 获取删除场景的参数集 参数： @return 返回值类型：
     * List<PropertyInfo> 创建时间： 2015-3-25下午2:23:43
     */
    private List<PropertyInfo> getSceneIdInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());
        LogUtil.logDebug(tag, "sceneId" + scene.getId());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(token);

        return infos;
    }

    /**
     * 方法名称： execScene 方法描述： 执行场景 参数： 返回值类型： void 创建时间： 2015-3-25下午2:40:27
     */
    private void execScene() {
        NetCommunicationUtil.setSingle(false);
        NetCommunicationUtil.httpConnect(getSceneIdInfos(),
                NetElectricsConst.METHOD_EXEC_SCENE,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            // // fun add
                            // Log.i("执行场景成功：", bundle.getString("result"));
                            message.what = EXCE_SCENE_SUCCESS;
                        } else {
                            message.what = EXCE_SCENE_FAIL;
                            // Log.i("执行场景失败：", bundle.getString("result"));
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        LogUtil.logDebug("开启场景", bundle.getString("message"));
                        Message message = handler.obtainMessage();
                        // // fun add
                        // Log.i("执行场景错误：", bundle.getString("message"));
                        message.what = EXCE_SCENE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ADD_SCENE_SUCCESS:
                    dialog.dismiss();
                    // 添加场景成功
                    scene.setId(msg.arg1 + "");
                    scenes.add(scenes.size() - 1, scene);
                    adapter.notifyDataSetChanged();
                    Intent intent = new Intent(getActivity(),SceneConfigActivity.class);
                    intent.putExtra("id", msg.arg1 +"");
                    startActivity(intent);
                    break;
                case ADD_SCENE_FAIL:
                    dialog.dismiss();
                    break;
                case ADD_SCENE_ERROR:
                    dialog.dismiss();
                    //CustomToast("网络不稳定，添加场景失败！请稍后重试");
                    ToastUtil.showToast(getActivity(), "网络不稳定，添加场景失败！请稍后重试!");
                    break;
                case DEL_SCENE_SUCCESS:
                    dialog.dismiss();
                    scenes.remove(scene);
                    adapter.notifyDataSetChanged();
                    break;
                case DEL_SCENE_FAIL:
                    break;
                case DEL_SCENE_ERROR:
                    dialog.dismiss();
                    //CustomToast("网络不稳定，删除场景失败，请稍后重试");
                    ToastUtil.showToast(getActivity(), "网络不稳定，删除场景失败，请稍后重试!");
                    break;
                case EXCE_SCENE_SUCCESS:
                    dialog.dismiss();
                    sceneDetail();
                    execSceneResult();
                    break;
                case EXCE_SCENE_FAIL:
                    if (reControlTime < 3) {
                        execScene();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        NetCommunicationUtil.setSingle(true);
                        //CustomToast("执行场景失败，请稍后重试");
                        ToastUtil.showToast(getActivity(), "执行场景失败，请稍后重试!");
                        reControlTime = 0;
                    }
                    break;
                case EXCE_SCENE_ERROR:
                    if (reControlTime < 3) {
                        execScene();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        NetCommunicationUtil.setSingle(true);
                        //CustomToast("网络不稳定，执行场景失败，请稍后重试");
                        ToastUtil.showToast(getActivity(), "网络不稳定，执行场景失败，请稍后重试!");
                        reControlTime = 0;
                    }
                    break;
                case GET_SCENE_SUCCESS:
                    scenes = SceneManager.getInstance().getScenes();
                    // 增加添加场景的按钮
                    scenes.add(addSceneView());
                    adapter = new SceneAdapter(getActivity(), R.layout.scene_item,
                            scenes);
                    gvSceneList.setAdapter(adapter);
                    // 开始异步加载场景的头像
                    GetSceneIconTask iconTask = new GetSceneIconTask(handler);
                    iconTask.execute(scenes);
                    break;
                case GET_SCENE_FAIL:
                    getScenes();
                    break;
                case GET_SCENE_ERROR:
                    dialog.dismiss();
                    //CustomToast("网络不稳定，获取场景失败，请稍后重试");
                    ToastUtil.showToast(getActivity(), "网络不稳定，获取场景失败，请稍后重试!");
                    break;
                case CONTROL_ALL_SUCCESS:
                    listenResult();
                    break;
                case CONTROL_ALL_FAIL:
                    if (reControlTime < 2) {
                        NetCommunicationUtil.setSingle(false);
                        controlAll();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        NetCommunicationUtil.setSingle(true);
                        //CustomToast("一键控制失败，请稍后重试");
                        ToastUtil.showToast(getActivity(), "一键控制失败，请稍后重试!");
                        reControlTime = 0;
                    }
                    break;
                case CONTROL_ALL_ERROR:
                    if (reControlTime < 2) {
                        NetCommunicationUtil.setSingle(false);
                        controlAll();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        NetCommunicationUtil.setSingle(true);
                        //CustomToast("网络不稳定，一键控制失败！请稍后重试");
                        ToastUtil.showToast(getActivity(), "网络不稳定，一键控制失败！请稍后重试!");
                        reControlTime = 0;
                    }
                    break;
                case GET_SCENE_ICON_SUCCESS:
                    // 加载场景头像完成，显示场景
                    dialog.dismiss();
                    adapter.notifyDataSetChanged();
                    break;
                case GET_DETAIL_SUCCESS:
                    scene.setDeviceList(NetCommunicationUtil.getSceneDevices());
                    break;
                case GET_DETAIL_FAIL:
                    break;
                case GET_DETAIL_ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    private void execSceneResult() {
        LogUtil.logDebug(tag, "============start to click===========");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetCommunicationUtil.setSingle(true);
                // scene bind device total size
                int total = getSceneTotalDevices();
                LogUtil.logDebug(tag,
                        "=====>>> execscene Total devices size is " + total);
                // online size
                int online = getSceneOnlineDevices();
                LogUtil.logDebug(tag,
                        "=====>>> execscene Online devices size is " + online);
                // execScene success control
                // LogUtil.logDebug(tag, "=====>>>ControlSuc devices size is "+
                // NetCommunicationUtil.getControlSucDevices());
                // compare
                // fun add 用户体验较好，如果没有在线设备的话，提示控制在线设备成功，给人的感觉不是很好
                if (online == 0) {
                    dialog.dismiss();
                    Toast.makeText(ApplicationUtil.getContext(),
                            "请选择该场景下的在线设备!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (NetCommunicationUtil.getControlSucDevices() != online) {
                    if (reControlTime < 2) {
                        NetCommunicationUtil.setSingle(false);
                        execScene();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        showResultDialog("控制成功："
                                + NetCommunicationUtil.getControlSucDevices()
                                + "台，"
                                + "控制失败："
                                + (online - NetCommunicationUtil
                                .getControlSucDevices()) + "台，"
                                + "离线设备：" + (total - online) + "台");
                        reControlTime = 0;
                    }
                } else {
                    dialog.dismiss();
                    // showResultDialog("控制在线设备成功");
                    // fun add
                    if (total - online == 0) {
                        showResultDialog("控制该场景下的在线设备成功!");
                    } else {
                        showResultDialog("控制该场景下的在线设备成功!\n离线设备有"
                                + (total - online) + "台!");
                    }
                    reControlTime = 0;
                }
                NetCommunicationUtil.cleanControlSucDevices();

                for (int i = 0; i < (scenes.size() - 1); i++) {
                    Scene tempScene = scenes.get(i);
                    tempScene.setSelect(false);
                }
                scene.setSelect(true);
                adapter.notifyDataSetChanged();
            }
        }, 3000);
    }

    /**
     * 方法名称： sceneDetail 方法描述： 获取场景属性 参数： 返回值类型： void 创建时间： 2015-3-25下午3:11:45
     */
    private void sceneDetail() {
        NetCommunicationUtil.httpConnect(getSceneDetails(),
                NetElectricsConst.METHOD_SCENE_DETAIL,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            NetCommunicationUtil.getHttpResultInfo(
                                    NetElectricsConst.METHOD_TAG.SceneDetail,
                                    bundle.getString("result"));
                            // fun add
                            LogUtil.logDebug(
                                    tag,
                                    "TabSec scenedetail"
                                            + bundle.getString("result"));
                            // Log.i("sceneDetail", bundle.getString("result"));
                            message.what = GET_DETAIL_SUCCESS;
                        } else {
                            message.what = GET_DETAIL_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        // System.out.println(bundle.getString("message"));
                        // Message message = handler.obtainMessage();
                        // message.what = GET_DETAIL_ERROR;
                        // handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称： getSceneInfos 方法描述： 获取场景ID 参数： @return 返回值类型： List<PropertyInfo>
     * 创建时间： 2015-3-25下午3:12:34
     */
    private List<PropertyInfo> getSceneDetails() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo sceneId = new PropertyInfo();
        sceneId.setName(NetElectricsConst.SCENE_ID);
        sceneId.setValue(scene.getId());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(sceneId);
        infos.add(token);

        return infos;
    }

    private int getSceneTotalDevices() {
        return scene.getDeviceList().size();
    }

    private int getSceneOnlineDevices() {
        int online = 0;
        for (int i = 0; i < scene.getDeviceList().size(); i++) {
            // get mac
            String mac = scene.getDeviceList().get(i).getMac();
            // get state
            if (!DeviceManager.getInstance().get(mac).isLeave()) {
                // fun add
                Log.i("getSceneOnlineDevices",
                        DeviceManager.getInstance().get(mac).getMac());
                online++;
            }
        }
        return online;
    }

    /**
     * 方法名称： listenResult 方法描述： 监听群控结果 参数： 返回值类型： void 创建时间： 2015-5-23上午8:13:27
     */
    private void listenResult() {
        LogUtil.logDebug(tag, "============start to click===========");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetCommunicationUtil.setSingle(true);
                // update state
                MainActivity mainActivity = (MainActivity) TabSecondFragment.this
                        .getActivity();
                mainActivity.getSlideMenuFragment().updateDeviceList();

                // fun add test
                for (int i = 0; i < DeviceManager.getInstance().getDevices()
                        .size(); i++) {
                    Log.i("listenresult", DeviceManager.getInstance()
                            .getDevices().get(i).isLeave()
                            + "");
                }

                int size = DeviceManager.getInstance().getOnlineCount();

                LogUtil.logDebug(tag, "=====>>>Online devices size is " + size);
                int total = DeviceManager.getInstance().getCount();
                Log.i("totaldevice", "设备总数量:"+total);
                LogUtil.logDebug(tag, "=====>>> oneshot Total devices size is "
                        + total);
                // LogUtil.logDebug(tag, "=====>>>ControlSuc devices size is "+
                // NetCommunicationUtil.getControlSucSize());
                LogUtil.logDebug(tag, "=====>>>ControlSuc devices size is "
                        + NetCommunicationUtil.getControlSucDevices());

                // compare
                if (NetCommunicationUtil.getControlSucDevices() != size) {
                    if (reControlTime < 2) {
                        NetCommunicationUtil.setSingle(false);
                        controlAll();
                        reControlTime++;
                    } else {
                        dialog.dismiss();
                        showResultDialog("控制成功："
                                + NetCommunicationUtil.getControlSucDevices()
                                + "台，"
                                + "控制失败："
                                + (size - NetCommunicationUtil
                                .getControlSucDevices()) + "台，"
                                + "离线设备：" + (total - size) + "台");
                        reControlTime = 0;
                    }
                } else {
                    dialog.dismiss();
                    // showResultDialog("控制在线设备成功");
                    // fun add 当设备列表中无在线设备的时候，一键控制提示执行失败20150925
                    if (size == 0) {
                        showResultDialog("设备列表中无在线设备,\n执行失败!");
                        return;
                    }
                    // fun add
                    if (total - size == 0) {
                        showResultDialog("控制在线设备成功!");
                    } else {
                        showResultDialog("控制在线设备成功!\n离线设备有" + (total - size)
                                + "台!");

                    }
                    reControlTime = 0;
                }
                NetCommunicationUtil.cleanControlSucDevices();
            }
        }, waitTime);
    }

    private void showResultDialog(String result) {
        layoutHint = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_hint_control, null);
        tvHintContent = (TextView) layoutHint
                .findViewById(R.id.tv_hint_control);
        tvHintContent.setText(result);

        configDialog = new CustomDialog(getActivity()).builder()
                .setTitle("控制结果").setView(layoutHint)
                .setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        configDialog.show();
    }

    private Scene addSceneView() {
        Scene addScene = new Scene();
        addScene.setId("0");
        addScene.setName("场景");
        addScene.setIcon(((BitmapDrawable) getResources().getDrawable(
                R.drawable.add_scene)).getBitmap());
        return addScene;
    }

    /**
     * 方法名称： CustomToast 方法描述： 自定义Toast 参数： @param info 返回值类型： void 创建时间：
     * 2015-3-9下午2:59:08
     */
    private void CustomToast(String info) {
        Toast toast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 方法名称： dialogShow 方法描述： 加载框显示 参数： @param string 返回值类型： void 创建时间：
     * 2015-4-11上午8:56:51
     */
    private void dialogShow(String string) {
        dialog = ApplicationUtil.getLoadingDialog(getActivity(), string);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /*
     * (非Javadoc) 方法名称： onResume 方法描述： TODO 重写部分： @see
     * android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        System.out.println("TabSecondFragment onResume excuted");
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        super.onResume();
    }
}
