package com.h.fileinput.Mp3Media;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.h.fileinput.video.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 2016/3/1 0001.
 */
public class MediaUtil {
    //查询歌曲信息
    public static List<Mp3Info> getMp3Infos(Context context){
        Cursor cursor =context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,null,null,null,MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>();
        long id;
        int duration,size,isMusic;
        String title,artist,url;

        for(int i= 0 ;i<cursor.getCount();i++){
            Mp3Info mp3Info = new Mp3Info();
            cursor.moveToNext();
            id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));//音乐id
            title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));//音乐标题
            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));//艺术家
            duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));//时长
            size = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));//大小
            isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));//是否为音乐
            url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//文件路径
            if(isMusic != 0){  //只把音乐添加到集合里
                mp3Info.id = id;
                mp3Info.title = title;
                mp3Info.artist = artist;
                mp3Info.duration = duration;
                mp3Info.size = size;
                mp3Info.url = url;
                mp3Infos.add(mp3Info);
            }

        }
        return mp3Infos;
    }

    public static List<VideoInfo> getVideoList(Context context){
        Cursor cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,null,null,null, MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
        int id,size,duration;
        String title,artist,album,displayName,mimeType,path;

        for(int i=0;i<cursor.getCount();i++){

            cursor.moveToNext();
            id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID)); //视频ID
            title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
            artist = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
            album = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ALBUM));
            displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
            mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE));
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
            size = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
            duration = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));

            VideoInfo videoInfo = new VideoInfo(id,title,artist,album,displayName,mimeType,path,size,duration);
            videoInfoList.add(videoInfo);
        }
        return videoInfoList;

    }
}
