package com.example.lenovo.model;

/**
 * Created by lenovo on 2015/8/8.
 */
public class Song {
    //��������
    private String mSongName;
    //�����ļ�����
    private String mSongFileName;
    //���ֳ���
    private int mNameLength;

    public char[] getNameCharacters(){
        return mSongName.toCharArray();  //�ַ���ת��������
    }

    public String getSongName() {
        return mSongName;
    }

    public void setSongName(String songname) {
        this.mSongName = songname;
        this.mNameLength=songname.length();//�������Ⱥ����ֳ���һ��
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
