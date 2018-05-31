package com.h.fileinput.easytouch.service;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.h.fileinput.easytouch.widgets.EasyTouchView;


public class AuxiliaryService extends Service implements EasyTouchView.ServiceListener {
    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        // ????service???? ????????TableShowView?????????????fun()???????????windowManager??
        super.onCreate();
        new EasyTouchView(this, this).initTouchViewEvent();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mIntent = intent;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void OnCloseService(boolean isClose) {
        stopService(mIntent);
    }
}