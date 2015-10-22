package com.example.lenovo.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lenovo.data.Const;
import com.example.lenovo.lwmusic.R;
import com.example.lenovo.model.IAlerDialogButtonListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lenovo on 2015/8/6.
 */
public class Util {
    private static AlertDialog mAlertDiaglog;
    public static View getView(Context context ,int layoutid){

        LayoutInflater inflater= (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout=inflater.inflate(layoutid,null);
        return  layout;
    }
    /**
     * 界面跳转
     * */
    public  static void startActivity(Context context,Class desti){
        Intent intent=new Intent();
        intent.setClass(context,desti);
        context.startActivity(intent);

        //关闭当前的Activity
        ((Activity)context).finish();
    }
    /**
     * 显示自定义对话框
     * */
    public static void showDialog(final Context context,String msg,
                                  final IAlerDialogButtonListener listener){
        View dialogview=null;
        AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.Theme_Transparent);
        dialogview=getView(context, R.layout.dialog_view);
        ImageButton btnOKview = (ImageButton) dialogview.findViewById(R.id.btn_dialag_ok);
        ImageButton btnCancelview = (ImageButton) dialogview.findViewById(R.id.btn_dialag_cancel);
        TextView textMessage = (TextView) dialogview.findViewById(R.id.text_dialog_message);
        textMessage.setText(msg);
        //设置OK的按键监听
        btnOKview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDiaglog != null) {
                    mAlertDiaglog.cancel();
                }
                //事件回调
                if (listener != null) {
                    listener.onClick();
                }
                //播放音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_ENTER);
            }
        });
        //设置Cancel的按键监听
        btnCancelview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAlertDiaglog!=null){
                    mAlertDiaglog.cancel();
                }
                //播放音效
                MyPlayer.playTone(context,MyPlayer.INDEX_STONE_CANCEL);
            }
        });
        //为Dialog设置View
        builder.setView(dialogview);
        //创建对话框
        mAlertDiaglog=builder.create();
        //显示对话框
        mAlertDiaglog.show();
    }
    /**
     * 游戏数据保存
     * */
    public static void saveData(Context context,int stageIndex,int coins){
        FileOutputStream fis =null;
        try {
            fis = context.openFileOutput(Const.FILE_NAME_SAVE_DATA,Context.MODE_PRIVATE);//文件输出
            DataOutputStream dos=new DataOutputStream(fis);//管道流

            try {
                dos.writeInt(stageIndex);//写入关卡数据
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dos.writeInt(coins);//写入金币
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {                      //关闭文件流
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 游戏数据读取
     * */
    public static int[] loadData(Context context){
        FileInputStream fis =null;
        int[] datas={-1,Const.TOTAL_COIN};
        try {
            fis=context.openFileInput(Const.FILE_NAME_SAVE_DATA);
            DataInputStream dis =new DataInputStream(fis);

            try {
                datas[Const.INDEX_LOAD_DATA_STAGE]=dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                datas[Const.INDEX_LOAD_DATA_COIN]=dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if (fis!=null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return datas;
    }
}
