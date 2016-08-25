package br.ufg.inf.dsdm.kleudson.wififacil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class ActCadastro extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private Button btnGerarQrCode;
    private EditText edtTxtSSID;
    private EditText edtTxtSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_cadastro);

        mToolbar = (Toolbar)findViewById(R.id.tbMain);
        btnGerarQrCode = (Button)findViewById(R.id.btnGerarQrCode);
        edtTxtSSID = (EditText)findViewById(R.id.edtTxtSSID);
        edtTxtSenha = (EditText)findViewById(R.id.edtTxtSenha);

        mToolbar.setTitle("WiFi Fácil");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnGerarQrCode.setOnClickListener(this);
    }

    public void onClick(View v) {
        String formatoWiFi = "";
        formatoWiFi = "WIFI:S:" + edtTxtSSID.getText().toString() + ";";
        formatoWiFi = formatoWiFi + "T:;";
        formatoWiFi = formatoWiFi + "P:" + edtTxtSenha.getText().toString() + ";;";
        Intent intent = new Intent(this, ActQrCode.class);
        intent.putExtra("INFORMACOESWIFI",formatoWiFi);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }
}
