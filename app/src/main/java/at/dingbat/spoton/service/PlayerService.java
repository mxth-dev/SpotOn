package at.dingbat.spoton.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import at.dingbat.spoton.activity.MainActivity;
import at.dingbat.spoton.models.ParcelableTrack;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by bendix on 30/07/15.
 */
public class PlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private IBinder binder = new Binder();

    private MediaPlayer player;
    private WifiManager.WifiLock wifiLock;

    private ArrayList<ParcelableTrack> list;
    private int currentTrack = 0;

    private Runnable onTrackChanged;
    private Runnable onStateChanged;
    private Runnable onProgressChanged;
    private Runnable onPrepared;

    private Timer timer;
    private Handler handler;
    private TimerTask timertask;

    private boolean isPrepared = false;

    public class Binder extends android.os.Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("", "onCreate");

        /*Notification n = new Notification();
        n.tickerText = "SpotOn";
        n.fullScreenIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, new Intent(getApplicationContext(), MainActivity.class), 0);
        startForeground(1, n);*/

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "lock");

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(onStateChanged != null) onStateChanged.run();
                resume();
                try {
                    timer.schedule(timertask, 0, 1000);
                } catch (Exception e) {}
                isPrepared = true;
            }
        });

        handler = new Handler();
        timer = new Timer();

        timertask = new TimerTask() {
            @Override
            public void run() {
                if(onProgressChanged != null) onProgressChanged.run();
            }
        };

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                try {
                    nextTrack();
                } catch(Exception e) {}
            }
        });
    }

    @Override
    public void onDestroy() {
        Log.d("", "onDestroy");
        super.onDestroy();
    }

    public void setPlaylist(ArrayList<ParcelableTrack> list) {
        this.list = list;
    }

    public void playTrack(int index) {
        currentTrack = index;
        playTrack(list.get(index));
    }

    private void playTrack(ParcelableTrack track) {
        try {
            if(!wifiLock.isHeld()) wifiLock.acquire();

            Log.d("", "Playing song '" +track.name+"' from url: "+track.preview_url);

            if(player.isPlaying()) player.stop();
            player.reset();

            player.setDataSource(track.preview_url);
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }


    public void pause() {
        if(player != null && player.isPlaying()) {
            player.pause();
            wifiLock.release();
            if(onStateChanged != null) onStateChanged.run();
            timer.purge();
        }
    }

    public void resume() {
        if(player != null && !player.isPlaying()) {
            wifiLock.acquire();
            player.start();
            if(onStateChanged != null) onStateChanged.run();
            try {
                timer.schedule(timertask, 0, 1000);
            } catch(Exception e) {}
        }
    }

    public void nextTrack() {

        isPrepared = false;

        if(currentTrack+1 < list.size()) {
            currentTrack++;
            playTrack(currentTrack);
        }

        try {
            if(onTrackChanged != null) onTrackChanged.run();
        } catch (Exception e) {}
    }

    public void previousTrack() {

        isPrepared = false;

        if(player != null && player.isPlaying() && player.getCurrentPosition() < 3000) currentTrack--;
        currentTrack = Math.max(0, currentTrack);
        playTrack(currentTrack);

        try {
            if(onTrackChanged != null) onTrackChanged.run();
        } catch (Exception e) {}
    }

    public ArrayList<ParcelableTrack> getPlaylist() {
        return list;
    }

    public ParcelableTrack getCurrentTrack() {
        return list.get(currentTrack);
    }

    public int getCurrentTrackIndex() {
        return currentTrack;
    }

    public boolean isFirst() {
        return currentTrack == 0;
    }

    public boolean isLast() {
        return currentTrack == (list.size()-1);
    }

    public void setOnTrackChanged(Runnable onTrackChanged) {
        this.onTrackChanged = onTrackChanged;
    }

    public void setOnStateChanged(Runnable onStateChanged) {
        this.onStateChanged = onStateChanged;
    }

    public void setOnProgressChanged(Runnable onProgressChanged) {
        this.onProgressChanged = onProgressChanged;
    }

    public void setOnPrepared(Runnable onPrepared) {
        this.onPrepared = onPrepared;
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public int getDuration() {
        /*if(isPrepared) return player.getDuration();
        else return 0;*/
        //Hardcoded because samples are always 30 seconds long
        return 30000;
    }

    public void setPosition(int position) {
        player.seekTo(position);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //player.stop();
        return super.onUnbind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
