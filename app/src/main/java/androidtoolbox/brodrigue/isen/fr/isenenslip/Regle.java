package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class Regle extends AppCompatActivity {

    Button pascompris;
    Button btnAh;
    private Context context;
    private MyService mServ;
    public boolean mIsBound;
    Intent music;

    private ServiceConnection Scon =new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder
                binder) {
            mServ = ((MyService.ServiceBinder ) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        bindService(new Intent(this,MyService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regle);
        pascompris = (Button) findViewById(R.id.btnPascompris);
        btnAh = (Button) findViewById(R.id.ah);
        MediaPlayer.create(this, R.raw.denisbrogniart);

        mServ = new MyService();
        music = new Intent();
        music.setClass(this, MyService.class);
        doBindService();
        startService(music);
    }

    public void Popup(View v) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(Regle.this);
            LayoutInflater factory = LayoutInflater.from(Regle.this);
            View view = factory.inflate(R.layout.popupwindow, null);
            alert.setView(view);
            alert.show();
    }

    public void goToAccueil(View v){
       finish();
    }

    public void onAh(View v){
        MediaPlayer Ahsound = MediaPlayer.create(this, R.raw.denisbrogniart);
        Ahsound.start();
        finish();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsBound) {
            mServ.pauseMusic();
            mIsBound = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!mIsBound) {
            mServ.resumeMusic();
            mIsBound = true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}

