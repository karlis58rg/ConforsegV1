package mx.ssp.conforseg.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.Modelo.ModeloAcceso;
import mx.ssp.conforseg.Modelo.ModeloIncidencias;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Incidencias extends AppCompatActivity {
    Button btnIncidencias;
    ImageView imgMicrofonoIncidencias;
    EditText txtObservacionesIncidencias;
    TextView lblServiciosIncidencias;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    SharedPreferences share;
    String cargarServicio,cargarUsuario,
    latitud = "null",longitud="null";
    ImageView imgHomeWhiteIncidencias,imgLogOutWhiteIncidencias;
    /*************************************************************/
    Double lat,lon;
    String mensaje1,mensaje2;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);
        cargarDatos();
        locationStart();

        btnIncidencias = findViewById(R.id.btnIncidencias);
        txtObservacionesIncidencias = findViewById(R.id.txtObservacionesIncidencias);
        imgMicrofonoIncidencias = findViewById(R.id.imgMicrofonoIncidencias);
        lblServiciosIncidencias = findViewById(R.id.lblServiciosIncidencias);
        lblServiciosIncidencias.setText(cargarServicio);
        imgHomeWhiteIncidencias = findViewById(R.id.imgHomeWhiteIncidencias);
        imgLogOutWhiteIncidencias = findViewById(R.id.imgLogOutWhiteIncidencias);

        btnIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                longitud = String.valueOf(lon);
                latitud = String.valueOf(lat);
                if(txtObservacionesIncidencias.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, DEBE AGREGAR LOS HECHOS PARA PODER CONTINUAR", Toast.LENGTH_SHORT).show();
                }else if(latitud.equals("null")&&longitud.equals("null")){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, DEBE HABILITAR LOS SERVICIOS DE GPS. ", Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                    insertIncidencias();
                }
            }
        });

        imgMicrofonoIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        imgHomeWhiteIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Incidencias.this, Servicios.class);
                startActivity(i);
                finish();
            }
        });

        imgLogOutWhiteIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Incidencias.this, Supervision.class);
                startActivity(i);
                finish();
            }
        });
    }
    /**************Método que inicia el intent para de grabar la voz*****************/
    private void iniciarEntradadeVoz() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"COMIENZA A HABLAR AHORA");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    /*******Almacena la Respuesta de la lectura de voz y la coloca en el Cuadro de Texto Correspondiente ********/
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case REQ_CODE_SPEECH_INPUT:{
                if (resultCode== RESULT_OK && null != data)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String textoActual = txtObservacionesIncidencias.getText().toString();
                    txtObservacionesIncidencias.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

    private void insertIncidencias() {
        System.out.println("INSERT");
        DataHelper dataHelper = new DataHelper(getApplication());
        int idDescServicio = dataHelper.getIdTempoServiciosSup(cargarServicio);
        String idServicio = String.valueOf(idDescServicio);

        longitud = String.valueOf(lon);
        latitud = String.valueOf(lat);

        ModeloIncidencias modeloIncidencias = new ModeloIncidencias
                ("1", idServicio, txtObservacionesIncidencias.getText().toString(),
                        latitud,longitud, cargarUsuario);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdNota", modeloIncidencias.getIdNota())
                .add("IdServicio", modeloIncidencias.getIdServicio())
                .add("Nota", modeloIncidencias.getNota())
                .add("Latitud", modeloIncidencias.getLatitud())
                .add("Longitud", modeloIncidencias.getLongitud())
                .add("Usuario", modeloIncidencias.getUsuario())
                .build();
        Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Incidencias/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Incidencias.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                txtObservacionesIncidencias.setText("");
                                Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                /*toast = Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                if( v != null) v.setGravity(Gravity.CENTER);
                                toast.show();*/
                            }else{
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, SU INFORMACIÓN NO PUDO SER ENVIADA CORRECTAMENTE, FAVOR DE INTENTARLO NUEVAMENTE ", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarServicio = share.getString("Servicio", "SIN INFORMACION");
        cargarUsuario = share.getString("Usuario", "SIN INFORMACION");
    }

    /*********************Apartir de aqui empezamos a obtener la direciones y coordenadas************************/
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Incidencias.Localizacion Local = new Incidencias.Localizacion();
        Local.setIncidencias(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        mensaje1 = "Localizacion agregada";
        mensaje2 = "";
        Log.i("HERE", mensaje1);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    /**************************Aqui empieza la Clase Localizacion********************************/
    public class Localizacion implements LocationListener {
        Incidencias incidencias;

        public Incidencias getIncidencias() {
            return incidencias;
        }

        public void setIncidencias(Incidencias incidencias1) {
            this.incidencias = incidencias1;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            lat = loc.getLatitude();
            lon = loc.getLongitude();
            System.out.println("Lat = " + loc.getLatitude() + "\n Long = " + loc.getLongitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            mensaje1 = "GPS Desactivado";
        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            mensaje1 = "GPS Activado";
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

}