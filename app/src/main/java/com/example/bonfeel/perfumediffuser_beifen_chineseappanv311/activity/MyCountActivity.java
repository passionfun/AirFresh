package com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.R;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.custom.CustomDialog;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.NetElectricsConst;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.model.User;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.ApplicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.GetUserIconTask;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.Helper;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.NetCommunicationUtil;
import com.example.bonfeel.perfumediffuser_beifen_chineseappanv311.util.UserManager;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Author:FunFun
 * Function:我的账号
 * Date:2016/4/12 11:40
 */
public class MyCountActivity extends BaseActivity
{
    private TextView tvTitle;
    //private Button btnScan;
    private TextView tvUserName;
    private TextView tvCommitAddr;
    private TextView tvMail;
    private LinearLayout layoutModifyCode;
    private LinearLayout layoutUserName;
    private LinearLayout layoutCommitAddr;
    private LinearLayout layoutMail;
    private LinearLayout layoutUserCenter;
    private ImageView ivUserIcon;

    private View layoutIconView;
    private TextView tvGallary;
    private TextView tvCamera;
    private CustomDialog iconDialog;

    private final int FROM_CONTENT = 5;
    private final int TAKE_PHOTO = 6;
    private final int CROP_PHOTO = 7;
    private final int photo_size = 320;
    private File file;
    private Bitmap bitmap;

    private String title;
    private Bundle resBundle;

    private final int GET_INFO_FAIL = 0;
    private final int GET_INFO_SUCCESS = 1;
    private final int GET_INFO_ERROR = 2;
    private final int UPLOAD_FAIL = 3;
    private final int UPLOAD_SUCCESS = 4;
    private final int UPLOAD_ERROR = 5;
    private final int GET_USER_ICON_SUCCESS = 6;

    private final int ACTIVITY_FOR_QR = 0;
    private final int ACTIVITY_FOR_NAME = 1;
    private final int ACTIVITY_FOR_ADDR = 2;
    private final int ACTIVITY_FOR_MAIL = 3;
    private final int ACTIVITY_FOR_CODE = 4;

    private Dialog dialog;

    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        setContentView(R.layout.activity_my_count);
        super.onCreate(savedInstanceState);
    }

    /* (非Javadoc)
     * 方法名称：	initialData
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialData()
     */
    @Override
    protected void initialData()
    {
        user = (User) UserManager.getInstance().get(0);
        title = "个人中心";
        dialog = ApplicationUtil.getLoadingDialog(this, "正在加载...");
        dialog.show();
        //联网请求获取用户信息
        checkUserInfo();
    }
    /**
     * 方法名称：	checkUserInfo
     * 方法描述：	获取用户信息
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-18下午4:36:44
     */
    private void checkUserInfo()
    {
        NetCommunicationUtil.httpConnect(
                getMyCountPropertyInfos(),
                NetElectricsConst.METHOD_MY_COUNT,
                NetElectricsConst.STYLE_OBJECT,
                new NetCommunicationUtil.HttpCallbackListener() {
                    @Override
                    public void onFinish(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        if (bundle.getInt("resCode") == 1) {
                            resBundle = NetCommunicationUtil.getHttpResultInfo(bundle.getString("result"), NetElectricsConst.METHOD_TAG.GetUserInfo);
                            message.what = GET_INFO_SUCCESS;
                        } else {
                            message.what = GET_INFO_FAIL;
                        }
                        handler.sendMessage(message);
                    }

                    @Override
                    public void onError(Bundle bundle) {
                        Message message = handler.obtainMessage();
                        message.what = GET_INFO_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getMyCountPropertyInfos
     * 方法描述：	获取http参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-1-31上午8:14:16
     */
    private List<PropertyInfo> getMyCountPropertyInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo userName = new PropertyInfo();
        userName.setName(NetElectricsConst.USER_NAME);
        userName.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(userName);
        infos.add(token);

        return infos;
    }
    /* (非Javadoc)
     * 方法名称：	initialView
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialView()
     */
    @Override
    protected void initialView()
    {
        tvTitle = (TextView)findViewById(R.id.tv_title);
        //btnScan = (Button)findViewById(R.id.btn_right);
        tvUserName = (TextView)findViewById(R.id.tv_user_name);
        tvCommitAddr = (TextView)findViewById(R.id.tv_commit_addr);
        tvMail = (TextView)findViewById(R.id.tv_user_mail);
        layoutModifyCode = (LinearLayout)findViewById(R.id.layout_modify_code);
        layoutUserName = (LinearLayout)findViewById(R.id.layout_user_name);
        layoutCommitAddr = (LinearLayout)findViewById(R.id.layout_commit_addr);
        layoutMail = (LinearLayout)findViewById(R.id.layout_mail);
        layoutUserCenter = (LinearLayout)findViewById(R.id.layout_user_center);
        ivUserIcon = (ImageView)findViewById(R.id.iv_user_pic);

        tvTitle.setText(title);
//		btnScan.setText("");
//		btnScan.setVisibility(View.VISIBLE);
//		btnScan.setBackgroundResource(R.drawable.button_scan);
    }

    /* (非Javadoc)
     * 方法名称：	initialHandler
     * 方法描述：	TODO
     * 重写部分：	@see com.example.bonfeel.activity.BaseActivity#initialHandler()
     */
    @Override
    protected void initialHandler()
    {
        ivUserIcon.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
				/*
				 * 需要新增接口，用来修改头像
				 */
                showDialog();
            }
        });
        //二维码扫描
//		btnScan.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent qrIntent = new Intent(MyCountActivity.this, CaptureActivity.class);
//				startActivityForResult(qrIntent, ACTIVITY_FOR_QR);
//			}
//		});
        layoutUserName.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //进入到用户名称修改
                Intent intent = new Intent(MyCountActivity.this, ModUserNameActivity.class);
                intent.putExtra("userInfo", resBundle);
                startActivityForResult(intent, ACTIVITY_FOR_NAME);
            }
        });
        layoutCommitAddr.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //进入到通讯地址修改
                Intent intent = new Intent(MyCountActivity.this, ModComminAddrActivity.class);
                intent.putExtra("userInfo", resBundle);
                startActivityForResult(intent, ACTIVITY_FOR_ADDR);
            }
        });
        layoutMail.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                //进入到邮箱修改
                Intent intent = new Intent(MyCountActivity.this, ModMailActivity.class);
                intent.putExtra("userInfo", resBundle);
                startActivityForResult(intent, ACTIVITY_FOR_MAIL);
            }
        });
        layoutModifyCode.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //进入到密码修改
                Intent intent = new Intent(MyCountActivity.this, ModUserKeyActivity.class);
                intent.putExtra("userInfo", resBundle);
                startActivityForResult(intent, ACTIVITY_FOR_CODE);
            }
        });
    }
    /**
     * 方法名称：	showDialog
     * 方法描述：	修改用户头像
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-9下午7:47:18
     */
    private void showDialog()
    {
        layoutIconView = LayoutInflater.from(MyCountActivity.this).inflate(R.layout.dialog_change_icon, null);
        tvGallary = (TextView)layoutIconView.findViewById(R.id.tv_from_gallary);
        tvCamera = (TextView)layoutIconView.findViewById(R.id.tv_from_camera);

        iconDialog = new CustomDialog(MyCountActivity.this)
                .builder()
                .setTitle("设置头像")
                .setView(layoutIconView)
                .setNegativeButton("取消", new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {}
                });
        iconDialog.show();
        tvGallary.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, FROM_CONTENT);
                iconDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                file = new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, TAKE_PHOTO);
                iconDialog.dismiss();
            }
        });
    }
    /**
     * 方法名称：	getPhotoFileName
     * 方法描述：	头像命名
     * 参数：			@return
     * 返回值类型：	String
     * 创建时间：	2015-3-27上午9:11:46
     */
    private String getPhotoFileName()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        //照片的后缀格式会影响裁剪的时候是否旋转
        return dateFormat.format(date)+".jpg";
    }
    private Handler handler = new Handler()
    {
        /* (非Javadoc)
         * 方法名称：	handleMessage
         * 方法描述：	TODO
         * 重写部分：	@see android.os.Handler#handleMessage(android.os.Message)
         */
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case GET_INFO_SUCCESS:
                    showUserInfo();
                    break;
                case GET_INFO_FAIL:
                    checkUserInfo();
                    break;
                case GET_INFO_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试");
                    break;
                case UPLOAD_SUCCESS:
                    checkUserInfo();
                    break;
                case UPLOAD_FAIL:
                    //重新提交
                    uploadIcon();
                    break;
                case UPLOAD_ERROR:
                    dialog.dismiss();
                    showInfo("程序异常，请稍后重试");
                    break;
                case GET_USER_ICON_SUCCESS:
                    dialog.dismiss();
                    ivUserIcon.setImageBitmap(Helper.getRoundBitmap(user.getIcon()));
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /**
     * 方法名称：	showUserInfo
     * 方法描述：	显示用户信息
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-3-27上午11:42:35
     */
    private void showUserInfo()
    {
        layoutUserCenter.setVisibility(View.VISIBLE);
        user.setName(resBundle.getString("name"));
        user.setEmail(resBundle.getString("mail"));
        user.setAddress(resBundle.getString("commit_addr"));
        if ( !user.getIconUrl().equals(resBundle.getString("iconUrl")) )
        {
            user.setIconUrl(resBundle.getString("iconUrl"));
            //加载头像
            GetUserIconTask iconTask = new GetUserIconTask(handler);
            iconTask.execute(user);
        }
        else
        {
            ivUserIcon.setImageBitmap(Helper.getRoundBitmap(user.getIcon()));
            dialog.dismiss();
        }
        tvUserName.setText(user.getName());
        tvCommitAddr.setText(user.getAddress());
        tvMail.setText(user.getEmail());
    }

    /* (非Javadoc)
     * 方法名称：	onActivityResult
     * 方法描述：	TODO
     * 重写部分：	@see android.app.Activity#onActivityResult(int, int, android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case ACTIVITY_FOR_NAME:
                if ( resultCode == RESULT_OK )
                {
                    checkUserInfo();
                }
                break;
            case ACTIVITY_FOR_ADDR:
                if ( resultCode == RESULT_OK )
                {
                    checkUserInfo();
                }
                break;
            case ACTIVITY_FOR_MAIL:
                if ( resultCode == RESULT_OK )
                {
                    checkUserInfo();
                }
                break;
            case ACTIVITY_FOR_CODE:
                if ( resultCode == RESULT_OK )
                {
                    checkUserInfo();
                }
                break;
            case ACTIVITY_FOR_QR:
                if ( resultCode == RESULT_OK )
                {
                    //判断二维码中的数据是网址，字符串还是其他
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    checkResult(scanResult);
                }
                break;
            case FROM_CONTENT:
                if ( resultCode == RESULT_OK )
                {
                    zoomPhoto(data.getData(), photo_size);
                }
                break;
            case TAKE_PHOTO:
                if ( resultCode == RESULT_OK )
                {
                    zoomPhoto(Uri.fromFile(file), photo_size);
                }
                break;
            case CROP_PHOTO:
                if ( resultCode == RESULT_OK )
                {
                    saveIcon(data);
                }
                break;
            default:
                break;
        }
    }
    /**
     * 方法名称：	checkResult
     * 方法描述：	判别二维码数据类型
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-7上午8:53:56
     */
    private void checkResult(String str)
    {
        URL url = null;
        try
        {
            url = new URL(str);
            openUrlPath(str);
        }
        catch ( MalformedURLException e )
        {
            //抛异常就不是网址,显示内容
            Intent intent = new Intent(MyCountActivity.this, ScanResultActivity.class);
            intent.putExtra("result", str);
            startActivity(intent);
        }
    }
    /**
     * 方法名称：	openUrlPath
     * 方法描述：	打开网址
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-7上午9:35:56
     */
    private void openUrlPath(String str)
    {
        Uri uri = Uri.parse(str);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    /**
     * 方法名称：	zoomPhoto
     * 方法描述：	裁剪图片
     * 参数：			@param uri
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午5:13:36
     */
    private void zoomPhoto(Uri uri, int size)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
//		 aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//		 outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_PHOTO);
    }
    /**
     * 方法名称：	saveIcon
     * 方法描述：	保存图片
     * 参数：			@param data
     * 返回值类型：	void
     * 创建时间：	2015-3-26下午5:19:44
     */
    private void saveIcon(Intent data)
    {
        Bundle extras = data.getExtras();
        if ( extras != null )
        {
            bitmap = extras.getParcelable("data");
			/*
			 * 上传图片资源
			 */
            dialog = ApplicationUtil.getLoadingDialog(this, "正在更新...");
            dialog.show();
            uploadIcon();
        }
    }
    /**
     * 方法名称：	uploadIcon
     * 方法描述：	上传头像到服务器
     * 参数：
     * 返回值类型：	void
     * 创建时间：	2015-4-9下午7:57:21
     */
    private void uploadIcon()
    {
        NetCommunicationUtil.httpConnect(
                getNewUserInfos(),
                NetElectricsConst.METHOD_SET_COUNT,
                NetElectricsConst.STYLE_RES,
                new NetCommunicationUtil.HttpCallbackListener()
                {
                    @Override
                    public void onFinish(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        if ( bundle.getInt("resCode") == 1)
                        {
                            message.what = UPLOAD_SUCCESS;
                        }
                        else
                        {
                            message.what = UPLOAD_FAIL;
                        }
                        handler.sendMessage(message);
                    }
                    @Override
                    public void onError(Bundle bundle)
                    {
                        Message message = handler.obtainMessage();
                        message.what = UPLOAD_ERROR;
                        handler.sendMessage(message);
                    }
                });
    }
    /**
     * 方法名称：	getNewUserInfos
     * 方法描述：	获取http参数集
     * 参数：			@return
     * 返回值类型：	List<PropertyInfo>
     * 创建时间：	2015-3-18下午4:59:24
     */
    private List<PropertyInfo> getNewUserInfos()
    {
        List<PropertyInfo> infos = new ArrayList<PropertyInfo>();

        PropertyInfo phone = new PropertyInfo();
        phone.setName(NetElectricsConst.USER_NAME);
        phone.setValue(((User) UserManager.getInstance().get(0)).getPhone());

        PropertyInfo name = new PropertyInfo();
        name.setName(NetElectricsConst.NAME);
        name.setValue(resBundle.get("name"));

        PropertyInfo areaid = new PropertyInfo();
        areaid.setName(NetElectricsConst.AREA_ID);
        areaid.setValue(resBundle.get("areaid"));

        PropertyInfo areaname = new PropertyInfo();
        areaname.setName(NetElectricsConst.AREA_NAME);
        areaname.setValue(resBundle.get("areaname"));

        PropertyInfo address = new PropertyInfo();
        address.setName(NetElectricsConst.ADDRESS);
        address.setValue(resBundle.get("commit_addr"));

        PropertyInfo mail = new PropertyInfo();
        mail.setName(NetElectricsConst.MAIL_BOX);
        mail.setValue(resBundle.get("mail"));

        PropertyInfo mobilephone = new PropertyInfo();
        mobilephone.setName(NetElectricsConst.MOBILE);
        mobilephone.setValue(resBundle.get("mobilephone"));

        PropertyInfo qq = new PropertyInfo();
        qq.setName(NetElectricsConst.QQ);
        qq.setValue(resBundle.get("qq"));

        PropertyInfo weixin = new PropertyInfo();
        weixin.setName(NetElectricsConst.WEIXIN);
        weixin.setValue(resBundle.get("weixin"));

        PropertyInfo image = new PropertyInfo();
        image.setName(NetElectricsConst.IMAGE);
        image.setValue(Helper.bitmap2String(bitmap));

        PropertyInfo token = new PropertyInfo();
        token.setName(NetElectricsConst.TOKEN);
        token.setValue("");

        infos.add(phone);
        infos.add(name);
        infos.add(areaid);
        infos.add(areaname);
        infos.add(address);
        infos.add(mail);
        infos.add(mobilephone);
        infos.add(qq);
        infos.add(weixin);
        infos.add(image);
        infos.add(token);

        return infos;
    }
}