package com.example.lenovo.myui;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.example.lenovo.lwmusic.R;
import com.example.lenovo.model.IWordButtonCliskListener;
import com.example.lenovo.model.WordButton;
import com.example.lenovo.util.Util;

import java.util.ArrayList;




/**
 * Created by lenovo on 2015/8/6.
 */
public class MyGridView extends GridView {
    private ArrayList<WordButton>mArraylist=new ArrayList<WordButton>();
    private MyGridAdapter myGridAdapte;
    private Context mcontext;
    private IWordButtonCliskListener mWordButtonListener;
    private Animation mScaleAnimation;
    public final static int COUNTS_WORDS=24;


    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext=context;
        myGridAdapte=new MyGridAdapter();
        this.setAdapter(myGridAdapte);
    }
    public void updataData(ArrayList<WordButton> list){
                mArraylist=list;
        setAdapter(myGridAdapte);//ˢ��,������������Դ

    }
    class MyGridAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return mArraylist.size();
        }

        @Override
        public Object getItem(int position) {
            return mArraylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int pos, View v, ViewGroup p) {
            final WordButton holder;

            if (v==null){
              v= Util.getView(mcontext, R.layout.self_ui_gridview_item);
                holder=mArraylist.get(pos);

                //���ض���
                mScaleAnimation = AnimationUtils.loadAnimation(mcontext,R.anim.scale);
                //�����ӳ�ʱ��
                mScaleAnimation.setStartOffset(pos*30);
                holder.mIndex=pos;
                holder.mViewButton=(Button)v.findViewById(R.id.item_button);
                holder.mViewButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mWordButtonListener.onWordButtonClick(holder);
                    }
                });
                v.setTag(holder);//�������һ����ǩ
            }else{
                holder= (WordButton) v.getTag();
            }
            //���Ŷ���
            v.startAnimation(mScaleAnimation);
            holder.mViewButton.setText(holder.mWordString);//��ʾ�ַ���

            return v;
        }
    }
    /**
     *ע������ӿ�
     * @param listener
     */
    public void registOnWordButtonClisck(IWordButtonCliskListener listener){
        mWordButtonListener=listener;

    }
}
