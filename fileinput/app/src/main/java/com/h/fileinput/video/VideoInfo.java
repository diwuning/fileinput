package com.h.fileinput.video;

/**
 * Created by h on 2016/3/4 0004.
 */
public class VideoInfo {
    public int id;
    public String title;
    public String album;
    public String artist;
    public String displayName;
    public String mimeType;
    public String path;
    public long size;
    public long duration;
    public VideoInfo(){

    }

    public VideoInfo(int id,String _title,String _album,String _artist,String _displayName,String _mimeType,
                     String _path,long _size,long _duration){
        super();
        this.id = id;
        this.title = _title;
        this.album = _album;
        this.artist = _artist;
        this.displayName = _displayName;
        this.mimeType = _mimeType;
        this.path = _path;
        this.size = _size;
        this.duration = _duration;
    }
}
