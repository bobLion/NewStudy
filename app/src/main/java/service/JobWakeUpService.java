package service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import java.util.List;

/**
 * @package service
 * @fileName JobWakeUpService
 * @Author Bob on 2018/6/23 9:49.
 * @Describe 5.0以上才会有
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobWakeUpService extends JobService{

    private final int JOB_WAK_UP_ID = 1;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //开启一个轮询
        JobInfo.Builder jobBuilder = new JobInfo.Builder(JOB_WAK_UP_ID,
                new ComponentName(this,JobWakeUpService.class));
        jobBuilder.setPeriodic(2000);

        JobScheduler jobScheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobBuilder.build());

        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        // 开启定时任务，定时轮询，看MessageService有没有被杀死
        // 如果被杀死的话，就启动。轮询onStartJob
        boolean messageServerAlive = isServiceAlive(MessageService.class.getName());
        if(!messageServerAlive){
            startService(new Intent(this,MessageService.class));
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     * 判断服务是否正在运行
     * @param serviceName
     * @return
     */
    private boolean isServiceAlive(String serviceName){
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = activityManager.getRunningServices(100);
        if(myList.size() <= 0){
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String pName = myList.get(i).service.getClassName().toLowerCase();
            if(pName.equals(serviceName)){
                isWork = true;
                break;
            }
        }
        return isWork;
    }

}
