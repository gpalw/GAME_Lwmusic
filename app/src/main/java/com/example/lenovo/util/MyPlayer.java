package com.example.lenovo.util;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 *  音乐播放类
 * Created by lenovo on 2015/8/11.
 */
public class MyPlayer {

    public final  static int INDEX_STONE_ENTER  =   0;
    public final  static int INDEX_STONE_CANCEL =   1;
    public final  static int INDEX_STONE_COIN   =   2;
    //音效文件名称
    private final static String[] SONG_NAMES=
            {"enter.mp3","cancel.mp3","coin.mp3"};
    //音效
    private static MediaPlayer[] mToneMediaPlayer= new MediaPlayer[SONG_NAMES.length];

    //歌曲播放
    private static MediaPlayer mMusicMediaPlayer;

    /**
     * 播放音效   无须反复加载
     * */
    public  static  void playTone(Context context,int index){
       //加载声音
        AssetManager assetManager=context.getAssets();
        if (mToneMediaPlayer[index]==null){
            mToneMediaPlayer[index] = new MediaPlayer();
            try {
                AssetFileDescriptor fileDescriptor=assetManager.openFd(SONG_NAMES[index]);
                //数据源
                mToneMediaPlayer[index].setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(),fileDescriptor.getLength());

                mToneMediaPlayer[index].prepare();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } //使音效播放
        mToneMediaPlayer[index].start();
    }
    /**
     * 播放歌曲
     * */
    public static void playSong(Context context, String fileName) {
        // 如果为空则创建该对象，确保只创建一次
        if (mMusicMediaPlayer == null) {
            mMusicMediaPlayer = new MediaPlayer();
        }

            // 强制重置状态
            mMusicMediaPlayer.reset();

            // 加载声音文件
            AssetManager assetManager = context.getAssets();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd(fileName);
                // 准备数据源
                mMusicMediaPlayer
                        .setDataSource(fileDescriptor.getFileDescriptor(),
                                fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                // 调用prepare状态
                mMusicMediaPlayer.prepare();
                // 开始播放声音
                mMusicMediaPlayer.start();

            } catch (IOException e) {

                e.printStackTrace();
            }

    }
    /**
     * 停止声音播放
     * @param context
     */
    public static void stopTheSong(Context context){
        if(mMusicMediaPlayer!=null){
            mMusicMediaPlayer.stop();
        }
    }
}
