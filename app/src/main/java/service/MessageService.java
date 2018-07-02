package service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.bob.android.newstudy.ProcessConnection;

/**
 * @package service
 * @fileName MessageService
 * @Author Bob on 2018/6/22 11:52.
 * @Describe
 */

public class MessageService extends Service {

    private int MessageId = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Log.e("TAG","等待接受消息");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提高线程的优先级
        startForeground(MessageId,new Notification());

        // 绑定建立连接
        bindService(new Intent(this,GuardService.class),myServiceConnection, Context.BIND_IMPORTANT);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ProcessConnection.Stub() {};
    }

    private ServiceConnection myServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 连接上
            Toast.makeText(MessageService.this,"MessageService已经建立连接",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            startService(new Intent(MessageService.this,GuardService.class));
            bindService(new Intent(MessageService.this,GuardService.class),myServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
