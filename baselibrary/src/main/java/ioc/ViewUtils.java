package ioc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Bob on 2018/4/11.
 */

public class ViewUtils {

    public static void inject(Activity activity){
        inject(new ViewFinder(activity),activity);
    }

    public static void inject(View view){
        inject(new ViewFinder(view),view);
    }

    public static void inject(View view,Object object){
        inject(new ViewFinder(view),object);
    }

    //兼容上面三个方法  object:反射需要执行的类
    private static void inject(ViewFinder viewFinder,Object object){
        injectFiled(viewFinder,object);
        injectEvent(viewFinder,object);
    }

    /**
     * 注入属性
     * @param viewFinder
     * @param object
     * */
    private static void injectFiled(ViewFinder viewFinder, Object object) {
        //① 获取类里面所有的属性
        Class<?> clazz = object.getClass();
        //获取所有的属性，包括公有和私有的
        Field[] fields = clazz.getDeclaredFields();
        //② 获取ViewById注解里面的value值
        for(Field field :fields){
            ViewById viewById = field.getAnnotation(ViewById.class);
            if(null != viewById){
                //获取注里面的ID值
                int viewId = viewById.value();
                //③ findViewById找到View
                View view = viewFinder.findViewById(viewId);
                if(null != view){
                    field.setAccessible(true);//能够注入所有的修饰符 private public
                    //④ 动态注入找到的view
                    try {
                        field.set(object,view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * 事件注入
     * */
    private static void injectEvent(ViewFinder viewFinder, Object object) {
        //1 获取类里面所有的方法
        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        //2 获取OnClick里面的value值
        for(Method method:methods){
            OnClick onClick = method.getAnnotation(OnClick.class);
            if(null != onClick){
                int[] viewIds = onClick.value();
                for(int viewId : viewIds){
                    //3 findViewById找到View
                    View view = viewFinder.findViewById(viewId);
                    boolean isCheckNet = method.getAnnotation(CheckNetUtil.class)!= null;
                    if(null != view){
                        //4 setOnClickListener
                        view.setOnClickListener(new DeclaredOnClickListener(method,object,isCheckNet));
                    }
                }
            }
        }


    }

    private static class DeclaredOnClickListener implements View.OnClickListener{

        private Object mObject;
        private Method mMethod;
        private boolean isCheckNet;

        public DeclaredOnClickListener(Method method, Object object,boolean isCheckNet) {
            this.mObject = object;
            this.mMethod = method;
            this.isCheckNet = isCheckNet;
        }

        @Override
        public void onClick(View view) {
            if(isCheckNet){
                if(!networkAvailable(view.getContext())){
                    Toast.makeText(view.getContext(),"网络不通畅，请检查网络",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //点击的时候会调用这个方法
            try {
                //私有的和共有的方法都能调用
                mMethod.setAccessible(true);
                //5 反射执行方法
                mMethod.invoke(mObject,view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                try {
                    mMethod.invoke(mObject,null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                } catch (InvocationTargetException e1) {
                    e1.printStackTrace();
                }
            }

        }
    }

    private static boolean networkAvailable(Context context){
        try{
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if(null != activeNetworkInfo && activeNetworkInfo.isConnected()){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
