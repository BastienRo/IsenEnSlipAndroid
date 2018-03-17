package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class Accueil extends AppCompatActivity {

    public boolean mIsBound = false;
    private MyService mServ;

    MediaPlayer Soundbutton;
    Button jouer;
    Intent music;

    private ServiceConnection Scon = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MyService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService() {
        bindService(new Intent(this, MyService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
        jouer = (Button) findViewById(R.id.btnJouer);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Soundbutton = MediaPlayer.create(this, R.raw.soundbuton);
        mServ = new MyService();
        music = new Intent();
        music.setClass(this, MyService.class);
        doBindService();
        startService(music);
    }

    public void goToJouer(View v) {

        mServ.onDestroy();
        mServ.choix = 1;
        mServ.length = 0;

        mServ.onCreate();


        Intent jeu = new Intent(this, GameBoardActivity.class);
        startActivity(jeu);

    }

    public void goToRegle(View v) {
        //Soundbutton.start();

        Intent regle = new Intent(this, Regle.class);
        startActivity(regle);


    }

    public void goToParam(View v) {
        //Soundbutton.start();

        Intent param = new Intent(this, Parametre.class);
        startActivity(param);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mIsBound) {
            mServ.pauseMusic();
            mIsBound = false;
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (!mIsBound) {
            mServ.resumeMusic();
            mIsBound = true;
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                }).create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
        stopService(music);
        mServ.onDestroy();
    }

}
