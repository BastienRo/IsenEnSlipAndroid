package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by brodrigue on 06/02/2018.
 */

public class MyService extends Service implements MediaPlayer.OnErrorListener {
    private final IBinder mBinder = new ServiceBinder();
    MediaPlayer mPlayer;

    public int background = R.raw.backgroundmusic;
    public int choix;
    public int gameTimer = R.raw.mansnothot;
    public Uri personelMusique;
    public boolean musicchangee;
    public int length = 0;

    public MyService() {
    }

    public class ServiceBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (choix == 0)  {
            mPlayer = MediaPlayer.create(this, background);
        }
        if (choix == 2){
            mPlayer = MediaPlayer.create(this, personelMusique);
        }
        if (choix == 1){
            mPlayer = MediaPlayer.create(this, gameTimer);
        }

        mPlayer.setOnErrorListener(this);

        if (mPlayer != null) {
            mPlayer.setLooping(true);
            mPlayer.setVolume(100, 100);
        }


        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            public boolean onError(MediaPlayer mp, int what, int
                    extra) {

                onError(mPlayer, what, extra);
                return true;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mPlayer.start();
        return START_STICKY;
    }

    public void pauseMusic() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            length = mPlayer.getCurrentPosition();

        }
    }

    public boolean isMusicchangee(Uri personelMusique){
        if(personelMusique == null){
            musicchangee = false;
        } else {
            musicchangee = true;
        }
        return  musicchangee;
    }

    public Uri setMusicchangee(Uri truc)
    {
        personelMusique = truc;
        return personelMusique;
    }

    public void resumeMusic() {
        if (mPlayer.isPlaying() == false) {
            mPlayer.seekTo(length);
            mPlayer.start();
        }
    }

    public void stopMusic() {
        mPlayer.stop();
        mPlayer.release();
        mPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
    }

    public boolean onError(MediaPlayer mp, int what, int extra) {

        Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
        if (mPlayer != null) {
            try {
                mPlayer.stop();
                mPlayer.release();
            } finally {
                mPlayer = null;
            }
        }
        return false;
    }
}