package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import org.ksoap2.SoapEnvelope;

/**
  *
  * Author:FunFun
  * Function:软件常量类,存储了包括网络通信，标签，方法名等常量。
  * Date:2016/4/12 8:55
  */
public class NetElectricsConst {
     public static final String SCENE_BROADCAST = "scene_config_to_main";

     /*
      * http通信参数
      */
     public static final String HTTP_NAMESPACE = "dosoft";
     // public static final String HTTP_URL =
     // "http://115.28.43.73:4321/SmartHomeDiffuser.asmx"; //IP地址
     public static final String HTTP_URL = "http://server.bonfeel.com.cn/SmartHomeDiffuser.asmx"; // 域名
     public static final int HTTP_VERSION = SoapEnvelope.VER12;
     /*
      * socket通信参数
      */
     // private static final String SERVER_ADDRESS =
     // "http://115.28.43.73:8221/api.aspx"; //IP地址
     private static final String SERVER_ADDRESS = "http://server.bonfeel.com.cn:8221/api.aspx"; // 域名
     // public static final String SOCKET_IP = "115.28.43.73"; //IP地址
     public static final String SOCKET_IP = "server.bonfeel.com.cn"; // 域名
     public static final int SOCKET_PORT = 9954;
     public static final int SOCKET_TIMEOUT = 30000;
     // 描述数据时上行还是下行
     public static final int MobileToDevice = 0;
     public static final int DeviceToMobile = 1;

     // webservice定义的方法
     // public static final String METHOD_REGIST = "I_RegisterUser";
     public static final String METHOD_REGIST = "I_RegisterUserV2";// 修改后的注册接口
     public static final String METHOD_FINDPASSWORD = "I_FindPassWord";// 修改后的注册接口
     public static final String METHOD_LOGIN = "I_UserLogin";

     public static final String METHOD_LOGINV2 = "I_UserLoginV2";
     public static final String METHOD_MY_COUNT = "I_GetUserInfo";
     public static final String METHOD_SET_COUNT = "I_SetUserInfo";
     public static final String METHOD_GET_DEVICE = "I_GetDevice";
     public static final String METHOD_ADD_DEVICE = "I_AddDevice";
     public static final String METHOD_DEL_DEVICE = "I_DelDevice";
     public static final String METHOD_GET_SCENE = "I_GetScene";
     public static final String METHOD_ADD_SCENE = "I_AddScene";
     public static final String METHOD_DEL_SCENE = "I_DelScene";
     public static final String METHOD_EXEC_SCENE = "I_ExecScene";
     public static final String METHOD_SCENE_DETAIL = "I_GetSceneDetail";
     // public static final String METHOD_MODIFY_USER = "I_ModifyUserInfo";
     // public static final String METHOD_GET_HELP = "I_GetHelpInfo";
     public static final String METHOD_SUB_SERVICE = "I_SetServiceSubmit";
     public static final String METHOD_APP_VERSION = "I_GetAppVersion";
     public static final String METHOD_EASY_LINK = "I_MatchDevice";
     public static final String METHOD_EASY_LINKV2 = "I_MatchDeviceV2";
     public static final String METHOD_EASY_LINKV3 = "I_MatchDeviceV3";
     public static final String METHOD_CURRENT_DATE = "I_GetTime";
     public static final String METHOD_CHANGE_DEVICE = "I_ChangeDevice";
     public static final String METHOD_CHANGE_PASS = "I_ChangePass";
     public static final String METHOD_GET_USUALPROBLEM = "I_GetAppQuestion";
     public static final String METHOD_PROP_SET = "I_UpdScene";
     public static final String METHOD_SCENE_ICON = "I_UpdSceneImage";
     public static final String METHOD_SCENE_NAME = "I_UpdSceneName";
     public static final String METHOD_SCENE_BIND = "I_UpdSceneAddDevice";
     public static final String METHOD_SCENE_UNBIND = "I_UpdSceneDelDevice";
     public static final String METHOD_EASY_CONTROL = "I_OPAllDevice";

     /*
      * 定义的枚举，用于对http通信返回的数据进行json翻译用
      */
     public enum METHOD_TAG {
          RegisterUser, // 用户注册
          UserLogin, // 用户登录
          GetUserInfo, // 获取用户信息
          GetDevice, // 获取设备列表
          AddDevice, // 添加设备
          DelDevice, // 删除设备
          GetScene, // 获取场景列表
          AddScene, // 添加场景
          DelScene, // 删除场景
          ExecScene, // 启动场景
          SceneDetail, // 场景详情
          ModifyUserInfo, // 修改用户信息
          GetHelpInfo, // 获取帮助信息
          SetServiceSubmit, // 意见反馈
          GetAppVersion, // 获取软件版本
          MatchDevice, // 一键配置
          GetDate, // 获取标准时间
          ChangeDevice, // 修改设备
          GetProblems, // 获取常见问题
          UpdateDevice;// 更新设备在离线
     }

     // webservice用到的方法参数名称
     public static final String USER_NAME = "mobileid";
     public static final String NAME = "username";
     public static final String USER_CODE = "password";
     public static final String OLD_CODE = "password_old";
     public static final String NEW_CODE = "password_new";
     public static final String PHONE_MAC = "mac";
     public static final String EMAIL = "email";// 邮箱
     public static final String TOKEN = "token";
     public static final String DEVICE_MAC = "devicemac";
     public static final String DEVICE_NAME = "devicename";
     public static final String DEVICE_MAC_ID = "mac";
     public static final String DEVICE_NAME_ID = "name";
     public static final String INSTALL_ADDR = "addr";
     public static final String DEVICE_ID = "deviceid";
     public static final String AREA_ID = "areaid";
     public static final String AREA_NAME = "areaname";
     public static final String ADDRESS = "address";
     public static final String MAIL_BOX = "mailbox";
     public static final String MOBILE = "mobile";
     public static final String QQ = "qq";
     public static final String WEIXIN = "weixin";
     public static final String TITLE = "title";
     public static final String CONTENT = "content";
     public static final String IMAGE = "image";
     public static final String TELEPHONE = "tel";
     public static final String ADDR = "addr";
     public static final String TYPE = "type";
     public static final String SCENE_ID = "sceneid";
     public static final String SCENE_NAME = "name";
     public static final String SCENE_IMAGE = "image";
     public static final String DEVICE_LIST = "deviceidlist";
     public static final String SCENE_DEVICE_ITME = "itemid";
     public static final String PROPNAME = "propname";
     public static final String PROPVALUE = "propvalue";
     public static final String STATE = "openstate";

     /*
      * socket通信用到的服务action
      */
     public static final String ACTION_CONNECT = "diffuser_connect_service";
     public static final String ACTION_SEND = "diffuser_send_service";
     public static final String ACTION_CLOSE = "diffuser_close_service";
     public static final String ACTION_ALARM = "diffuser_alarm_service";
     /*
      * 广播的action,用于sokect通信结果的响应
      */
     public static final String ACTION_PHONE_SIGNUP = "diffuser_phone_signup";
     public static final String ACTION_DATA_RECEIVE = "diffuser_receive_cast";
     public static final String ACTION_CURRENT_TIME = "diffuser_current_time";
     public static final String ACTION_DEVICE_SIGNIN = "diffuser_device_signin";
     public static final String ACTION_ALARM_CAST = "diffuser_alarm_cast";
     public static final String ACTION_SET_TIMESCALE = "diffuser_set_timescale";
     public static final String ACTION_SET_CURTIMESCALE = "diffuser_set_curtimescale";
     public static final String ACTION_CHECK_TEMPWEAT = "diffuser_check_tempweat";
     public static final String ACTION_CONTROL_NIG = "diffuser_control_nig";
     public static final String ACTION_HEART_BEAT = "diffuser_heart_beat";
     // 这两个广播的功能类似handler，不参与socket通信
     public static final String ACTION_SET_SUCCEED = "diffuser_set_succeed";
     public static final String ACTION_SET_TIMESCALE_SUCCEED = "diffuser_set_timescale_succeed";

     public static final String ACTION_DATA_WRITE = "diffuser_write_cast";
     public static final String ACTION_DATA_FAILED = "diffuser_failed_cast";
     public static final String ACTION_DATA_ERROR = "diffuser_error_cast";
	/*
	 * 标记，用来决定当成功收到socket通信数据之后发送哪个广播
	 */
     // public static final int WindControl = 0;//测试风速

     public static final int PhoneSignUp = 1;
     public static final int DeviceSignIn = 2;
     public static final int QueryState = 3;
     public static final int QueryTimeScale = 3;
     public static final int ControlDevice = 3;
     public static final int CurTime = 4;
     public static final int SetTimeScale = 5;
     public static final int SetCurTimeScale = 6;
     public static final int CheckTempWeat = 7;
     public static final int ControlNIG = 8;

     /*
      * socket通信用到的数据帧类型
      */
     public static final String OPERATE_QUERY = "query";
     public static final String OPERATE_CONTROL = "control";
     public static final String OPERATE_SUBMIT = "submit";
     public static final String OPERATE_RESPOND = "respond";
     public static final String DEVICE_OFFLINE = "offline";

     /*
      * http接口通信，返回的结果类型
      */
     public static final int STYLE_RES = 0;
     public static final int STYLE_OBJECT = 1;

     /*
      * 限定场景个数
      */
     public static final int MAX_SCENE_COUNT = 8;
     public static final int MAX_DEVICE_COUNT = 50;
     /*
      * 未使用部分
      */
     public static final String XML_SOAP_TITLE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
     public static final String XML_SOAP_HEAD = "<soap:Envelope "
             + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
             + "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" "
             + "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">";
     public static final String XML_SOAP_BODY = "<soap:Body>";
     public static final String XML_SOAP_BODY_END = "</soap:Body>";
     public static final String XML_SOAP_END = "</soap:Envelope>";
     public static final String I_GetExpend = "http://115.28.43.73:8222/SmartHomeAirCondition.asmx?op=I_GetExpend";
     public static final String I_GetExpendSoap = "http://tempuri.org/I_GetExpend";

}
