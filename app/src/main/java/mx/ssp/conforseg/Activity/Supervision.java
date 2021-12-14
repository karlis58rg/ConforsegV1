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
    Button btnCargarServicios,btnElegirSupervision;
    Spinner spSupervision;
    String descServicio = "";
    int bandera = 0,cargarBandera = 0;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    ImageView imgLogOutWhite;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervision);
        btnElegirSupervision = findViewById(R.id.btnElegirSupervision);
        spSupervision = findViewById(R.id.spSupervision);
        imgLogOutWhite = findViewById(R.id.imgLogOutWhite);
        btnCargarServicios = findViewById(R.id.btnCargarServicios);
        cargarDatos();
        ListServicios();

        if(cargarBandera == 1){
            btnCargarServicios.setVisibility(View.GONE);
        }else{
            btnCargarServicios.setVisibility(View.VISIBLE);
        }

        btnCargarServicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bandera = 1;
                guardarBanderaServicio();
                ListServicios();
                btnCargarServicios.setVisibility(View.GONE);
            }
        });

        btnElegirSupervision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(spSupervision.getSelectedItem() == null && bandera == 0){
                    Toast.makeText(getApplicationContext(),"LO SENTIMOS, ES NECESARIO CARGAR LOS SERVICIOS Y POSTERIORMENTE SELECCIONAR UN SERVICIO.",Toast.LENGTH_SHORT).show();
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
                deleteServicios();
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
        list = dataHelper.getAllTempoServiciosSup();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SERVICIOS");
            Toast.makeText(getApplication(), "CUENTA CON SERVICIOS ACTIVOS.", Toast.LENGTH_LONG).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplication(), R.layout.spinner_layout, R.id.txt, list);
            adapter.notifyDataSetChanged();
            spSupervision.setAdapter(adapter);
            System.out.println("LISTA"+list);
        }else{
            Toast.makeText(getApplication(), "LO SENTIMOS, NO CUENTA CON SERVICIOS ACTIVOS.", Toast.LENGTH_LONG).show();
        }
    }

    private void guardarServicio() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("Servicio", descServicio );
        editor.apply();
    }
    private void guardarBanderaServicio() {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putInt("ServicioCargado", bandera );
        editor.apply();
    }

    private void eliminarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        editor = share.edit();
        editor.remove("Servicio").apply();
        editor.remove("Usuario").apply();
        editor.remove("ServicioCargado").apply();
    }

    private boolean deleteServicios() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        boolean delete = dataHelper.DeleteTempoServiciosSup();
        System.out.println(delete);
        System.out.println("ELIMINANDO REGISTRO SQLITE");
        return  delete;
    }

    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarBandera = share.getInt("ServicioCargado", 0);
    }

}