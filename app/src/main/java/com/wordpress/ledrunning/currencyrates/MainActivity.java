package com.wordpress.ledrunning.currencyrates;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import Callbacks.ICustomCallback;
import Dialogs.ValueInputDialog;
import Network.AppStatus;

public class MainActivity extends AppCompatActivity {

    // Get data from RBC https://www.cbr-xml-daily.ru/daily_json.js
    // Currency data

    private ArrayList<CurrencyRateModel> listOfCurrency;
    private CustomAdapter myAdapter;
    final String TAG = "Network Activity Log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // async TASK Run
        registerReceiver(new NetworkChangeReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        /*
            GetRbcExchangeRates async = new GetRbcExchangeRates();
            async.execute();
         */
    }


    ICustomCallback myCallback = new ICustomCallback() {

        @Override
        public void resultValue(double value) {

            Toast.makeText(MainActivity.this, "value: " + value, Toast.LENGTH_LONG).show();

            for(int i = 0, size = listOfCurrency.size(); i < size; i++) {

                CurrencyRateModel item = listOfCurrency.get(i);
                item.setRate(item.getValue() * value / item.getNominal());
            }
            myAdapter.notifyDataSetChanged();
            //myAdapter.notifyDataSetInvalidated(); Мгновенное обновление;
        }
    };

    public class GetRbcExchangeRates extends AsyncTask<String, String, String> {

        // private ArrayList<CurrencyRateModel> listOfCurrency = new ArrayList<>();
        private final String urlAddress = "https://www.cbr-xml-daily.ru/daily_json.js";

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader bufferedReader = null;
            StringBuilder jSonResult = new StringBuilder();

            try {

                    URL url = new URL(urlAddress);
                    connection = (HttpURLConnection)url.openConnection();
                    connection.connect();

                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String line = "";

                    while((line = bufferedReader.readLine()) != null) {

                        jSonResult.append(line);

                    }

            }
            catch(Exception e) {
                e.printStackTrace();
               // publishProgress("MESSAGE", "Отсутствует подключение к интернет!");
            }
            finally {

                if(connection != null)
                    connection.disconnect();

                try {
                    if(bufferedReader !=null)
                        bufferedReader.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }

            }

            listOfCurrency = new ArrayList<>();

            try {

                JSONObject rbcGETRates = new JSONObject(jSonResult.toString());
                JSONObject jSonValute = rbcGETRates.getJSONObject("Valute");
                Iterator<String> arrayKey = jSonValute.keys();

                while(arrayKey.hasNext()) {

                    String key = arrayKey.next();
                    JSONObject jSonItem = jSonValute.getJSONObject(key);
                    CurrencyRateModel currencyitems = new CurrencyRateModel();
                    currencyitems.setId(jSonItem.getString("ID"));
                    currencyitems.setNameCode(jSonItem.getString("NumCode"));
                    currencyitems.setCharCode(jSonItem.getString("CharCode"));
                    currencyitems.setNominal(jSonItem.getInt("Nominal"));
                    currencyitems.setName(jSonItem.getString("Name"));
                    currencyitems.setValue(jSonItem.getDouble("Value"));
                    currencyitems.setPrevious(jSonItem.getDouble("Previous"));
                    listOfCurrency.add(currencyitems);
                }
            }
            catch (JSONException e) {

                e.printStackTrace();
            }

               return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if(values[0].equals("MESSAGE")) {
                Toast.makeText(MainActivity.this, values[1], Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            ListView lsView = (ListView)findViewById(R.id.currencyView);
            myAdapter = new CustomAdapter(MainActivity.this, listOfCurrency);

           // if (AppStatus.getInstance(MainActivity.this).isOnline()) {

                lsView.setAdapter(myAdapter);
                myAdapter.notifyDataSetChanged();
           // }


            lsView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    FragmentManager fgManager = getSupportFragmentManager();
                    ValueInputDialog dialog = new ValueInputDialog();

                    dialog.inputValueDialogInit(myCallback);
                    dialog.show(fgManager, "");
                }
            });

        }
    }


    public class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() ||
                    mobile != null && mobile.isConnectedOrConnecting();
            if (isConnected) {
                // Тут и запускаю обносление списка
                // async TASK Run
                GetRbcExchangeRates async = new GetRbcExchangeRates();
                async.execute();
                Toast.makeText(context, "YES", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "YES");

            } else {
                Toast.makeText(context, "NO", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "NO");
            }
        }
    }
}
