package com.erly.shakedemo;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erly.shakedemo.ifc.AnimListener;
import com.erly.shakedemo.ifc.OnShakeListener;
import com.erly.shakedemo.util.ShakeManager;
import com.erly.shakedemo.util.Utils;

import java.util.Random;

public class ShakeActivity extends AppCompatActivity implements OnShakeListener,AnimListener{
    private SoundPool mSoundPool;
    private int wxAudioID;
    private Vibrator mVibrator;
    private ImageView ivTop;
    private ImageView ivBottom;
    private LinearLayout llRoot;
    private ProgressBar pb;
    private TextView tvMsg;

    /**
     * 自跳转
     * @param context
     */
    public static void selfJump(Context context){
        Intent intent = new Intent(context,ShakeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);
        ivTop = findViewById(R.id.iv_shake_top);
        ivBottom = findViewById(R.id.iv_shake_bottom);
        llRoot = findViewById(R.id.shake_bottom_root);
        pb = findViewById(R.id.shake_progress);
        tvMsg = findViewById(R.id.shake_msg);

        mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        wxAudioID = mSoundPool.load(this, R.raw.weichat_audio, 1);
        ShakeManager.getInstance().setShakeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ShakeManager.getInstance().register(this);
    }

    @Override
    protected void onStop() {
        ShakeManager.getInstance().unRegister();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mSoundPool.release();
        super.onDestroy();
    }

    @Override
    public void onShakeComplete(SensorEvent event) {
        Utils.starWxShakeAnim(ivTop,ivBottom,this);
        mVibrator.vibrate(300);
        mSoundPool.play(wxAudioID, 1, 1, 0, 0, 1);
        llRoot.setVisibility(View.GONE);
    }

    @Override
    public void animEnd(Animation animation) {
        llRoot.setVisibility(View.VISIBLE);
        pb.setVisibility(View.VISIBLE);
        tvMsg.setText("拼命加载中...");
        handler.sendEmptyMessageDelayed(0,2000);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0){
                pb.setVisibility(View.INVISIBLE);
                tvMsg.setText("恭喜您中奖啦！兑换码："+ new Random().nextInt());
            }
        }
    };
}
