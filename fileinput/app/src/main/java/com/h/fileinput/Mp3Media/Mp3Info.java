package com.h.fileinput.Mp3Media;

/**
 * Created by h on 2016/2/23 0023.
 */
public class Mp3Info {
    public long id;
    public String title;
    public String artist;//艺术家
    public long duration;//时长
    public long size;//文件大小
    public String url;//文件路径

    public Mp3Info(long _id,String _title,String _artist,long _duration,long _size,String _url){
        this.id = _id;
        this.title = _title;
        this.artist = _artist;
        this.duration = _duration;
        this.size = _size;
        this.url = _url;
    }

    public Mp3Info(){

    }
}
