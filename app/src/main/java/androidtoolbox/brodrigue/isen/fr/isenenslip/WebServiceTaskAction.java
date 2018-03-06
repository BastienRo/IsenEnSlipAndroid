package androidtoolbox.brodrigue.isen.fr.isenenslip;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by brodrigue on 06/03/2018.
 */

public class WebServiceTaskAction extends AsyncTask<String, String , String> {
        HttpURLConnection urlConnection;


        Random rand = new Random();
        int n = rand.nextInt(4);
        private CallBackInterface callBackInterface;

        public WebServiceTaskAction(CallBackInterface callBackInterfaceImplementation) {
                this.callBackInterface = callBackInterfaceImplementation;
        }
        @Override
        protected String doInBackground(String... args) {

                StringBuilder result = new StringBuilder();
                try {
                        URL url = new URL("https://isenenslipapi.herokuapp.com/action/"+n);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        String line;
                        while ((line = reader.readLine()) != null) {
                                result.append(line);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                } finally {
                        urlConnection.disconnect();
                }
                return result.toString();
        }
        @Override
        protected void onPostExecute(String aString) {
                if (aString != null && !aString.isEmpty()) {
                        callBackInterface.succes(aString);
                }else {
                        callBackInterface.error();
                }
        }
}