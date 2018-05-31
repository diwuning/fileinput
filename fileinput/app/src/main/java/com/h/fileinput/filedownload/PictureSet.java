package com.h.fileinput.filedownload;

/**
 * Created by h on 2016/1/29 0029.
 */
public class PictureSet {
    public int cameraId;
    public String pictureName;
    public String pictureData;
    public String createTime;

    public PictureSet(int cameraId1,String pictureName1,String pictureData1,String createTime1){
        this.cameraId = cameraId1;
        this.pictureName = pictureName1;
        this.pictureData = pictureData1;
        this.createTime = createTime1;
    }
    public PictureSet(){

    }
}
