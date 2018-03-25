package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import java.util.ArrayList;
import java.util.List;

/**
  *
  * Author:FunFun
  * Function:百芬扩香机设备类
  * Date:2016/4/12 8:52
  */
public class Device {
     private String sceneID;// 场景控制的唯一编号
     private int id;// 设备编号
     private String name;// 设备名称
     private String mac;// 设备mac
     private boolean power;// 设备开关
     private int airSpeed;// 风速(配置文件V1.2后暂不用了)
     private double volume;// 香水剩余量
     private CurTime curTime;// 设备当前时间
     private int error;// 故障
     private boolean isBeep;// 蜂鸣器开关(配置文件V1.2后暂不用了)
     private boolean isFullWork;// 24小时工作时间段开关(配置文件V1.2后暂不用了)
     private List<TimeScale> timeScales;// 设备的工作时间段
     private int curIndex;// 当前设备工作的时间段ID
     private int temp;// 设备周围的温度
     private int weat;// 设备周围的湿度
     private boolean isNIG;// 负离子发生器开关

     private InstallLocation location;// 安装位置的描述
     private boolean isSelect;// 是否是当前选择设备
     private boolean isLeave;// 设备是否离线状态
     private int tempError;// 温度传感器故障
     private int timeError;// 时钟故障
     private String DeviceAddress;// fun add

     /**
      * 构造方法名称：	Device
      * 创建时间：		2015-1-15下午3:56:49
      */
     public Device() {
         this.sceneID = "";
         this.id = 0;
         this.name = "";
         this.mac = "";
         this.DeviceAddress = "";
         this.power = false;
         this.airSpeed = 1;
         this.volume = 100;
         this.curTime = new CurTime();
         this.error = 0;
         this.isBeep = false;
         this.isFullWork = true;
         this.timeScales = new ArrayList<TimeScale>();
         this.curIndex = 1;
         this.isSelect = false;
         this.location = new InstallLocation();
         this.temp = 0;
         this.weat = 0;
         this.isNIG = false;
         this.isLeave = false;
         this.tempError = 0;
         this.timeError = 0;
     }

     public String getDeviceAddress() {
         return DeviceAddress;
     }

     public void setDeviceAddress(String deviceAddress) {
         this.DeviceAddress = deviceAddress;
     }

     /**
      * 返回值： sceneID
      */
     public String getSceneID() {
         return sceneID;
     }

     /**
      * 参数设置： sceneID to set
      */
     public void setSceneID(String sceneID) {
         this.sceneID = sceneID;
     }

     /**
      * 返回值： id
      */
     public int getId() {
         return id;
     }

     /**
      * 参数设置： id to set
      */
     public void setId(int id) {
         this.id = id;
     }

     /**
      * 返回值： name
      */
     public String getName() {
         return name;
     }

     /**
      * 参数设置： name to set
      */
     public void setName(String name) {
         this.name = name;
     }

     /**
      * 返回值： mac
      */
     public String getMac() {
         return mac;
     }

     /**
      * 参数设置： mac to set
      */
     public void setMac(String mac) {
         this.mac = mac;
     }

     /**
      * 返回值： power
      */
     public boolean isPower() {
         return power;
     }

     /**
      * 参数设置： power to set
      */
     public void setPower(boolean power) {
         this.power = power;
     }

     /**
      * 返回值： airSpeed
      */
     public int getAirSpeed() {
         return airSpeed;
     }

     /**
      * 参数设置： airSpeed to set
      */
     public void setAirSpeed(int airSpeed) {
         this.airSpeed = airSpeed;
     }

     /**
      * 返回值： volume
      */
     public double getVolume() {
         return volume;
     }

     /**
      * 参数设置： volume to set
      */
     public void setVolume(double d) {
         this.volume = d;
     }

     /**
      * 返回值： curTime
      */
     public CurTime getCurTime() {
         return curTime;
     }

     /**
      * 参数设置： curTime to set
      */
     public void setCurTime(CurTime curTime) {
         this.curTime = curTime;
     }

     /**
      * 返回值： error
      */
     public int getError() {
         return error;
     }

     /**
      * 参数设置： error to set
      */
     public void setError(int error) {
         this.error = error;
     }

     /**
      * 返回值： isBeep
      */
     public boolean isBeep() {
         return isBeep;
     }

     /**
      * 参数设置： isBeep to set
      */
     public void setBeep(boolean isBeep) {
         this.isBeep = isBeep;
     }

     /**
      * 返回值： isFullWork
      */
     public boolean isFullWork() {
         return isFullWork;
     }

     /**
      * 参数设置： isFullWork to set
      */
     public void setFullWork(boolean isFullWork) {
         this.isFullWork = isFullWork;
     }

     /**
      * 返回值： timeScales
      */
     public List<TimeScale> getTimeScales() {
         return timeScales;
     }

     /**
      * 参数设置： timeScales to set
      */
     public void setTimeScales(List<TimeScale> timeScales) {
         this.timeScales = timeScales;
     }

     // 这个用于读取设备时间段
     public void addTimeScale(TimeScale timeScale) {
         this.timeScales.add(timeScale);
     }

     // 这个用于新增时间段
     public void addNewTimeScale(TimeScale timeScale) {
         this.timeScales.add(timeScale.getId(), timeScale);
     }

     public void delTimeScale(int index) {
         for (int i = 0; i < timeScales.size(); i++) {
             if (timeScales.get(i).getId() == index) {
                 this.timeScales.remove(i);
                 break;
             }
         }

     }

     public void updateTimeScale(TimeScale timeScale) {
         for (int i = 0; i < timeScales.size(); i++) {
             if (timeScales.get(i).getId() == timeScale.getId()) {
                 timeScales.get(i).setStartTime(timeScale.getStartTime());
                 timeScales.get(i).setEndTime(timeScale.getEndTime());
                 timeScales.get(i).setWorkTime(timeScale.getWorkTime());
                 timeScales.get(i).setRestTime(timeScale.getRestTime());
                 timeScales.get(i).setCircle(timeScale.getCircle());
                 break;
             }
         }
     }

     /**
      * 方法名称：	getTimeScale
      * 方法描述：	获取ID为参数值的时间段
      * 参数：			@param index
      * 参数：			@return
      * 返回值类型：	TimeScale
      * 创建时间：	2015-4-17下午5:24:34
      */
     public TimeScale getTimeScale(int index) {
         // if ( index >= timeScales.size() )
         // {
         // return null;
         // }
         TimeScale scale = null;
         for (int i = 0; i < timeScales.size(); i++) {
             if (timeScales.get(i).getId() == index) {
                 scale = new TimeScale();
                 scale = timeScales.get(i);
                 break;
             }
         }
         return scale;
     }

     /**
      * 返回值： isSelect
      */
     public boolean isSelect() {
         return isSelect;
     }

     /**
      * 参数设置： isSelect to set
      */
     public void setSelect(boolean isSelect) {
         this.isSelect = isSelect;
     }

     /**
      * 返回值： location
      */
     public InstallLocation getLocation() {
         return location;
     }

     /**
      * 参数设置： location to set
      */
     public void setLocation(InstallLocation location) {
         this.location = location;
     }

     /**
      * 返回值： temp
      */
     public int getTemp() {
         return temp;
     }

     /**
      * 参数设置： temp to set
      */
     public void setTemp(int temp) {
         this.temp = temp;
     }

     /**
      * 返回值： weat
      */
     public int getWeat() {
         return weat;
     }

     /**
      * 参数设置： weat to set
      */
     public void setWeat(int weat) {
         this.weat = weat;
     }

     /**
      * 返回值： isNIG
      */
     public boolean isNIG() {
         return isNIG;
     }

     /**
      * 参数设置： isNIG to set
      */
     public void setNIG(boolean isNIG) {
         this.isNIG = isNIG;
     }

     /**
      * 返回值： curIndex
      */
     public int getCurIndex() {
         return curIndex;
     }

     /**
      * 参数设置： curIndex to set
      */
     public void setCurIndex(int curIndex) {
         this.curIndex = curIndex;
     }

     /**
      * 返回值： isLeave
      */
     public boolean isLeave() {
         return isLeave;
     }

     /**
      * 参数设置： isLeave to set
      */
     public void setLeave(boolean isLeave) {
         this.isLeave = isLeave;
     }

     /**
      * 返回值： tempError
      */
     public int getTempError() {
         return tempError;
     }

     /**
      * 参数设置： tempError to set
      */
     public void setTempError(int tempError) {
         this.tempError = tempError;
     }

     /**
      * 返回值： timeError
      */
     public int getTimeError() {
         return timeError;
     }

     /**
      * 参数设置： timeError to set
      */
     public void setTimeError(int timeError) {
         this.timeError = timeError;
     }
}
