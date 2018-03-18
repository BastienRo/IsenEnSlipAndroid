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
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class GameBoardActivity extends AppCompatActivity {


    public int finish;
    private MediaPlayer mp;
    TextView text1;
    Button next;
    public Personne users;
    public Action action;
    public boolean mIsBound = false;
    private MyService mServ;
    TextView tvName;
    TextView tvActivite;
    Intent music;
    Parametre parametre;
    private CountDownTimer timer;
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
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        mServ = new MyService();
        //mServ.choix = 1;
        music = new Intent();
        music.setClass(this, MyService.class);
        doBindService();
        startService(music);
        next = (Button)findViewById(R.id.next);

        tvName = (TextView) findViewById(R.id.tvName);
        tvActivite = (TextView) findViewById(R.id.tvActivite);

        text1=(TextView)findViewById(R.id.textView1);



        new WebServiceTaskAction(new CallBackInterface() {
            @Override
            public void succes(String json) {
                Gson gson = new GsonBuilder().create();
                action = gson.fromJson(json, Action.class);

                tvActivite.setText(action.getActivity());
            }

            @Override
            public void error() {
                Toast.makeText(GameBoardActivity.this,"je suis dans error",Toast.LENGTH_SHORT).show();
            }
        }).execute();

        new WebServiceTask(new CallBackInterface() {
            @Override
            public void succes(String json) {
                Gson gson = new GsonBuilder().create();
                users = gson.fromJson(json, Personne.class);

                tvName.setText(users.getName());
            }

            @Override
            public void error() {
                Toast.makeText(GameBoardActivity.this,"je suis dans error",Toast.LENGTH_SHORT).show();
            }
        }).execute();




        timer = new CountDownTimer(30000, 1000) { // adjust the milli seconds here



            public void onTick(long millisUntilFinished) {

                text1.setText("" + millisUntilFinished / 1000);

            }

            public void onFinish() {
                finish = 1;
                text1.setText("done!");
                mServ.pauseMusic();
                mp = MediaPlayer.create(GameBoardActivity.this, R.raw.horn);
                mp.start();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                v.vibrate(2000);
                next.setVisibility(View.VISIBLE);
            }
        }.start();

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
        mServ.onDestroy();
        if (mServ.isMusicchangee(mServ.personelMusique) == false) {
            mServ.choix = 0;
            mServ.onCreate();
            Toast.makeText(this,"Tu est dans le chpix 0",Toast.LENGTH_SHORT).show();
        } else {
            //parametre.onChoixMusic();
            Toast.makeText(this,"tu es dans le choix 2",Toast.LENGTH_SHORT).show();
            mServ.onCreate();
        }

        timer.cancel();
        //Toast.makeText(this,"lol",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void reload(View view) {
        if (!isConnected()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Attention les Données mobile ou la WiFi ont été désactivé veuillez réactiver l'accés à internet.")
                    .setTitle("Impossible de se connecter")
                    .setCancelable(false)
                    .setPositiveButton("Activer",
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
            mServ.length = 0;
            finish();
            Intent jeu = new Intent(this, GameBoardActivity.class);
            startActivity(jeu);
        }
    }
}
