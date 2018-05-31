package com.h.fileinput.db.operateDao;

import com.h.fileinput.MyApp;
import com.h.fileinput.db.greenBean.CookBean;

/**
 * Created by Administrator on 2017/3/17 0017.
 */
public class CookDao {
    /**
     * 增加菜谱
     * @author wangchm
     * @2017年3月16日
     * @param bean
     */
    public static void insertData(CookBean bean){
        MyApp.getDaoSession().getCookBeanDao().insertOrReplace(bean);
    }
}
