package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;

/**
 *
 * Author:FunFun
 * Function:
 * Date:2016/4/12 8:54
 */
public class EmspInstruction {
    public static int port = 8089;
    public static String desIp = "255.255.255.255";
    public static String serverIp = "115.28.43.73";
    public static int serverPort = 9954;
    public static byte[] config_ini = null;
    public static byte[] get_mac = {0x0C, 0x00, 0x0A, 0x00, 0x00, 0x00, (byte) 0xE9, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    public static byte[] get_config = {0x02, 0x00, 0x0A, 0x00, 0x00, 0x00, (byte) 0xF3, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
    private static byte[] server_ip;
    private static byte[] server_port;
    /**
     * 参数设置： config_ini to set
     */
    public static void setConfig_ini(byte[] config_ini)
    {
        //保存get_config的信息
        EmspInstruction.config_ini = new byte[169];
        for (int i = 0; i < 169; i++)
        {
            EmspInstruction.config_ini[i] = config_ini[i];
        }
        //生成服务器信息指令
        server_ip = Helper.StringToHexBytes(16, serverIp);
        server_port = Helper.intToByteArray(serverPort);
    }
    public static byte[] set_config()
    {
        byte[] set_config = config_ini;
        //修改帧头
        set_config[0] = 0x03;
        set_config[4] = 0x00;
        //添加服务信息
        for (int i = 0; i < 16; i++)
        {
            set_config[74+i] = server_ip[i];
        }
        for (int i = 0; i < 2; i++)
        {
            set_config[122+i] = server_port[i];
        }
        //设置TCP Client
        set_config[124] = 0x01;
        //复制数据域值
        byte[] data = new byte[config_ini.length - 10];
        for (int i = 0; i < data.length; i++)
        {
            data[i] = set_config[i+8];
        }
        //计算校验位
        byte[] check = Helper.checkSum(data, data.length);
        set_config[set_config.length - 2] = check[0];
        set_config[set_config.length - 1] = check[1];

//		StringBuffer getString = new StringBuffer("set_config：");
//		for (int i = 0; i < set_config.length; i++)
//		{
//			getString.append(
//					" "+
//					"0123456789ABCDEF".charAt(0xf & set_config[i] >> 4)+
//					"0123456789ABCDEF".charAt(set_config[i] & 0xf));
//		}
//		System.out.println(getString);
        return set_config;
    }
    public static byte[] reset = {0x01, 0x00, 0x0A, 0x00, 0x00, 0x00, (byte) 0xF4, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
}
