package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
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
    private CountDownTimer timer;

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
       mServ.choix = 0;
        mServ.onCreate();
        timer.cancel();
        Toast.makeText(this,"lol",Toast.LENGTH_SHORT).show();
        finish();
    }

    public void reload(View view) {
        mServ.length = 0;
        finish();
        Intent jeu = new Intent(this, GameBoardActivity.class);
        startActivity(jeu);
    }




}
