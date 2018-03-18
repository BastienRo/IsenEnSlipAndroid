package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
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
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    boolean isMobile;

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

    public boolean isConnected(){

        connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null) {

            networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null) {
                if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return isMobile;
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

        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Vous avez besoin d'une connexion internet pour cette application. S'il vous plait activer la WiFi ou les données mobiles dans les Options.")
                    .setTitle("Impossible de se connecter")
                    .setCancelable(false)
                    .setPositiveButton("Settings",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    )
                    .setNegativeButton("Retour",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        } else {

        }
    }

    public void goToJouer(View v) {

        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Vous avez besoin d'une connexion internet pour cette application. S'il vous plait activer la WiFi ou les données mobiles dans les Options.")
                    .setTitle("Impossible de se connecter")
                    .setCancelable(false)
                    .setPositiveButton("Options",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    )
                    .setNegativeButton("Retout",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }
                    );
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            mServ.onDestroy();
            mServ.choix = 1;
            mServ.length = 0;
            mServ.onCreate();
            Intent jeu = new Intent(this, GameBoardActivity.class);
            startActivity(jeu);
        }

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
                .setTitle("Quitter Vraiment?")
                .setMessage("Allez on s'amuse bien, non?")
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
