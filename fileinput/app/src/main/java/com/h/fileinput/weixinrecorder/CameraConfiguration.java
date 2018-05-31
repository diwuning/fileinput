package com.h.fileinput.weixinrecorder;

import android.app.Activity;
import android.hardware.Camera;
import android.view.Surface;

import java.util.List;

/**
 * Created by Harry.Kong.
 * Time 2017/2/16.
 * Email kongpengcheng@aliyun.com.
 * Description:相机的配置
 */

public class CameraConfiguration {

    /**
     * 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
     * @param activity,cameraId
     * @return
     */
    public static int getPreviewDegree1(Activity activity, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360;
            result = (360 - result) % 360;// compensate the mirror  
        } else {
            // back-facing  
            result = (info.orientation - degree + 360) % 360;
        }
        return result;
    }
    /**
     * 获取最合适的尺寸
     */
    public static Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea) {
                bestSize = s;
                largestArea = area;
            }
        }
        return bestSize;
    }

}
