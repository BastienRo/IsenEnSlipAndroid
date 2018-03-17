package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Affiche_jeu_test extends AppCompatActivity {

    private TextView tvPersonne;
    private TextView tvAction;
    Context context;
    public Personne users;
    public Action action;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affiche_jeu_test);
        context = this;
        tvPersonne = (TextView) findViewById(R.id.tvPersonne);
        tvAction = (TextView) findViewById(R.id.tvAction);

        progressDialog = new ProgressDialog(Affiche_jeu_test.this);
        progressDialog.setMessage("Récupération des datas");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Handler updateHandler = new Handler();

        updateHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                progressDialog.cancel();
            }
        }, 5000);


        new WebServiceTaskAction(new CallBackInterface() {
            @Override
            public void succes(String json) {
                Gson gson = new GsonBuilder().create();
                action = gson.fromJson(json, Action.class);

                tvAction.setText(action.getActivity());
            }

            @Override
            public void error() {
                Toast.makeText(Affiche_jeu_test.this,"je suis dans error",Toast.LENGTH_SHORT).show();
            }
        }).execute();

        new WebServiceTask(new CallBackInterface() {
            @Override
            public void succes(String json) {
                Gson gson = new GsonBuilder().create();
                users = gson.fromJson(json, Personne.class);

                tvPersonne.setText(users.getName());
            }

            @Override
            public void error() {
                Toast.makeText(Affiche_jeu_test.this,"je suis dans error",Toast.LENGTH_SHORT).show();
            }
        }).execute();
    }
}
