package com.h.fileinput.AnalyzeHtml;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ListView;

import com.h.fileinput.R;
import com.h.fileinput.db.greenBean.CookBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* 用jsoup解析html
* */
public class AnalyzeHtmlActivity extends AppCompatActivity {
    private static final String TAG = "AnalyzeHtmlActivity";
    GridView gv_menu;
    CardViewAdapter adapter;
    Context mContext;
    List<CookBean> cookBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyze_html);

        mContext = AnalyzeHtmlActivity.this;
        cookBeanList = new ArrayList<CookBean>();
        initDate();
        gv_menu = (GridView) findViewById(R.id.lv_menu);
//        adapter = new CardViewAdapter();
    }

    public void initDate(){
        final List<CookBean> cooks = new ArrayList<CookBean>();
        //获取菜谱列表
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //从一个URL中加载一个Document对象
                    Document doc = Jsoup.connect("http://home.meishichina.com/recipe.html").get();
                    //获取菜谱的名称和url
                    Elements ulElements = doc.select("div#recipeindex_living ul li");

                    for(Element element:ulElements){
                        Log.d(TAG,"ul="+element.toString());
                        CookBean bean = new CookBean();
                        bean.setTitle(element.child(0).attr("title"));
                        bean.setUrl(element.child(0).attr("href"));
                        bean.setCreateUser(element.child(1).attr("title"));
                        bean.setUserUrl(element.child(1).attr("href"));
                        cooks.add(bean);
                    }
                    //获取菜谱的图片
                    Elements imgEls = doc.select("div#recipeindex_living ul li a i img");
                    for(int i=0;i<imgEls.size();i++){
                        Element imgEl = imgEls.get(i);
                        CookBean cookBean = cooks.get(i);
                        cookBean.setImgPath(imgEl.attr("data-src"));
                        Log.d(TAG,"cookBean="+cookBean.getTitle()+","+cookBean.getImgPath());
                    }
                    Message message = new Message();
                    message.what=0;
                    message.obj = cooks;
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    cookBeanList = (List<CookBean>) msg.obj;
                    adapter = new CardViewAdapter(mContext,cookBeanList);
                    gv_menu.setAdapter(adapter);
                    break;
            }
        }

    };
}
