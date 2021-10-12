package mx.ssp.conforseg.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Supervision extends AppCompatActivity {
    Button btnElegirSupervision;
    Spinner spSupervision;
    String descServicio;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    ImageView imgLogOutWhite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);
        btnElegirSupervision = findViewById(R.id.btnElegirSupervision);
        spSupervision = findViewById(R.id.spSupervision);
        imgLogOutWhite = findViewById(R.id.imgLogOutWhite);
        ListServicios();

        btnElegirSupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descServicio = (String) spSupervision.getSelectedItem();
                if(descServicio.isEmpty()){
                    Toast.makeText(getApplicationContext(),"LO SENTIMOS, ES NECESARIO SELECCIONAR UN SERVICIO.",Toast.LENGTH_SHORT).show();
                }else{
                    guardarServicio();
                    Intent i = new Intent( Supervision.this, Servicios.class);
                    startActivity(i);
                    finish();
                }
            }
        });

        imgLogOutWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarDatos();
                Intent i = new Intent(Supervision.this,Login.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**************** SPINNER **************************************/
    private void ListServicios() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllTempoServiciosSup();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_layout, R.id.txt, list);
        spSupervision.setAdapter(adapter);
    }

    private void guardarServicio() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("Servicio", descServicio );
        editor.apply();
    }

    private void eliminarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        editor = share.edit();
        editor.remove("Servicio").apply();
        editor.remove("Usuario").apply();
    }
}