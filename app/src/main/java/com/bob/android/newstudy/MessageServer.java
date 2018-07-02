package com.bob.android.newstudy;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * @package com.bob.android.newstudy
 * @fileName MessageServer
 * @Author Bob on 2018/6/13 6:33.
 * @Describe TODO
 */

public class MessageServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // 绑定

        return null;
    }

   /* private final UserAidel.Stub mBinder   = new UserAidel.Stub() {
        @Override
        public String getUsername() throws RemoteException {
            return "Bob";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "bobadmin";
        }
    };
*/
}
