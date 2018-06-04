package com.h.fileinput.Glin;

import android.os.Environment;
import android.util.Log;
import android.util.Printer;
import android.widget.ImageView;

import org.loader.glin.Glin;
import org.loader.glin.annotation.PUT;
import org.loader.glin.cache.DefaultCacheProvider;
import org.loader.glin.chan.GlobalChanNode;
import org.loader.glin.chan.LogChanNode;
import org.loader.glin.helper.LogHelper;
import org.loader.okclient.OkClient;

/**
 * Created by pc on 2018/6/4.
 */

public class CustomGlin {
    private static CustomGlin INSTANCE;

    public synchronized static CustomGlin getINSTANCE(){
        if(null == INSTANCE){
            synchronized (CustomGlin.class){
                if(null == INSTANCE){
                    INSTANCE = new CustomGlin();
                }
            }
        }
        return INSTANCE;
    }

    private static final LogHelper.LogPrinter logPrinter = new LogHelper.LogPrinter() {
        @Override
        public void print(String tag, String content) {
            Log.d(tag,content);
        }
    };

    //request log
    LogChanNode beforeLog = new LogChanNode(true, logPrinter);

    //response log
    LogChanNode afterLog = new LogChanNode(true,logPrinter);

    //Global chan node before request
    GlobalChanNode before = new GlobalChanNode(beforeLog);
    //Global chan node after response
    GlobalChanNode after = new GlobalChanNode(afterLog);

    public Glin getGlin(String baseUrl){
        Glin glin = new Glin.Builder().client(new OkClient())
//                .baseUrl("https://api.douban.com/v2/")
                .baseUrl(baseUrl)
                .globalChanNode(before,after)
                .parserFactory(new FastJsonParserFactory())//自定义
//                .cacheProvider(new DefaultCacheProvider(Environment.getExternalStorageDirectory()+"test",200))
                .timeout(10000).build();
        return glin;
    }
}
