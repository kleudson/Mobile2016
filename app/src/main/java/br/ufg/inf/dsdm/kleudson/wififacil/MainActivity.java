package br.ufg.inf.dsdm.kleudson.wififacil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends AppCompatActivity {

    boolean redeEncontrada = true;
    private Toolbar mToolbar;

//    private Button buttonConectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Activity activity = this;
/*        buttonConectar = (Button)this.findViewById(R.id.btnConectar);
        buttonConectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/



        Toolbar mToolbar = (Toolbar) findViewById(R.id.tbMain);
        mToolbar.setTitle("WiFi Fácil");
        mToolbar.setLogo(R.drawable.ic_logo);
/*        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem menuItem){
                Intent intent = null;
                switch (menuItem.getItemId()){
                    case R.id.plus:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://www.facebook.com"));
                    break;
                }

                return  true;
            }
        });*/
        setSupportActionBar(mToolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                Snackbar.make(view, "Ativando Leitor QRCODE", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Aproxime do QRCode WiFi");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String ssid = "";
        String senha = "";
        String conteudo = "";
        String [] conteudoComSplit;
        if(result != null) {
            if(result.getContents() == null) {
                Log.d("MainActivity", "Leitura Cancelada");
                Toast.makeText(this, "Leitura Cancelada", Toast.LENGTH_LONG).show();
            } else {
                conteudo = result.getContents();

                if (!conteudo.substring(0,4).equals("WIFI")){
                    Toast.makeText(this,"QRCode Inválido!!!",Toast.LENGTH_LONG).show();
                } else {
                    conteudoComSplit = conteudo.split(";");
                    ssid = conteudoComSplit[0];
                    ssid = ssid.substring(7, ssid.length());
                    senha = conteudoComSplit[2];
                    senha = senha.substring(2, senha.length());
//                    Toast.makeText(this, "Conteúdo: " + result.getContents(), Toast.LENGTH_LONG).show();

                    Connection(ssid,senha);

                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }

}

    public void Connection(String ssid, String password){

        WifiConfiguration wifiConfiguration = new WifiConfiguration();

        wifiConfiguration.SSID = "\"".concat(ssid).concat("\"");
        wifiConfiguration.status = WifiConfiguration.Status.DISABLED;
        wifiConfiguration.priority = 40;

        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        wifiConfiguration.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
        wifiConfiguration.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        wifiConfiguration.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.RSN); // For WPA2
        wifiConfiguration.allowedProtocols.set(WifiConfiguration.Protocol.WPA); // For WPA


        wifiConfiguration.preSharedKey = "\"".concat(password).concat("\"");

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        int statusWiFiOriginal;

        if (!wifiManager.isWifiEnabled()){
            Toast.makeText(getApplicationContext(), "Ativando WIFI...", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
            statusWiFiOriginal = 0;
        } else {
            statusWiFiOriginal = 1;
        }

        int networkId = wifiManager.addNetwork(wifiConfiguration);


        Log.d("MainActivity","Valor networkID: " + networkId);

        if (networkId == -1){
            redeEncontrada = false;
        } else {
            redeEncontrada = true;
        }

            if (redeEncontrada == false) {
                Toast.makeText(getApplicationContext(), "Não foi possível conectar!", Toast.LENGTH_SHORT).show();
                if (statusWiFiOriginal == 0) {
                    Toast.makeText(getApplicationContext(), "Desativando WIFI...", Toast.LENGTH_LONG).show();
                    wifiManager.setWifiEnabled(false);
                }
            } else {
                wifiManager.enableNetwork(networkId, true);
                Toast.makeText(getApplicationContext(), "Conectado na rede '" + ssid + "' com Sucesso!!!", Toast.LENGTH_LONG).show();
            }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.plus) {
            Intent intent = new Intent(this, ActCadastro.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
