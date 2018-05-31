package com.h.fileinput;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.h.fileinput.db.greenDao.DaoMaster;
import com.h.fileinput.db.greenDao.DaoSession;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orm.SugarContext;

/**
 * Created by Administrator on 2017/3/17 0017.
 */
public class MyApp extends Application {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    public static SQLiteDatabase db;
    public static final String DB_NAME = "fileinput.db";
    private static Context mContext;
    public static MyApp instance;

    public static MyApp getInstance(){
        if(null == instance){
            instance = new MyApp();
        }
        return instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
        getDaoMaster();
        initImageLoader(mContext);
        SugarContext.init(getApplicationContext());
    }

    public void onTerminate() {
        SugarContext.terminate();
        super.onTerminate();
    }

    private static DaoMaster getDaoMaster(){
        if(daoMaster == null){
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(mContext,DB_NAME,null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    public static DaoSession getDaoSession(){
        if(daoSession == null){
            if(daoMaster == null){
                daoMaster = getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    /**
     * 初始化ImageLoader
     *
     * @param context
     * @date 2017-3-16 上午11:10:28
     * @fileTag 方法说明：
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize((int) (2 * 1024 * 1024))
                .build();
        ImageLoader.getInstance().init(config);
    }
}
