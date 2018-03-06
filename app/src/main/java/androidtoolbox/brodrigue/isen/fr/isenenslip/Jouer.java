package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Jouer extends AppCompatActivity {

    public boolean mIsBound = false;
    private MyService mServ;

    MediaPlayer Soundbutton;
    Button commencer;
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
    Spinner nbjoeur;
    ArrayAdapter<CharSequence> adapter;
    @Override    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jouer);
        mServ = new MyService();
        music = new Intent();
        music.setClass(this, MyService.class);
        doBindService();
        startService(music);

        commencer = (Button)findViewById(R.id.btnCommencer);
        nbjoeur = (Spinner) findViewById(R.id.nbjoueur);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.nbjoueur, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        nbjoeur.setAdapter(adapter);
    }

    public void onCommencer(View v)
    {
        Intent jeu = new Intent(this, Affiche_jeu_test.class);
        startActivity(jeu);
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
       finish();
    }
}

