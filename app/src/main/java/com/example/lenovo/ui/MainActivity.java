package com.example.lenovo.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.data.Const;
import com.example.lenovo.lwmusic.R;
import com.example.lenovo.model.IAlerDialogButtonListener;
import com.example.lenovo.model.IWordButtonCliskListener;
import com.example.lenovo.model.Song;
import com.example.lenovo.model.WordButton;
import com.example.lenovo.myui.MyGridView;
import com.example.lenovo.util.MyPlayer;
import com.example.lenovo.util.Util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class  MainActivity extends Activity implements IWordButtonCliskListener{

    // 唱片相关动画
    private Animation mPanAnim;
    private LinearInterpolator mPanLin;//动画运动的速度

    /**自定义LOG*/
    public final static String TAG="MainActivity";
    /** 答案状态 —— 正确 */
    public final static int STATUS_ANSWER_RIGHT =1;
    /** 答案状态 —— 错误 */
    public final static int STATUS_ANSWER_WRONG =2;
    /** 答案状态 —— 不完整 */
    public final static int STATUS_ANSWER_LACK =3;

    /**闪烁次数*/
    public final static int SPASH_TIMES =6;

    /***
     * 对话框ID
     * */
    public final static int ID_DIALOG_DELETE_WORD=1; //删除错误答案
    public final static int ID_DIALOG_TIP_ANSWER=2; //答案提示
    public final static int ID_DIALOG_LACK_COIN=3; //金币不足
    /**
     * 通关标志
     * */
    private boolean FINSH_ALL=false;

    // 摇杆进入动画
    private Animation mBarInAnim;
    private LinearInterpolator mBarInLin;

    // 摇杆退出动画
    private Animation mBarOutAnim;
    private LinearInterpolator mBarOutLin;

    //显示过关歌曲名称
    private TextView mCurrentSongNamePassView;

    // 唱片控件
    private ImageView mViewPan;

    // 拨杆控件
    private ImageView mViewPanBar;

    //过关文本
    private TextView mCurrentStagePassView;
    //过关文本
    private TextView mCurrentStageView;

    // Play 按键事件
    private ImageButton mBtnPlayStart;

    // 过关界面
    private View mPassView;

    // 当前动画是否正在运行
    private boolean mIsRunning = false;

    // 文字框容器
    private ArrayList<WordButton> mAllWords;   //待选框文字
    private ArrayList<WordButton> mBtnSelectWords;//已选择的文字

    private MyGridView mMyGridView;


    // 当前的歌曲
    private Song mCurrentSong;

    //当前关的索引
    private int mCurrentStageIndex= -1;

    //初始关的索引
    private int mCurrentStageDefaultIndex= -1;

    //金币数量
    private int mCurrentCoins = Const.TOTAL_COIN;

    //显示金币
    private TextView mViewCurrentCoins;

    // 答案框
    private LinearLayout mViewWordsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //读取游戏数据
        int[] datas=Util.loadData(this);
        mCurrentStageIndex=datas[Const.INDEX_LOAD_DATA_STAGE];
        mCurrentCoins=datas[Const.INDEX_LOAD_DATA_COIN];
        // 初始化控件
        mViewPan = (ImageView)findViewById(R.id.imageView1);//盘片
        mViewPanBar = (ImageView)findViewById(R.id.imageView2);//摇杆
        mViewCurrentCoins = (TextView) findViewById(R.id.tet_bar_coin);//金币
        mViewCurrentCoins.setText(mCurrentCoins + "");

        mMyGridView = (MyGridView)findViewById(R.id.gridview);

        // 注册监听
        mMyGridView.registOnWordButtonClisck(this);
        mViewWordsContainer = (LinearLayout)findViewById(R.id.word_select_cantainer);

        //初始化摇杆动画
        mBarInAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_45);
        mBarInLin = new LinearInterpolator();
        mBarInAnim.setInterpolator(mBarInLin);
        mBarInAnim.setFillAfter(true);
        mBarInAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // 开始唱片动画
                mViewPan.startAnimation(mPanAnim);
            }


        });
        //初始化摇杆出的动画
        mBarOutAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_back);
        mBarOutLin = new LinearInterpolator();
        mBarOutAnim.setInterpolator(mBarOutLin);//修改的位置mPanLin原为mBarOutLim
        mBarOutAnim.setFillAfter(true);
        mBarOutAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 整套动画播放完毕
                mIsRunning = false;
                mBtnPlayStart.setVisibility(View.VISIBLE);


            }
        });

// 初始化盘片动画
        mPanAnim = AnimationUtils.loadAnimation(this, R.anim.rotate);
        mPanLin = new LinearInterpolator();
        mPanAnim.setInterpolator(mPanLin);
        mPanAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                // 开始唱片动画
                mViewPanBar.startAnimation(mBarOutAnim);
            }
        });

        mBtnPlayStart = (ImageButton)findViewById(R.id.btn_play_start);
        mBtnPlayStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                handlePlayButton();

            }
        });

        // 初始化游戏数据
        initCurrentStageData();

        // 处理删除按键事件
        handleDeleteWord();

        // 处理提示按键事件
        handleTipWord();


        // 处理案件后退事件
        handleBackEvent();


    }

    /**
     * 处理圆盘中间的播放按钮，就是开始播放音乐
     */
        private void handlePlayButton() {
        if (mViewPanBar != null) {
            if (!mIsRunning) {
                mIsRunning = true;

                // 开始拨杆进入动画
                mViewPanBar.startAnimation(mBarInAnim);
                mBtnPlayStart.setVisibility(View.INVISIBLE);
                //播放音乐
                MyPlayer.playSong(MainActivity.this, mCurrentSong.getSongFileName());

            }
        }
    }


    /**
     * 处理返回键
     * */
    private void  handleBackEvent(){

        ImageButton backbutton = (ImageButton) findViewById(R.id.btn_bar_back);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mCurrentStageIndex > 0) {
                    // 减少当前关卡数

                    mCurrentStageIndex -= 2;
                    // 保存游戏数据
                    Util.saveData(MainActivity.this, mCurrentStageIndex - 1,
                            mCurrentCoins);

                    //暂停音乐
                    MyPlayer.stopTheSong(MainActivity.this);

                    mViewPan.clearAnimation();
                    // 减少金币
                    handleCoins(-getfinashCoin());
                    // 重新加载关卡
                    initCurrentStageData();
                }
            }
        });
    }



    @Override
    public void onPause() {
        //保存游戏数据
       if (!FINSH_ALL){Util.saveData(MainActivity.this, mCurrentStageIndex - 1, mCurrentCoins);}
        mViewPan.clearAnimation();
        MyPlayer.stopTheSong(MainActivity.this);
        super.onPause();
    }

    //读取歌曲信息
    private Song loadStageSongInfo(int stageindex){
        Song song =new Song();
        String[] stage = Const.SONG_INFO[stageindex];
        song.setSongFileName(stage[Const.INDEX_FILE_NAME]);
        song.setSongName(stage[Const.INDEX_SONG_NAME]);
        return song;

    }
    /**
     *加载关卡数据
     * */
    private  void initCurrentStageData(){
        // 第一关的时候隐藏后退按钮
        ImageButton backButton = (ImageButton) findViewById(R.id.btn_bar_back);
        if(mCurrentStageIndex == -1){
            backButton.setVisibility(View.INVISIBLE);
        }else {
            backButton.setVisibility(View.VISIBLE);
        }



        // 读取当前关的歌曲信息
        mCurrentSong =loadStageSongInfo(++mCurrentStageIndex);
        // 初始化已选择框
        mBtnSelectWords=initWordSelect();
        LayoutParams params=new LayoutParams(mSize(),mSize());

        /**
         * 清空之前的答案框
         * */
        mViewWordsContainer.removeAllViews();
        //增加新的答案框
        for(int i=0;i<mBtnSelectWords.size();i++){
            mViewWordsContainer.addView(mBtnSelectWords.get(i).mViewButton,
                    params);
        }
        //显示当前关卡索引
        mCurrentStageView= (TextView) findViewById(R.id.text_current_stage);
        if (mCurrentStageView!=null){mCurrentStageView.setText((mCurrentStageIndex+1)+"");}
         //获得数据
        mAllWords = initAllWord();
        //更新数据 - MyGridView
        mMyGridView.updataData(mAllWords);
        //开始就播放音乐
        handlePlayButton();

    }

    /**
     * 获得手机屏幕尺寸并且返回选择框大小
     * */

    private int mSize(){

        //获得屏幕宽度和高度
        WindowManager wm=(WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int wwidth=wm.getDefaultDisplay().getWidth(); //手机屏幕的宽度
        int hheight=wm.getDefaultDisplay().getHeight(); //手机屏幕的高度
        int size=(wwidth+hheight)/Const.WEIHTANDHIGHT;
        return size;
    }

    //初始化待选文字框
    private  ArrayList<WordButton> initAllWord(){
        ArrayList<WordButton> data =new ArrayList<WordButton>();

        // 获得所有待选文字
        String[] words=generateWord();
        for (int i=0;i<MyGridView.COUNTS_WORDS;i++){
            WordButton Button =new WordButton();
            Button.mWordString=words[i];
            data.add(Button);
        }
        return  data;
    }

    //初始化已选文字框
    private ArrayList<WordButton> initWordSelect(){
        ArrayList<WordButton> data =new ArrayList<WordButton>();
        for (int i=0;i<mCurrentSong.getNameLength();i++){
            View view = Util.getView(MainActivity.this,R.layout.self_ui_gridview_item);
            final WordButton holder=new WordButton();
            holder.mViewButton = (Button) view.findViewById(R.id.item_button);
            holder.mViewButton.setTextColor(Color.WHITE);
            holder.mViewButton.setText("");
            holder.mIsVisiable=false;
            holder.mViewButton.setBackgroundResource(R.mipmap.game_wordblank);
            holder.mViewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cleanTheAnswer(holder);
                }
            });
            data.add(holder);
        }
        return  data;
    }

    @Override
    public void onWordButtonClick(WordButton wordButton)
    {
        setSelectWord(wordButton);
        //检查答案的状态
        int checkResult=checkTheanswer();

        //检查答案
        switch (checkResult){
            case STATUS_ANSWER_LACK://答案不完整

                //设置文字颜色为白色
                for (int i =0;i<mBtnSelectWords.size();i++){
                    mBtnSelectWords.get(i).mViewButton.setTextColor(Color.WHITE);
                }
                break;
            case STATUS_ANSWER_RIGHT:// 过关并获得奖励
                handlePassEvent();
                break;
            case STATUS_ANSWER_WRONG:// 闪烁文字并提示用户
                sparkTheWork();
                break;


        }
    }
    /**处理过关界面及事件*/
    private void handlePassEvent(){
        //进入通关画面
        mPassView =(LinearLayout)this.findViewById(R.id.pass_view);

        // 设置待选框可见性
        mPassView.setVisibility(View.VISIBLE);

        //停止未完成的动画
        mViewPan.clearAnimation();
        //停止正在播放的音乐
        MyPlayer.stopTheSong(MainActivity.this);
        //播放音效
        MyPlayer.playTone(MainActivity.this,MyPlayer.INDEX_STONE_COIN);
       //当前关的索引
        mCurrentStagePassView= (TextView) findViewById(R.id.text_current_stage_pass);
        if (mCurrentStagePassView!=null){
            mCurrentStagePassView.setText((mCurrentStageIndex+1)+"");
        }

        // 显示歌曲名称
        mCurrentSongNamePassView =(TextView) findViewById(R.id.text_current_song_pass);
        if (mCurrentSongNamePassView!=null){
            mCurrentSongNamePassView.setText(mCurrentSong.getSongName());
        }
        //设置下一关按钮事件
        ImageButton btnPass = (ImageButton)findViewById(R.id.btn_next);
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judegAppPassed()){
                    //跳转到通关画面
                    Util.saveData(MainActivity.this, mCurrentStageDefaultIndex, mCurrentCoins);
                    FINSH_ALL=true;
                    Util.startActivity(MainActivity.this,AllPassView.class);
                }else {
                    //过关画面可见
                    mPassView.setVisibility(View.GONE);
                    //过关奖励
                    finsh_addcoin();
                    //删除按钮显示
                    setDeleteconfig(View.VISIBLE);
                    //加载游戏数据
                    initCurrentStageData();
                }
            }
        });
    }
    /**过关增加金币*/
    private void finsh_addcoin(){
        mCurrentCoins+=getfinashCoin();
        mViewCurrentCoins.setText(mCurrentCoins+"");

    }
    /**判断是否通关*/
    private boolean judegAppPassed(){
        return (mCurrentStageIndex == Const.SONG_INFO.length-1);
    }

    //设置按钮可见性
    private void setButtonVisiable(WordButton wordButton,int visbility){
        wordButton.mViewButton.setVisibility(visbility);

    }
    private void cleanTheAnswer(WordButton wordbuttoon){
        wordbuttoon.mViewButton.setText("");
        wordbuttoon.mWordString="";
        wordbuttoon.mIsVisiable=false;
        //清除答案
        setButtonVisiable(mAllWords.get(wordbuttoon.mIndex), View.VISIBLE);

    }

    private void setSelectWord(WordButton wordButton) {
        for (int i = 0; i < mBtnSelectWords.size(); i++) {
            if (mBtnSelectWords.get(i).mWordString.length() == 0) {
                // 进入待选框
                mBtnSelectWords.get(i).mViewButton.setText(wordButton.mWordString);
                mBtnSelectWords.get(i).mIsVisiable = true;
                mBtnSelectWords.get(i).mWordString = wordButton.mWordString;

                // 获得索引
                mBtnSelectWords.get(i).mIndex = wordButton.mIndex;

                // 设置按键不可见
                setButtonVisiable(wordButton, View.INVISIBLE);

                break;
            }
        }
    }
    //生成所有的待选文字
    private String[] generateWord() {
        Random random=new Random();
        String[] words =new String[MyGridView.COUNTS_WORDS];
        //存入歌名
        for (int i=0;i<mCurrentSong.getNameLength();i++){
            words[i]=mCurrentSong.getNameCharacters()[i]+"";
        }
        //获取随机文字并存入数组
        for (int i=mCurrentSong.getNameLength();i<MyGridView.COUNTS_WORDS;i++){
            words[i]=getRandomChar()+"";
        }
        // 打乱文字顺序：首先从所有元素中随机选取一个与第一个元素进行交换，
        // 然后在第二个之后选择一个元素与第二个交换，知道最后一个元素。
        // 这样能够确保每个元素在每个位置的概率都是1/n。
        for (int i=MyGridView.COUNTS_WORDS-1;i>=0;i--){
            int index = random.nextInt(i+1);//随机交换位置
            String buf=words[index];
            words[index]=words[i];
            words[i]=buf;
        }

        return words;
    }


    //生成随机汉字
    private char getRandomChar() {
        String str="";
        int hightPas;
        int lowPas;
        Random random= new Random();//随机
        hightPas=(176+Math.abs(random.nextInt(39)));//高位字符。后面数字太大会生成生僻字
        lowPas=(161+Math.abs(random.nextInt(93)));//低位字符
        byte[] b=new byte[2];//定义字节数组
        b[0]=(Integer.valueOf(hightPas)).byteValue();
        b[1]=(Integer.valueOf(lowPas)).byteValue();
        try {
            str=new String(b,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str.charAt(0);

    }
    /** 检查答案*/
    public int checkTheanswer(){
        //按已选框文字数量确定循环检查次数
        for (int i=0;i<mBtnSelectWords.size();i++){
            //判断已选框是否为空
            if (mBtnSelectWords.get(i).mWordString.length()==0){
                return STATUS_ANSWER_LACK;
            }
        }
            //判断答案是否一致
            StringBuffer sb =new StringBuffer();
            for (int i=0;i<mBtnSelectWords.size();i++){
                sb.append(mBtnSelectWords.get(i).mWordString);
        }
        return (sb.toString().equals(mCurrentSong.getSongName())) ?
                STATUS_ANSWER_RIGHT:STATUS_ANSWER_WRONG;
    }
    /**闪烁文字*/
    private void sparkTheWork(){
        TimerTask task=new TimerTask() {
            boolean mChange=false;
            int mSpardTimes = 0;
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (++mSpardTimes>SPASH_TIMES){
                            return;
                        }
                        //闪烁逻辑
                        for (int i =0;i<mBtnSelectWords.size();i++){
                            mBtnSelectWords.get(i).mViewButton.setTextColor(
                                    mChange? Color.RED:Color.WHITE);
                        }
                        mChange=!mChange;
                    }
                });
            }
        };
        //设置定时器
        Timer timer =new Timer();
        timer.schedule(task,1,150);
    }

    /**增加或者减少指定数量的金币*/
    private boolean handleCoins(int data){
        //判断当前总的金币数量是否可被减少
        if (mCurrentCoins + data>=0){
            mCurrentCoins+=data;
            mViewCurrentCoins.setText(mCurrentCoins+"");
            return true;
        }
        else {return false;}
    }
    /**
     * 提示文字，自动选择一个答案
     * */
    private void tipAnswer(){
        // 减少金币数量
        if (!handleCoins(-getTipCoin())){
            // 金币数量不够，显示对话框，提示用户金币不够
            showConfirmDialog(ID_DIALOG_LACK_COIN);
            return;
        }
        boolean tipWord=false;
        for (int i=0;i<mBtnSelectWords.size();i++){
            if (mBtnSelectWords.get(i).mWordString.length()==0){

                //找到一个答案的文字，并且根据当前答案框的文字填入
                onWordButtonClick(findIsAnswerWord(i));
                tipWord=true;
                break;
            }
        }
        //没有找到可以填充的答案
        if (!tipWord){
            //闪烁文字来提示用户
            sparkTheWork();
        }

    }
    /**
     * 删除一个文字
     * */
    private void deleteOneWord(){
        // 减少金币
        if (!handleCoins(-getDeleteWordCoin())){
            // 不能减少指定的金币,显示提示对话框
            showConfirmDialog(ID_DIALOG_LACK_COIN);
            return;
        }

        // 将这个索引对应的WordButton设置为不可见
       for (int i=0;i<=Const.DELETETIME;i++) {
           if (findNoteAnswerWord() != null) {
               setButtonVisiable(findNoteAnswerWord(), View.INVISIBLE);
           }
       }
    }
    /**随机删除一个文字*/
    private WordButton findNoteAnswerWord(){

        Random random = new Random();
        WordButton buf = null;
        //if (mDelNum <( MyGridView.COUNTS_WORDS)-mCurrentSong.getNameLength()) {
        List<WordButton> btns = findVisibleWordButton();
        if (btns.size() == -1) {
            buf = null;
        } else {
            buf = btns.get(random.nextInt(btns.size()));
        }

        return buf;
    }


    /**
     * 找到所有可见的，且不是答案的按钮列表
     */
    private List<WordButton> findVisibleWordButton() {
        List<WordButton> wordBtns = new ArrayList<WordButton>();

        for (WordButton btn : mAllWords) {
            if (btn.mIsVisiable && !isTheAnswerWord(btn)) {
                wordBtns.add(btn);
            }
        }

        return wordBtns;
    }
    /**
     * index
     *            当前需要填入答案框的索引
     * */
    private WordButton findIsAnswerWord(int index){
        WordButton buf = null;
        for(int i=0;i<MyGridView.COUNTS_WORDS;i++){
            buf=mAllWords.get(i);
            if (buf.mWordString.equals(""+mCurrentSong.getNameCharacters()[index])){
                return buf;
            }
        }
        return null;

    }
    /**判断某个文字是否是答案*/
    private boolean isTheAnswerWord(WordButton word){
        boolean result =false;
        for (int i=0;i<mCurrentSong.getNameLength();i++){
            if (word.mWordString.equals
                    (""+mCurrentSong.getNameCharacters()[i])){
                result =true;
                break;
            }
        }
        return result;
    }

    /**从配置文件里面读取删除操作用的金币*/
    private int getDeleteWordCoin(){
        return this.getResources().getInteger(R.integer.play_delete_answer);
    }
    /**从配置文件里面读取提示操作用的金币*/
    private int getTipCoin(){
        return this.getResources().getInteger(R.integer.play_tip_answer);
    }

    /**从配置文件里面读取过关界面增加的金币*/
    private int getfinashCoin(){
        return this.getResources().getInteger(R.integer.finsh_addcoin);
    }

    /**处理删除待文字事件*/
    private  void handleDeleteWord(){
        ImageButton button = (ImageButton) findViewById(R.id.btn_delete_word);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog(ID_DIALOG_DELETE_WORD);
            }
        });
    }
    /**处理提示文字事件*/
    private  void handleTipWord(){
        ImageButton button = (ImageButton) findViewById(R.id.btn_tip_answer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmDialog(ID_DIALOG_TIP_ANSWER);
            }
        });
    }
    //对话框事件
    //删除错误答案
    private IAlerDialogButtonListener mBtnOkDeleteWordListener =
            new IAlerDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行事件，删除错误答案
                deleteOneWord();
                //删除按钮隐藏
                    setDeleteconfig(View.INVISIBLE);
                }
            };
    //答案提示
    private IAlerDialogButtonListener mBtnTipWordListener =
            new IAlerDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行事件，答案提示
                tipAnswer();
                }
            };
    //金币不足
    private IAlerDialogButtonListener mBtnLackCoinWordListener =
            new IAlerDialogButtonListener() {
                @Override
                public void onClick() {
                    //执行事件，金币不足

                }
            };

    //显示对话框
    private void showConfirmDialog(int d){
        switch (d){
            case ID_DIALOG_DELETE_WORD:
                Util.showDialog(MainActivity.this,"确认使用"+getDeleteWordCoin()+"个金币来删除多个错误选择吗？一关只能够使用一次噢！！",mBtnOkDeleteWordListener);
                break;

            case ID_DIALOG_TIP_ANSWER:
                Util.showDialog(MainActivity.this,"确认使用"+getTipCoin()+"个金币来获得正确答案吗？",mBtnTipWordListener);
                break;

            case ID_DIALOG_LACK_COIN:
                Util.showDialog(MainActivity.this,"金币不足噢。~",mBtnLackCoinWordListener);
                break;
        }
    }

    /**
     * 设置删除按钮是否可见
     * */
    private void setDeleteconfig(int vis){
        //按钮
        ImageButton button = (ImageButton) findViewById(R.id.btn_delete_word);
        button.setVisibility(vis);
        //文字
        TextView textView = (TextView) findViewById(R.id.delete_text);
        textView.setVisibility(vis);
        //图形
        ImageView view= (ImageView) findViewById(R.id.delete_coin);
        view.setVisibility(vis);

    }
}