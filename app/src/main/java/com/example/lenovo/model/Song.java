package com.example.lenovo.model;

/**
 * Created by lenovo on 2015/8/8.
 */
public class Song {
    //歌曲名称
    private String mSongName;
    //歌曲文件名称
    private String mSongFileName;
    //名字长度
    private int mNameLength;

    public char[] getNameCharacters(){
        return mSongName.toCharArray();  //字符串转换成数组
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songname) {
        this.mSongName = songname;
        this.mNameLength=songname.length();//歌名长度和名字长度一样
    }

    public String getSongFileName() {
        return mSongFileName;
    }

    public void setSongFileName(String songfilename) {
        this.mSongFileName = songfilename;
    }

    public int getNameLength() {
        return mNameLength;
    }


}
