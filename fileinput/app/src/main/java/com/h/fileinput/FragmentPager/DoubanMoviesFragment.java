package com.h.fileinput.FragmentPager;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h.fileinput.R;

public class DoubanMoviesFragment extends Fragment{

    private RecyclerView recyclerView;
    private Context mContext;
    private LinearLayoutManager manager;

    private String tag_id;
    private String equipment_type;
    private int cookbook_type = 6;
//    private SmartRefreshLayout refresh_layout;
    private ImageView img_back;
    private boolean isRefresh;
    private boolean isLoadMore;
    private int currentPage = 1;
    private TextView title;
    private String cookbook_tag_name;
    private RelativeLayout rl_top_title;

//    public DoubanMoviesFragment() {
//        AnnotionProcessor.of(this);
//    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_selected_recpe);
        mContext = this.getActivity();
//        handleIntent();
//        initView();
//        initListener();
//        initData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_selected_tab, null);
        initView(view);
        handleIntent();
//        initListener();
        initData();
        return view;
    }

    private void initListener() {
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
//        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                currentPage = 1;
//                isRefresh = true;
//                initData();
//            }
//        });

//        refresh_layout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                ++ currentPage;
//                isLoadMore = true;
//                initData();
//            }
//        });

//        adapter.setOnItemClickListener(new SelectedRecipeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View v, int position, String cookbook_id) {
//                //跳转菜谱详情页
//                Intent i = new Intent(mContext, RecipesDetailActivity.class);
//                i.putExtra("cookbook_id",cookbook_id);
//                startActivity(i);
//            }
//        });
    }

    public void initData() {
//        pobiRecipePresenter.getSelectedRecipes(cookbook_type,currentPage, equipment_type, 20, tag_id);
//        pobiRecipePresenter.getSelectedRecipes(cookbook_type,currentPage, "晶钻Q3", 20, tag_id);
        title.setText(cookbook_tag_name);
    }

    private void handleIntent() {
        tag_id = getArguments() != null ? getArguments().getString("tag_id") : "";
        equipment_type = getArguments() != null ? getArguments().getString("equipment_type") : "";
        cookbook_tag_name = getArguments() != null ? getArguments().getString("cookbook_tag_name") : "";
        Log.e("NewFood","1111111tag_id="+tag_id+","+equipment_type+","+cookbook_tag_name);
    }

    private void initView(View view) {
        title = (TextView) view.findViewById(R.id.tv_tabName);
//        img_back = (ImageView) view.findViewById(R.id.img_back);
//        rl_top_title = view.findViewById(R.id.rl_top_title);
//        rl_top_title.setVisibility(View.GONE);

//        refresh_layout = (SmartRefreshLayout) view.findViewById(R.id.refresh_layout);
//        refresh_layout.setRefreshHeader(new ClassicsHeader(mContext));
//        refresh_layout.setRefreshFooter(new ClassicsFooter(mContext));
        recyclerView = (RecyclerView) view.findViewById(R.id.reclerview);
        manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new RecyclerViewSpaceDecoration(5));
//        adapter = new SelectedRecipeAdapter(mContext);
//        recyclerView.setAdapter(adapter);
    }



//    @Override
//    public void onPobiRecipes(PobiRecipeBean pobiRecipeBean, boolean b) {
//        if(adapter == null || pobiRecipeBean == null || pobiRecipeBean.getCookbook_list() == null) return;
//        if(isLoadMore) {
//            PobiRecipeBean old = adapter.getData();
//            List<PobiRecipeBean.CookbookListBean> newList = new ArrayList<>();
//            if(old != null && old.getCookbook_list() != null) {
//                newList.addAll(old.getCookbook_list());
//                newList.addAll(pobiRecipeBean.getCookbook_list());
//            }
//            pobiRecipeBean.setCookbook_list(newList);
//        }
//        adapter.setData(pobiRecipeBean);
//        isRefresh = false;
//        isLoadMore = false;
//        if (refresh_layout != null) {
//            refresh_layout.finishLoadmore();
//            refresh_layout.finishRefresh();
//        }
//    }
//
//    @Override
//    public void onPobiRecipesFail(String s) {
//        isLoadMore = false;
//        isRefresh = false;
//        if (refresh_layout != null) {
//            refresh_layout.finishLoadmore();
//            refresh_layout.finishRefresh();
//        }
//    }
}
