package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.util.Log;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:百芬扩香机设备管理器，单例模式，管理设备的增，删，改，查
 * Date:2016/4/12 9:24
 */
public class DeviceManager extends Manager {
    // 创建单例模式
    private static class DeviceHolder {
        static final DeviceManager DEVICE_MANAGER = new DeviceManager();
    }

    public static DeviceManager getInstance() {
        return DeviceHolder.DEVICE_MANAGER;
    }

    private Device device;
    // 设备列表
    private List<Device> devices;

    private DeviceManager() {
        devices = new ArrayList<Device>();
    }

    @Override
    public Object create() {
        device = new Device();
        return device;
    }

    @Override
    public void delete(Object item) {
        devices.remove(item);
    }

    public void delete(int index) {
        devices.remove(index);
    }

    @Override
    public void update(Object item) {
    }

    public void update(int index, boolean state) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getId() == index) {
                devices.get(i).setLeave(state);
                Log.i("ustate", devices.get(i).getMac());
            }

        }
    }

    @Override
    public Object get(int index) {
        return devices.get(index);
    }

    public Device get(String mac) {
        if (devices == null) {
            return null;
        }
        int index = 0;
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getMac().equals(mac)) {
                index = i;
            }
        }
        return devices.get(index);
    }

    @Override
    public void add(Object item) {
        for (int i = 0; i < devices.size(); i++) {
            if (((Device) item).getMac().equals(devices.get(i).getMac())) {
                return;
            }
        }
        devices.add((Device) item);
    }

    /**
     * 方法名称：	getCount
     * 方法描述：	获取设备个数
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-4-6上午9:34:28
     */
    public int getCount() {
        return devices.size();
    }

    /**
     * 方法名称：	getOnlineCount
     * 方法描述：	获取在线设备个数
     * 参数：			@return
     * 返回值类型：	int
     * 创建时间：	2015-5-22下午2:49:44
     */
    public int getOnlineCount() {
        int count = 0;
        for (int i = 0; i < devices.size(); i++) {
            if (!devices.get(i).isLeave()) {
                count++;
                Log.i("onMAC", devices.get(i).getName()
                        + devices.get(i).getMac());
                Log.i("onstate", devices.get(i).getName()
                        + devices.get(i).isLeave() + "");
            }
        }
        Log.i("count", "在线设备的数量:" + count);
        return count;
    }

    /**
     * 返回值： devices
     */
    public List<Device> getDevices() {
        return devices;
    }

    /* (non-Javadoc)
     * @see com.example.bonfeel.util.Manager#clean()
     */
    @Override
    public void clean() {
        device = null;
        devices.clear();
    }
}
