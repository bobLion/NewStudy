package service;

import android.app.Notification;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.bob.android.newstudy.ProcessConnection;

/**
 * @package service
 * @fileName GuardService
 * @Author Bob on 2018/6/23 9:08.
 * @Describe 守护进程 ，双进程通讯
 */

public class GuardService extends Service {


    private final  int GuardId = 1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提高线程的优先级
        startForeground(GuardId,new Notification());

        // 绑定建立连接
        bindService(new Intent(this,MessageService.class),myServiceConnection, Context.BIND_IMPORTANT);
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
            Toast.makeText(GuardService.this,"GuardService已经建立连接",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 断开连接
            startService(new Intent(GuardService.this,MessageService.class));
            bindService(new Intent(GuardService.this,MessageService.class),myServiceConnection, Context.BIND_IMPORTANT);
        }
    };
}
