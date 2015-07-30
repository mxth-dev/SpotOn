package at.dingbat.spoton.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.ArrayList;

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


    public class Binder extends android.os.Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE)).createWifiLock(WifiManager.WIFI_MODE_FULL, "lock");
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

            Log.d("", "Playing song '"+track.name+"' from url: "+track.preview_url);

            if(player.isPlaying()) player.stop();
            player.reset();

            player.setDataSource(track.preview_url);
            player.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        if(player != null && player.isPlaying()) {
            player.pause();
            wifiLock.release();
        }
    }

    public void resume() {
        if(player != null && !player.isPlaying()) {
            wifiLock.acquire();
            player.start();
        }
    }

    public void nextTrack() {
        currentTrack++;
        if(currentTrack < list.size()) playTrack(currentTrack);
    }

    public void previousTrack() {
        if(player != null && player.isPlaying() && player.getCurrentPosition() < 3000) currentTrack--;
        if(currentTrack >= 0) playTrack(currentTrack);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

}
