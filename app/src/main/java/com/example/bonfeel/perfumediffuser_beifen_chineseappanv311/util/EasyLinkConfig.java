package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.EmspInstruction;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetInfo;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Random;

/**
 *
 * Author:FunFun
 * Function:一键配置。一键配置的路由器配置，不涉及服务器信息的配置，只配置设备需要连接的无线网路
 * 					方便设备移动时，轻松更改网络连接。
 * Date:2016/4/12 9:26
 */
public class EasyLinkConfig {
    private static boolean isStop;
    private static boolean isEmspStop;
    private static boolean isCheckStop;
    private static NetInfo wifiNetInfo;
    private static String syncHString = "abcdefghijklmnopqrstuvw";
    private static String desIp = "239.118.0.0";
    private static int srcPort = 54064;
    private static DatagramSocket endSocket;
    private static InetSocketAddress desSocket;
    private static int emspTag;
    private static Handler handler;

    private static final int GET_MAC = 0;

    /**
     * 方法名称：	checkDevice
     * 方法描述：	检查网络中是已有设备
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-15上午11:44:01
     */
    public static void  checkDevice(Handler msgHandler)
    {
        handler = msgHandler;
        try
        {
            //客户端socket
            endSocket = new DatagramSocket(9999);
        }
        catch ( SocketException e )
        {
            e.printStackTrace();
        }
        //目标socket
        desSocket = new InetSocketAddress(EmspInstruction.desIp, EmspInstruction.port);
        isCheckStop = false;
        checkListener();
        checkSend();
    }
    private static void checkListener()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                byte[] getBuffer = new byte[1024];
                DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
                while (!isCheckStop)
                {
                    try
                    {
                        System.out.println("waitting for device's response");
                        endSocket.receive(getPacket);
                        if ( getPacket != null )
                        {
                            System.out.println("get device's mac address success"+ getPacket.getAddress().toString());
                            //提取出mac地址
                            byte[] mac = new byte[6];
                            for (int i = 0; i < mac.length; i++)
                            {
                                mac[i] = getPacket.getData()[i + 8];
                            }
                            SocketAddress socketAddress = getPacket.getSocketAddress();
                            EasyLinkUtil.pushExistQueue(mac, socketAddress);
                        }
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private static void checkSend()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    DatagramPacket packet = null;
                    int checkTimes = 0;
                    while (!isCheckStop)
                    {
                        packet = new DatagramPacket(EmspInstruction.get_mac, EmspInstruction.get_mac.length, desSocket);
                        System.out.println("发送get_mac指令：");
                        endSocket.send(packet);
                        Thread.sleep(1000L);
                        checkTimes++;
                        //get_mac时间已到
                        if ( checkTimes > 4 )
                        {
                            stopCheckDevice();
                            endSocket.close();
                            System.out.println("finish check device");
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            handler.sendMessage(message);
                            break;
                        }
                    }
                }
                catch ( SocketException e )
                {
                    e.printStackTrace();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static void stopCheckDevice()
    {
        isCheckStop = true;
    }
    /**
     * 方法名称：	transminNetInfo
     * 方法描述：	发送ssid，key，phone_ip给模块,使模块能够连接上局域网
     * 参数：			@param netInfo
     * 返回值类型：	void
     * 创建时间：	2015-3-13下午2:46:08
     */
    public static void transminNetInfo(final Context context)
    {
        wifiNetInfo = EasyLinkUtil.getNetInfo();
        isStop = false;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                EasyLinkUtil.MulticastLockAcquire(context);
                while (!isStop)
                {
                    try
                    {
                        transmitting();
                        System.out.println("发送ssid和key");
                    }
                    catch ( InterruptedException e )
                    {
                        e.printStackTrace();
                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
                if ( isStop )
                {
                    EasyLinkUtil.MulticastLockRelease(context);
                }
            }
        }).start();
    }
    /**
     * 方法名称：	transmitting
     * 方法描述：	传输网络信息给模块
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-13下午2:50:43
     * @throws InterruptedException
     * @throws IOException
     */
    private static void transmitting() throws InterruptedException, IOException
    {
        byte[] data = new byte[2];
        InetSocketAddress desSocket;
        DatagramPacket packet;
        byte[] syncHBuffer = syncHString.getBytes();

        byte[] userInfo = new byte[5];
        userInfo[0] = 0x23;
        String strUserIp = String.format("%08x", wifiNetInfo.getPhone_ip());
        System.arraycopy(Helper.hexStringToBytes(strUserIp), 0, userInfo, 1, 4);
        byte[] key = new byte[wifiNetInfo.getKey().getBytes("UTF-8").length];
        byte[] ssid = new byte[wifiNetInfo.getSsid().getBytes("UTF-8").length];
        System.arraycopy(wifiNetInfo.getKey().getBytes("UTF-8"), 0, key, 0, key.length);
        System.arraycopy(wifiNetInfo.getSsid().getBytes("UTF-8"), 0, ssid, 0, ssid.length);

        data[0] = (byte) ssid.length;
        data[1] = (byte) key.length;
        data = Helper.byteMerger(data, (Helper.byteMerger(ssid, key)));
        for (int i = 0; i < 5; i++)
        {
            desSocket = new InetSocketAddress(InetAddress.getByName(desIp), getRandomNumber());
            packet = new DatagramPacket(syncHBuffer, 20, desSocket);
            send(packet, desIp);
            Thread.sleep(10L);
        }
        byte[] tempLen = null;
        if ( data.length % 2 == 0 )
        {
            if ( userInfo.length == 0 )
            {
                tempLen = new byte[]{0, 0, 0};
            }
            else
            {
                tempLen = new byte[]{(byte) userInfo.length, 0};
            }
        }
        else
        {
            tempLen = new byte[]{0, (byte) userInfo.length, 0};
        }
        data = Helper.byteMerger(data, tempLen);
        data = Helper.byteMerger(data, userInfo);
        for (int i = 0; i < data.length; i += 2)
        {
            String ip = "";
            if ( i + 1 < data.length )
            {
                ip = "239.126." + (int)(data[i] & 0xff) + "." + (int)(data[i + 1] & 0xff);
            }
            else
            {
                ip = "239.126." + (int)(data[i] & 0xff) + ".0";
            }
            desSocket = new InetSocketAddress(InetAddress.getByName(ip), getRandomNumber());
            byte[] empty = new byte[i / 2 + 20];
            packet = new DatagramPacket(empty, empty.length, desSocket);
            send(packet, ip);
            Thread.sleep(10L);
        }
    }
    /**
     * 方法名称：	send
     * 方法描述：	发送数据包
     * 参数：			@param packet
     * 参数：			@param destnation
     * 参数：			@throws IOException
     * 返回值类型：	void
     * 创建时间：	2015-3-13下午3:13:41
     */
    private static void send(DatagramPacket packet, String destnation) throws IOException
    {
        MulticastSocket socket = new MulticastSocket(srcPort);
        socket.setReuseAddress(true);
        socket.joinGroup(InetAddress.getByName(destnation));
        socket.send(packet);
        socket.close();
    }
    public static void stopTransminNetInfo()
    {
        isStop = true;
    }
    private static int getRandomNumber()
    {
        int num = new Random().nextInt(65536);
        if ( num < 10000 )
        {
            return 65523;
        }
        else
        {
            return num;
        }
    }
    /**
     * 方法名称：	emspControl
     * 方法描述：	此操作在模块连接上无线局域网之后进行的EMSP指令通信
     * 参数：			@param handler
     * 返回值类型：	void
     * 创建时间：	2015-3-16上午9:08:27
     */
    public static void emspControl()
    {
        try
        {
            //客户端socket
            endSocket = new DatagramSocket(9999);
        }
        catch ( SocketException e )
        {
            e.printStackTrace();
        }
        isEmspStop = false;
        //emsp指令index
        emspTag = GET_MAC;
        //监听emsp指令
        emspListener();
        //发送emsp指令
        emspSend();
    }
    /**
     * 方法名称：	emspListener
     * 方法描述：	监听返回emsp,这里用handler而不用interface是因为handler可以更新界面
     * 参数：			@param handler
     * 返回值类型：	void
     * 创建时间：	2015-3-16上午9:08:43
     */
    private static void emspListener()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                byte[] getBuffer = new byte[1024];
                DatagramPacket getPacket = new DatagramPacket(getBuffer, getBuffer.length);
                while (!isEmspStop)
                {
                    try
                    {
                        System.out.println("等待返回消息");
                        endSocket.receive(getPacket);
                        if ( getPacket != null )
                        {
                            switch (emspTag)
                            {
                                //当收到get_mac的返回时，停止发送ssid和key
                                case GET_MAC:
                                    System.out.println("get device's mac address success"+ getPacket.getAddress().toString());
                                    //提取出mac地址
                                    byte[] mac = new byte[6];
                                    for (int i = 0; i < mac.length; i++)
                                    {
                                        mac[i] = getPacket.getData()[i + 8];
                                    }
                                    SocketAddress socketAddress = getPacket.getSocketAddress();
                                    EasyLinkUtil.pushQueue(mac, socketAddress);
                                    break;
                            }
                        }

                    }
                    catch ( IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    /**
     * 方法名称：	emspSend
     * 方法描述：	emsp指令发送,根据tag区别步骤：0==get_mac, 1== get_config, 2 == set_config, 3 == reset,
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-13下午4:06:13
     */
    private static void emspSend()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    DatagramPacket packet = null;
                    int sendTimer = 0;
                    while (!isEmspStop)
                    {
                        switch (emspTag)
                        {
                            case GET_MAC:
                                packet = new DatagramPacket(EmspInstruction.get_mac, EmspInstruction.get_mac.length, desSocket);
                                break;
                        }
                        System.out.println("发送get_mac指令：");
                        endSocket.send(packet);
                        Thread.sleep(1000L);
                        sendTimer++;
                        //get_mac时间已到
                        if ( sendTimer > 19 )
                        {
                            stopTransminNetInfo();
                            stopEmspControl();
                            endSocket.close();
                            System.out.println("finish device config");
                            Message message = handler.obtainMessage();
                            message.what = 2;
                            handler.sendMessage(message);
                            break;
                        }
                    }
                }
                catch ( SocketException e )
                {
                    e.printStackTrace();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private static void stopEmspControl()
    {
        isEmspStop = true;
    }
}
