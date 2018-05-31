package com.h.fileinput.EasyRecyclerView;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.h.fileinput.R;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CommonRecyclerActivity extends AppCompatActivity {
    String TAG = CommonRecyclerActivity.class.getSimpleName();
    Context mContext;

    RecyclerView rv_tea;
    SwipeRefreshLayout srl_tea;
    Button btn_add,btn_clear,btn_refresh;
    private Handler mHandler = new Handler();
    int page;

    List<Article> mLists;
    ArticleForRecyclerAdapter mArticleAdapter;
    int loadCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_recycler);

        mContext = this;
        page = 1;
        rv_tea = (RecyclerView) findViewById(R.id.rv_tea);
        srl_tea = (SwipeRefreshLayout) findViewById(R.id.srl_tea);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_refresh = (Button) findViewById(R.id.btn_refresh);
        mLists = new ArrayList<>();
        for(int i=0;i<15;i++){
            mLists.add(new Article("111","A33333333333333","1"));
        }

        rv_tea.setLayoutManager(new LinearLayoutManager(this));
//        rv_tea.setLayoutManager(new GridLayoutManager(this,5));

        mArticleAdapter = new ArticleForRecyclerAdapter(this);

        rv_tea.setAdapter(mArticleAdapter);
        mArticleAdapter.addAll(mLists);

        mArticleAdapter.setError(R.layout.view_error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"加载有误，点击后重新加载");
                Toast.makeText(mContext,"加载有误，点击后重新加载",Toast.LENGTH_LONG).show();
                refreshData();
            }
        });

        //设置加载更多
        mArticleAdapter.setMore(R.layout.view_progress, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadCount++;
                Log.d(TAG,"loadCount = "+loadCount);
                loadMore();
            }
        });

        mArticleAdapter.setNoMore(R.layout.view_non_more);

        mArticleAdapter.setOnItemClickListener(new RecyclerArrayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG,"当前数据为空，请下拉刷新");
                Toast.makeText(mContext, mLists.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        srl_tea.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMore();
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLists.clear();
                mArticleAdapter.clear();
                mArticleAdapter.stopMore();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srl_tea.setRefreshing(true);
                refreshData();
            }
        });

        mArticleAdapter.resumeMore();
    }

    private void loadMore(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLists.clear();
                if(loadCount == 1){
                    page = 1;
                }else{
                    page++;
                }

                if(page >3){
                    mArticleAdapter.stopMore();
                }
                for(int i=0;i<15;i++){
                    mLists.add(new Article("","article","2"));
                }
                mArticleAdapter.addAll(mLists);
            }
        },2000);
    }

    private void refreshData(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mArticleAdapter.pauseMore();
                page = 1;
                mLists.clear();
                for(int i=0;i<15;i++){
                    mLists.add(new Article("","C","1"));
                }

                mArticleAdapter.clear();
                mArticleAdapter.insertAll(mLists,0);
                srl_tea.setRefreshing(false);
            }
        },2000);
    }
}
