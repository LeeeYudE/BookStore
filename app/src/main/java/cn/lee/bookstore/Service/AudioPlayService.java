package cn.lee.bookstore.Service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;

import cn.lee.bookstore.utils.ConstantUtil;

/**
 * Created by Administrator on 2016/1/21.
 */
public class AudioPlayService extends Service {

    private MediaPlayer mediaPlayer;
    private BroadcastReceiver broadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlay();
        initBroadcast();
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if (action.equals(ConstantUtil.ACTION_LISTENER_PLAY)){
                    String url = intent.getStringExtra("url");
                    try {
                        mediaPlayer.setDataSource(AudioPlayService.this,Uri.parse(url));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(ConstantUtil.ACTION_LISTENER_PLAY);
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public AudioPlayService playService = AudioPlayService.this;
    }


    protected void initMediaPlay() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(mPreparedListener);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                // 当音频播放结束的时候会调用这个方法
                @Override
                public void onCompletion(MediaPlayer mp) {
                    System.out.println("onCompletion");
//                    next();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private MediaPlayer.OnPreparedListener mPreparedListener=new
            MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
        }
    };

    private void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void play(String url){
        try {
            mediaPlayer.setDataSource(this, Uri.parse(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** 播放开关，要么播放，要么暂停*/
    public void playToggle() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
//                notificationManager.cancel(notificationId);
            } else {
                mediaPlayer.start();
//                sendNotification();
            }
        }
    }

    /** 获取当前播放的位置 */
    public int getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }

    /** 获取音频的总时长 */
    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }

    /** 位置跳转 */
    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


}
