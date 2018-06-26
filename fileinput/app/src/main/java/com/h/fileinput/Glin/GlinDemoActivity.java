package com.h.fileinput.Glin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.h.fileinput.Glin.adapter.BookListAdapter;
import com.h.fileinput.Glin.bean.Book;
import com.h.fileinput.Glin.bean.BookEntity;
import com.h.fileinput.R;
import com.h.fileinput.widget.EmptyView;

import org.loader.glin.Callback;
import org.loader.glin.Glin;
import org.loader.glin.Result;
import org.loader.glin.call.Call;

import java.util.List;

public class GlinDemoActivity extends AppCompatActivity implements EmptyView.RetryListener {
    String baseUrl = "https://api.douban.com/v2/";
    BookListAdapter listAdapter;
    ListView lv_bookList;
    EmptyView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glin_demo);
        lv_bookList = (ListView) findViewById(R.id.lv_bookList);
        LinearLayout ll_panel = (LinearLayout) findViewById(R.id.ll_panel);
        emptyView = ViewHelper.f(this,R.id.emptyview);
        emptyView.bind(ll_panel).setRetryListener(this);
        commRequest();
    }

    public void commRequest(){
        Glin glin = CustomGlin.getINSTANCE().getGlin(baseUrl);
        UserApi api = glin.create(UserApi.class,getClass().getName());

        Call<List<Book>> call = api.list("畅销");
        call.enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Result<List<Book>> result) {
                if(result.isOK()){
                    listAdapter = new BookListAdapter(result.getResult(),GlinDemoActivity.this);
                    lv_bookList.setAdapter(listAdapter);
                }else{
                    Toast.makeText(GlinDemoActivity.this,"error",Toast.LENGTH_SHORT).show();
                    emptyView.errorNet();
                }
            }
        });


        //返回数据只有一条() 这个接口没法获取一条的返回数据
//        Glin glinOne = CustomGlin.getINSTANCE().getGlin(baseUrl);
//        UserApi apiOne = glinOne.create(UserApi.class,getClass().getName());
//        Call<BookEntity> callOne = apiOne.one("畅销");
        Call<BookEntity> callOne = api.one("畅销");
        callOne.enqueue(new Callback<BookEntity>(){

            @Override
            public void onResponse(Result<BookEntity> result) {
                if(result.isOK()){
                    Toast.makeText(GlinDemoActivity.this,"ok",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(GlinDemoActivity.this,"error",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void retry() {
//        Toast.makeText(GlinDemoActivity.this,"1111111",Toast.LENGTH_SHORT).show();
        commRequest();
    }
}
