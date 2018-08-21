package com.erly.shakedemo;

import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.erly.shakedemo.ifc.OnShakeListener;
import com.erly.shakedemo.util.ShakeManager;
import com.erly.shakedemo.util.Utils;

public class MainActivity extends AppCompatActivity implements OnShakeListener {
    private SoundPool mSoundPool;
    private int wxAudioID;
    private Vibrator mVibrator;
    private ImageView ivTop;
    private ImageView ivBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ivTop = findViewById(R.id.main_shake_top);
        ivBottom = findViewById(R.id.main_shake_bottom);

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
    public void onShakeComplete(SensorEvent event) {
        Utils.starWxShakeAnim(ivTop,ivBottom);
        mVibrator.vibrate(300);
        mSoundPool.play(wxAudioID, 1, 1, 0, 0, 1);
    }

    @Override
    protected void onDestroy() {
        mSoundPool.release();
        super.onDestroy();
    }
}
