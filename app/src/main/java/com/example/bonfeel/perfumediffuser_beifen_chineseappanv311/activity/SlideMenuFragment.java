package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.DeviceAdapter;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.DeviceManager;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.LogUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.ksoap2.serialization.PropertyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:侧滑菜单,用于显示设备列表
 * Date:2016/4/12 13:33
 */
public class SlideMenuFragment extends Fragment {
    private String tag = "设备列表界面";
    private View tabLayout;
    private Button btnAddDevice;
    private ListView lvDevice;
    private DeviceAdapter adapter;
    private MainActivity mainActivity = null;

    private CustomDialog digDelDevice;
    private View layoutDlgContent;
    private TextView tvDelName;

    private final int DEL_DEVICE_SUCCESS = 1;
    private final int DEL_DEVICE_FAIL = 2;
    private final int DEL_DEVICE_ERROR = 3;

    private final int UPDATE_DEVICE_LIST = 4;
    private final int UPDATE_DEVICE_LIST_FAIL = 5;
    private final int UPDATE_DEVICE_LIST_ERROR = 6;

    /*
     * 方法名称：	onCreate
     * 方法描述：	TODO
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialData();
        mainActivity = (MainActivity) SlideMenuFragment.this
                .getActivity();
    }

    private void initialData() {
        adapter = new DeviceAdapter(ApplicationUtil.getContext(),
                R.layout.device_item, DeviceManager.getInstance().getDevices());
    }
    /*
     * 方法名称：	onCreateView
     * 方法描述：
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tabLayout = inflater.inflate(R.layout.fragment_slide_menu, container,
                false);
        mainActivity.showMenu(tabLayout);
        LogUtil.logDebug(tag, "onCreateView"
                + DeviceManager.getInstance().getDevices());
        initialView(tabLayout);
        initialHandler();
        return tabLayout;
    }

    /**
     * 方法名称：	initialView
     * 方法描述：	初始化控件
     * 参数：			@param v
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午10:45:16
     */
    private void initialView(View v) {
        lvDevice = (ListView) v.findViewById(R.id.lv_devices);
        lvDevice.setAdapter(adapter);
        btnAddDevice = (Button) v.findViewById(R.id.btn_add_device);
    }

    /**
     * 方法名称：	initialHandler
     * 方法描述：	初始化事件处理
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-1-21上午10:45:43
     */
    private void initialHandler() {
        // 添加设备
        btnAddDevice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 判断是否已是最大数量
                if (DeviceManager.getInstance().getCount() < NetElectricsConst.MAX_DEVICE_COUNT) {
                    Intent addIntent = new Intent(ApplicationUtil.getContext(),
                            FirstConfigActivity.class);
                    startActivity(addIntent);
                } else {
                    CustomToast("设备最多为50台");
                }
            }
        });
        // 选择设备
        lvDevice.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Device device = (Device) DeviceManager.getInstance().get(
                        position);
                mainActivity.toggle();
                mainActivity.getFirstFragment().setDevice(device);
                if (mainActivity.getThirdFragment() != null) {
                    mainActivity.getThirdFragment().setDevice(device);
                }
            }
        });
        // 删除设备
        lvDevice.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
    }

    /**
     * 方法名称：	showDeleteDialog
     * 方法描述：	删除设备对话框
     * 参数：			@param index
     * 返回值类型：	void
     * 创建时间：	2015-3-16下午3:15:54
     */
    private void showDeleteDialog(final int index) {
        Device device = (Device) DeviceManager.getInstance().get(index);
        layoutDlgContent = LayoutInflater.from(getActivity()).inflate(
                R.layout.dialog_delete_device, null);
        tvDelName = (TextView) layoutDlgContent
                .findViewById(R.id.tv_device_name);
        tvDelName.setText("确认删除" + device.getName() + "吗？");
        digDelDevice = new CustomDialog(getActivity()).builder()
                .setTitle("删除设备").setView(layoutDlgContent)
                .setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).setPositiveButton("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteDevice(index);
                    }
                });
        digDelDevice.show();
    }

    /**
     * 方法名称：	deleteDevice
     * 方法描述：	tap接口调用，删除设备
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-16下午3:01:08
     */
    private void deleteDevice(final int index) {
        Device device = (Device) DeviceManager.getInstance().get(index);
        NetCommunicationUtil.httpConnect(getDeleteInfos(device.getId()),
                NetElectricsConst.METHOD_DEL_DEVICE,
                NetElectricsConst.STYLE_RES, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            message.what = DEL_DEVICE_SUCCESS;
                            message.arg1 = index;
                            Log.i("msg.arg1", index + "");
                        } else {
                            message.what = DEL_DEVICE_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = DEL_DEVICE_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getDeleteInfos
     * 方法描述：	删除设备时的参数集
     * 参数：			@param id
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-9下午7:43:50
     */
    private List<PropertyInfo> getDeleteInfos(int id) {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo deviceId = new PropertyInfo();
        deviceId.setName(NetElectricsConst.DEVICE_ID);
        deviceId.setValue(id);

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(deviceId);
        infos.add(token);

        return infos;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 删除设备成功
                case DEL_DEVICE_SUCCESS:
                    // 准备被删除的设备
                    Device device = (Device) DeviceManager.getInstance().get(
                            msg.arg1);
                    Log.i("getCount", DeviceManager.getInstance().getCount() + "");
                    if (device.isSelect()) {
                        // 先判断是否还有其他设备，若有，则显示下一次；若无，则显示无设备
                        MainActivity mainactivity = (MainActivity) SlideMenuFragment.this
                                .getActivity();
                        if (DeviceManager.getInstance().getCount() > 1) {
                            // 判断是不是设备列表末尾的那个
                            if (DeviceManager.getInstance().getCount() == (msg.arg1 + 1)) {

                                mainactivity.getFirstFragment()
                                        .setDevice(
                                                (Device) DeviceManager
                                                        .getInstance().get(0));
                                if (mainactivity.getThirdFragment() != null) {
                                    mainactivity.getThirdFragment().setDevice(
                                            (Device) DeviceManager.getInstance()
                                                    .get(0));
                                }
                            } else {
                                // 如果不是删除最后一个，则显示删除的下一个
                                mainactivity.getFirstFragment().setDevice(
                                        (Device) DeviceManager.getInstance().get(
                                                msg.arg1 + 1));
                                if (mainactivity.getThirdFragment() != null) {
                                    mainactivity.getThirdFragment().setDevice(
                                            (Device) DeviceManager.getInstance()
                                                    .get(msg.arg1 + 1));
                                }
                            }
                        } else {
                            mainactivity.getFirstFragment().setDevice(null);
                            if (mainactivity.getThirdFragment() != null) {
                                mainactivity.getThirdFragment().setDevice(null);
                            }
                        }
                    }
                    DeviceManager.getInstance().delete(msg.arg1);
                    adapter.notifyDataSetChanged();
                    CustomToast("成功删除设备");
                    break;
                case DEL_DEVICE_FAIL:
                    break;
                case DEL_DEVICE_ERROR:
                    break;
                case UPDATE_DEVICE_LIST:
                    adapter.notifyDataSetChanged();
                    break;
                // fun add
                case UPDATE_DEVICE_LIST_FAIL:
                    // Toast.makeText(getActivity(), "获取更新后的设备列表失败!",
                    // Toast.LENGTH_LONG).show();

                    break;
                // fun add
                case UPDATE_DEVICE_LIST_ERROR:
                    // Toast.makeText(getActivity(), "获取更新后的设备列表程序异常!请稍后重试……",
                    // Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 方法名称：	CustomToast
     * 方法描述：	自定义Toast
     * 参数：			@param info
     * 返回值类型：	void
     * 创建时间：	2015-3-9下午2:59:08
     */
    private void CustomToast(String info) {
        Toast toast = Toast.makeText(ApplicationUtil.getContext(), info,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /* (非Javadoc)
     * 方法名称：	onResume
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        LogUtil.logDebug(tag, "onResume exectuded");
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    /**
     * 方法名称：	queryDeviceList
     * 方法描述：	http接口，更新一键配置后的设备列表
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-16下午4:35:23
     */
    public void updateDeviceList() {
        NetCommunicationUtil.httpConnect(getDeviceListInfos(),
                NetElectricsConst.METHOD_GET_DEVICE,
                NetElectricsConst.STYLE_OBJECT, new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        // fun add
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {

                            // 更新设备列表的在离线状态
                            NetCommunicationUtil.getHttpResultInfo(
                                    NetElectricsConst.METHOD_TAG.UpdateDevice,
                                    bundle.getString("result"));
                            message.what = UPDATE_DEVICE_LIST;
                            // handler.sendMessage(message);
                            // fun add
                        } else {
                            message.what = UPDATE_DEVICE_LIST_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        // fun add
                        Message message = handler.obtainMessage();
                        message.what = UPDATE_DEVICE_LIST_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }

    /**
     * 方法名称：	getDeviceListInfos
     * 方法描述：	获取设备列表的参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-16下午4:41:23
     */
    private List<PropertyInfo> getDeviceListInfos() {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo nameInfo = new PropertyInfo();
        nameInfo.setName(NetElectricsConst.USER_NAME);
        nameInfo.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(nameInfo);
        infos.add(token);

        return infos;
    }

    /* (非Javadoc)
     * 方法名称：	onDestroy
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
