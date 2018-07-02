package com.bob.android.newstudy;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import http.HttpUtils;
import http.ResponseCode;
import http.ResponseResult;
import ioc.OnClick;
import ioc.ViewById;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bob.android.framelibrary.base.BaseSkinActivity;
import com.bob.android.framelibrary.view.DefaultNavigationBar;
import com.bob.android.framelibrary.http.HttpCallback;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import application.BaseApplication;
import model.JokyEntity;
import model.UserLoginEntity;
import model.Weather;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import service.GuardService;
import service.JobWakeUpService;
import service.MessageService;
import skin.SkinManager;
import util.PermissionsCheckerUtil;

import static http.ResponseCode.RESPONSE_CODE_FAILURE;


public class MainActivity extends BaseSkinActivity {

    @ViewById(R.id.tv)
    TextView mTv;
    @ViewById(R.id.edt_user_code)
    EditText mEdtUserCode;
    @ViewById(R.id.edt_password)
    EditText mEdtPassword;
    @ViewById(R.id.location)
    TextView tvLocation;
    @ViewById(R.id.img_location)
    ImageView mImgLocation;
    @ViewById(R.id.btn_album)
    Button mbtnAlbum;
    @ViewById(R.id.img)
    ImageView mImg;
    @ViewById(R.id.imageView)
    SubsamplingScaleImageView imageView;

    // 客户端一定要获取aidl的实例
    // 客户端
//    private UserAidel mUserAidel ;

    // 客户端
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接好了
//            mUserAidel = UserAidel.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
        }
    };

    public void getUserName(View view){
//        try {
//            Toast.makeText(this,mUserAidel.getUsername(),Toast.LENGTH_LONG).show();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    public void getPwd(View view){
//        try {
//            Toast.makeText(this,mUserAidel.getUserPwd(),Toast.LENGTH_LONG).show();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * @Fields PERMISSION_REQUEST_CODE:请求权限的返回CODE
     */
    public static final int PERMISSION_REQUEST_CODE = 10001;

    private Context mContext;
    String longitude;
    String latitude;

    private static final String weatherUrl = "http://v.juhe.cn/weather/index";
    private static final String jokeUrl = "http://v.juhe.cn/joke/content/text.php";
//    private static final String weatherUrlKey = "1b28029059ed45d2bd553884224ef66d";
    private static final String weatherUrlKey ="1b28029059ed45d2bd553884224ef66d";
    private static final String jokeKey = "9f73bfd55e1a379bcc36b6629c07f4c3";

    private float lastLatitude,lastLongitude,nowLatitude,nowLongitude;

    private TimerTask timerTask ;
    private Timer timer;

    //所需权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private final int GET_LOCATION = 1111;
    private final int CHANGE_LOCATION = 1112;
    private PermissionsCheckerUtil mPermissionsCheckerUtil; //权限检测器

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GET_LOCATION:
                    String dexcribe = (String) msg.obj;
                    Snackbar.make(mTv,"已经超出了距离！当前经纬度："+ dexcribe,Snackbar.LENGTH_LONG).show();
                    break;
                case CHANGE_LOCATION:
                    String changeLocation = (String)msg.obj;
                    tvLocation.setText(changeLocation);
                    break;
            }
        }
    };


    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        mContext = this;
    }

    private void initPermission() {
        if (mPermissionsCheckerUtil.lacksPermissions(PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        }
    }
    /*private void fixDexBug() {
        File fixFile = new File(Environment.getExternalStorageDirectory(),"fix.dex");
        if(fixFile.exists()){
            FixDexManager fixDexManager = new FixDexManager(this);
            try {
                fixDexManager.fixDex(fixFile.getAbsolutePath());
                Toast.makeText(mContext,"修复成功",Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mContext,"修复失败",Toast.LENGTH_LONG).show();
            }
        }
    }*/

    private void aliFix() {
        File fixFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
        if (fixFile.exists()) {
            try {
                BaseApplication.mPatchManager.addPatch(fixFile.getAbsolutePath());
                Toast.makeText(mContext, "修复成功", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "修复失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void initVariables() {
        //        IDaoSupport<Student> daoSupport = DaoSupportFactory.getFactory().getDao(Student.class);
//        daoSupport.insert(new Student("Alex", "23"));
        mPermissionsCheckerUtil = new PermissionsCheckerUtil(mContext);
        initPermission();
        String location = getLngAndLat(mContext);
        String[] locations = location.split("\\,");
        nowLongitude = lastLongitude = Float.valueOf(locations[0]);
        nowLatitude = lastLatitude = Float.valueOf(locations[1]);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                String location = getLngAndLat(mContext);
                String[] locations = location.split("\\,");
                nowLongitude = Float.valueOf(locations[0]);
                nowLatitude = Float.valueOf(locations[1]);
                ifChangeLocation(lastLongitude,lastLatitude,nowLongitude,nowLatitude);
            }
        };
        timer = new Timer(true);
        timer.schedule(timerTask,1000,1000);

//        Snackbar.make(mTv,location,Snackbar.LENGTH_LONG).show();
//        fixDexBug();
        //aliFix();
    }

    @Override
    protected void initTitle() {
        DefaultNavigationBar defaultNavigationBar = new DefaultNavigationBar
//                .Builder(this,(ViewGroup) findViewById(R.id.view_group))
                .Builder(this)
                .setTitle("首页")
                .setRightText("搜索")
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(mContext, "点击了右边文字", Toast.LENGTH_LONG).show();
                    }
                })
                .builder();
    }

    @Override
    protected void initView() {
        imageView.setImage(ImageSource.resource(R.mipmap.img9195));
    }

    @Override
    protected void initData() {
        startService(new Intent(this, MessageService.class));
        startService(new Intent(this, GuardService.class));
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            // 必须大于等于5.0
            startService(new Intent(this,JobWakeUpService.class));

        }
        /*startService(new Intent(this,MessageServer.class));
        // 客户端
       *//* Intent intent = new Intent();
        intent.setAction("android.intent.action.RESPOND_VIA_MESSAGE");
        intent.setPackage("com.bob.android.newstudy.adil");*//*
       Intent intent = new Intent(this,MessageServer.class);
       bindService(intent,mServiceConnection,Context.BIND_AUTO_CREATE);*/
    }

    //    @CheckNetUtil
    @OnClick(R.id.tv)
    public void tvClick(View view) {
       /* Toast.makeText(mContext,2/0+"测试",Toast.LENGTH_LONG).show();*/
        dialog.AlertDialog dialog = new dialog.AlertDialog.Builder(this)
                .setText(R.id.btn_click, "评论")
                .setText(R.id.tv_dialog_title, "评论分享")
                .setUserAnimation(R.style.dialog_from_bottom_anim)
                .setContentView(R.layout.layout_dialog)
//                .setFullWidth()
//                .fromBottom(true)
                .show();


        final EditText mEdtText = dialog.getView(R.id.edt_input);
        dialog.setOnClickListener(R.id.img_jingdong, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了京东", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setOnClickListener(R.id.img_dangdang, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "点击了当当", Toast.LENGTH_LONG).show();
            }
        });

        dialog.setOnClickListener(R.id.btn_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mEdtText.getText().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @OnClick(R.id.btn_regist)
    private void registUser(View view) {
        String userCode = mEdtUserCode.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        HttpUtils.with(this).url("http://172.20.25.153:8080/regist").post()
                .addParam("userCode", userCode)
                .addParam("password", password)
                .execute(new HttpCallback<ResponseResult>() {
                    @Override
                    public void onSuccess(ResponseResult result) {
                        if(result.getResponseCode()== ResponseCode.RESPONSE_CODE_SUCCESS){
                            String resultStr = result.getContent();
                            UserLoginEntity userLoginEntity = JSON.parseObject(resultStr,UserLoginEntity.class);
                            Snackbar.make(mTv, result.getMessage()+"   " + userLoginEntity.getUserCode(), Snackbar.LENGTH_LONG).show();
                        }else if(result.getResponseCode() == RESPONSE_CODE_FAILURE){
                            Snackbar.make(mTv, result.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                /*.execute(new HttpCallback<String>() {

                    @Override
                    public void onSuccess(String result) {
                        UserLoginEntity userLoginEntity = JSON.parseObject(result, UserLoginEntity.class);
                        if (null != userLoginEntity && null != userLoginEntity.getUserCode()) {
                            Snackbar.make(mTv, "注册成功：" + userLoginEntity.getUserCode(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(mTv, "注册失败！", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });*/
    }

    @OnClick(R.id.btn_find)
    private void findUserByName(View view){
        String userCode = mEdtUserCode.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        HttpUtils.with(this).url("http://172.20.25.153:8080/find_by_name").post()
                .addParam("name", userCode)
//                .addParam("password", password)
                .execute(new HttpCallback<ResponseResult>() {
                    @Override
                    public void onSuccess(ResponseResult result) {
                        if(result.getResponseCode()== ResponseCode.RESPONSE_CODE_SUCCESS){
                            String resultStr = result.getContent();
                            List<Student> studentList = JSONArray.parseArray(resultStr,Student.class);
                            Snackbar.make(mTv, result.getMessage()+"   " + studentList.get(0).getName(), Snackbar.LENGTH_LONG).show();
                        }else if(result.getResponseCode() == RESPONSE_CODE_FAILURE){
                            Snackbar.make(mTv, result.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }
    @OnClick(R.id.img_location)
    private void setLocation(View view){
        /*String location = getLngAndLat(mContext);
        String[] locations = location.split("\\,");
        lastLongitude = Float.valueOf(locations[0]);
        lastLatitude = Float.valueOf(locations[1]);*/
        uploadImg();
    }

    @OnClick(R.id.btn_text_http_util)
    private void httpRequestClick(View view) {
        HttpUtils.with(this).url("http://172.20.25.153:8080/hello")// 路径 apk参数等都要放到jni里面去以防被反编译
//        HttpUtils.with(this).url("http://192.168.0.104:8080/hello").cache(true)
//        HttpUtils.with(this).url("http://172.20.25.153:8080/delete_by_name").post().cache(false)
//                .addParam("name","Kelly")
//        HttpUtils.with(this).url("http://172.20.25.153:8080/insert").post()
//        HttpUtils.with(this).url("http://192.168.0.104:8080/insert")
//        HttpUtils.with(this).url("http://172.20.25.153:8080/find_by_id")
//           .addParam("name","Alex")
//                .addParam("age","25").post()
        /*String userCode = mEdtUserCode.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();*/
//        HttpUtils.with(this).url("http://172.20.25.153:8080/login").post()
//        HttpUtils.with(this).url("http://172.20.25.153:8080/file/download")
//        HttpUtils.with(this).url("http://172.20.25.153:8088/app/quert")

               /* .addParam("userCode", userCode)
                .addParam("password", password)
                .addParam("longitude",longitude)
                .addParam("latitude",latitude)*/

              .execute(new HttpCallback<ResponseResult>() {
                  @Override
                  public void onSuccess(ResponseResult result) {
                      int responseCode = result.getResponseCode();
                        String resultStr = result.getContent();
                        List<Student> studentList = JSONArray.parseArray(resultStr,Student.class);
                        int size = studentList.size();
                  }

                  @Override
                  public void onError(Exception e) {

                  }
              });
               /* .execute(new HttpCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        UserLoginEntity userLoginEntity = JSON.parseObject(result, UserLoginEntity.class);
                        if (null != userLoginEntity && null != userLoginEntity.getUserCode()) {
                            Snackbar.make(mTv, "登录成功：" + userLoginEntity.getUserCode(), Snackbar.LENGTH_LONG).show();
                        } else {
                            Snackbar.make(mTv, "登录失败！", Snackbar.LENGTH_LONG).show();
                        }
                        *//*List<Student> students = com.alibaba.fastjson.JSONArray.parseArray(result,Student.class);
                        int size = students.size();*//*

                    }

                    @Override
                    public void onError(Exception e) {
                        Snackbar.make(mTv, "网络连接失败！" + e, Snackbar.LENGTH_LONG).show();
                    }
                });*/
    }

    private void uploadImg() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        File file = new File(Environment.getExternalStorageDirectory() + "/uploadImg.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("img", "uploadImg.jpg",
                        RequestBody.create(MediaType.parse("img/png"), file));
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url("http://172.20.25.153:8080/file/uploadFile")
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("TAG", "成功" + response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    /**
     * 获取经纬度
     *
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    private String getLngAndLat(Context context) {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //从gps获取经纬度
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {//当GPS信号弱没获取到位置的时候又从网络获取
                    return getLngAndLatWithNetwork();
                }

        } else {
            //从网络获取经纬度
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }
        }
        return longitude + "," + latitude;
    }

    //从网络获取经纬度
    @SuppressLint("MissingPermission")
    public String getLngAndLatWithNetwork() {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Message msg = handler.obtainMessage();
                msg.what = CHANGE_LOCATION;
                msg.obj = longitude + "," + latitude;
                handler.sendMessage(msg);
            }
            return longitude + "," + latitude;
    }


    LocationListener locationListener = new LocationListener() {

        // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        // Provider被enable时触发此函数，比如GPS被打开
        @Override
        public void onProviderEnabled(String provider) {

        }

        // Provider被disable时触发此函数，比如GPS被关闭
        @Override
        public void onProviderDisabled(String provider) {

        }

        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        @Override
        public void onLocationChanged(Location location) {
        }
    };


    public  void ifChangeLocation(float lastLongitude,float lastLatitude,float nowLongitude,float nowLatitude) {
        //先计算查询点的经纬度范围
        double r = 6371;//地球半径千米
        double dis = 0.005;//2米距离
        double dlng = 2 * Math.asin(Math.sin(dis / (2 * r)) / Math.cos(lastLatitude * Math.PI / 180));
        dlng = dlng * 180 / Math.PI;//角度转为弧度
        double dlat = dis / r;
        dlat = dlat * 180 / Math.PI;
        double minlat = lastLatitude - dlat;
        double maxlat = lastLatitude + dlat;
        double minlng = lastLongitude - dlng;
        double maxlng = lastLongitude + dlng;

        if(maxlng - nowLongitude < 0 || maxlat - nowLatitude <0){
            Message msg = handler.obtainMessage();
            msg.what = GET_LOCATION;
            msg.obj = nowLongitude + "," + nowLatitude;
            handler.sendMessage(msg);

        }
    }

    @OnClick(R.id.btn_album)
    private void turnToAlbum(View view){
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    /**
     * 换肤
     * @param view
     */
    @OnClick(R.id.skin_change)
    private void changeSkin(View view){
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                +File.separator+"red.skin";
        int result = SkinManager.getInstance().loadSkin(skinPath);
    }

    /**
     * 默认皮肤
     * @param view
     */
    @OnClick(R.id.skin_default)
    private void skinDefault(View view){
        int result = SkinManager.getInstance().restoreDefault();
    }

    @OnClick(R.id.btn_skip)
    private void skipPage(View view){
       /* Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);*/
       /* HttpUtils.with(this).url(weatherUrl)
                .get()
                .addParam("cityname","长沙")
                .addParam("dtype","json")
                .addParam("format",2)
                .addParam("key",weatherUrlKey).execute(new HttpCallback<Weather>() {
            @Override
            public void onSuccess(Weather result) {
                if(null != result){
                    result.getResult().getToday();
                }
            }

            @Override
            public void onError(Exception e) {

            }
        });*/
       HttpUtils.with(this).url(jokeUrl)
               .addParam("page",1)
               .addParam("pagesize",20)
               .addParam("key",jokeKey)
               .execute(new HttpCallback<JokyEntity>() {
                   @Override
                   public void onSuccess(JokyEntity result) {
                        if(null != result){
                            Toast.makeText(mContext,"请求成功",Toast.LENGTH_LONG).show();
                        }
                   }

                   @Override
                   public void onError(Exception e) {

                   }
               });
    }

    private void changeSkin(){
        try {
            // 读取本地一个.skin文件里面的资源
            Resources superRes = getResources();
            //创建AssetManager
            AssetManager asset = AssetManager.class.newInstance();

            //添加本地下载好的资源皮肤
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
//            method.setAccessible(true);
            method.invoke(asset,Environment.getDownloadCacheDirectory().getAbsolutePath()+
            File.separator+"red.skin");
            Resources resources = new Resources(asset,superRes.getDisplayMetrics(),
                    superRes.getConfiguration());
            // 获取资源id
            int drawableId = resources.getIdentifier("img_src","drawable","com.hc.skinpugin");
            Drawable drawable = resources.getDrawable(drawableId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
