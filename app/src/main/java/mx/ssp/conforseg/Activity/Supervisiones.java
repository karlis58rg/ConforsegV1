package mx.ssp.conforseg.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.Modelo.ModeloAcceso;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;
import mx.ssp.conforseg.Utilidades.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Supervisiones extends AppCompatActivity {
    Button btnGuardarSuperviciones;
    TextView lblServiciosSuperviciones;
    SharedPreferences share;
    String cargarServicio,cargarUsuario,
            latitud = "null",longitud="null";
    EditText txtFechaRegistroSup,txtTaxis,txtVisitantesPeatonales,txtVisitanteenVehiculo,
            txtEmpleadasDomesticas,txtTrabajadorenVehiculo,txtTrabajadorPeatonal,txtAccesoARC,txtEmpleados,txtIncidentes,txtRecorridos,txtOtros;
    ImageView imgHomeWhiteSupervisiones,imgLogOutWhiteSupervisiones;
    Funciones funciones;
    /*************************************************************/
    Double lat,lon;
    String mensaje1,mensaje2;
    Toast toast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisiones);
        cargarDatos();
        locationStart();
        btnGuardarSuperviciones = findViewById(R.id.btnGuardarSuperviciones);
        lblServiciosSuperviciones = findViewById(R.id.lblServiciosSuperviciones);
        txtFechaRegistroSup = findViewById(R.id.txtFechaRegistroSup);
        txtTaxis = findViewById(R.id.txtTaxis);
        txtVisitantesPeatonales = findViewById(R.id. txtVisitantesPeatonales);
        txtVisitanteenVehiculo = findViewById(R.id.txtVisitanteenVehiculo);
        txtEmpleadasDomesticas = findViewById(R.id.txtEmpleadasDomesticas);
        txtTrabajadorenVehiculo = findViewById(R.id.txtTrabajadorenVehiculo);
        txtTrabajadorPeatonal= findViewById(R.id.txtTrabajadorPeatonal);
        txtAccesoARC = findViewById(R.id.txtAccesoARC);
        txtEmpleados = findViewById(R.id.txtEmpleados);
        txtIncidentes = findViewById(R.id.txtIncidentes);
        txtRecorridos = findViewById(R.id.txtRecorridos);
        txtOtros = findViewById(R.id.txtOtros);
        imgHomeWhiteSupervisiones = findViewById(R.id.imgHomeWhiteSupervisiones);
        imgLogOutWhiteSupervisiones = findViewById(R.id.imgLogOutWhiteSupervisiones);

        funciones = new Funciones();
        lblServiciosSuperviciones.setText(cargarServicio);

        txtFechaRegistroSup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.calendar(R.id.txtFechaRegistroSup,Supervisiones.this,Supervisiones.this);
            }
        });

        btnGuardarSuperviciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                longitud = String.valueOf(lon);
                latitud = String.valueOf(lat);
                if(txtFechaRegistroSup.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, LA FECHA ES NECESARIA PARA PODER CONTINUAR", Toast.LENGTH_SHORT).show();
                }else if(latitud.equals("null")&&longitud.equals("null")){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, DEBE HABILITAR LOS SERVICIOS DE GPS. ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                    funciones.Procesando(Supervisiones.this,"","ENVIANDO ACCESOS, UN MOMENTO POR FAVOR...");
                    insertAcceso();
                }
            }
        });

        imgHomeWhiteSupervisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Supervisiones.this, Servicios.class);
                startActivity(i);
                finish();
            }
        });

        imgLogOutWhiteSupervisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Supervisiones.this, Supervision.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void insertAcceso() {
        if(txtTaxis.getText().toString().isEmpty()){
            txtTaxis.setText("0");
        }
        if( txtVisitantesPeatonales.getText().toString().isEmpty())
        {
            txtVisitantesPeatonales.setText("0");
        }
        if(txtVisitanteenVehiculo.getText().toString().isEmpty()){
            txtVisitanteenVehiculo.setText("0");
        }
        if(txtEmpleadasDomesticas.getText().toString().isEmpty()){
            txtEmpleadasDomesticas.setText("0");
        }
        if(txtTrabajadorenVehiculo.getText().toString().isEmpty()){
            txtTrabajadorenVehiculo.setText("0");
        }
        if(txtTrabajadorPeatonal.getText().toString().isEmpty()) {
            txtTrabajadorPeatonal.setText("0");
        }
        if(txtAccesoARC.getText().toString().isEmpty()) {
            txtAccesoARC.setText("0");
        }
        if(txtEmpleados.getText().toString().isEmpty()){
            txtEmpleados.setText("0");
        }
        if(txtIncidentes.getText().toString().isEmpty()){
            txtIncidentes.setText("0");
        }
        if(txtRecorridos.getText().toString().isEmpty()){
            txtRecorridos.setText("0");
        }
        if(txtOtros.getText().toString().isEmpty()){
            txtOtros.setText("0");
        }

        System.out.println("INSERT");
        DataHelper dataHelper = new DataHelper(getApplication());
        int idDescServicio = dataHelper.getIdTempoServiciosSup(cargarServicio);
        String idServicio = String.valueOf(idDescServicio);

        longitud = String.valueOf(lon);
        latitud = String.valueOf(lat);
        ModeloAcceso modeloAcceso = new ModeloAcceso
                (idServicio, txtFechaRegistroSup.getText().toString(), txtTaxis.getText().toString(),
                       txtVisitantesPeatonales.getText().toString(), txtVisitanteenVehiculo.getText().toString(), txtEmpleadasDomesticas.getText().toString(),
                        txtTrabajadorenVehiculo.getText().toString(), txtTrabajadorPeatonal.getText().toString(), txtAccesoARC.getText().toString(),
                        txtEmpleados.getText().toString(), txtIncidentes.getText().toString(), txtRecorridos.getText().toString(),
                        txtOtros.getText().toString(),longitud, latitud, cargarUsuario);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdServicio", modeloAcceso.getIdServicio())
                .add("Fecha", modeloAcceso.getFecha())
                .add("Taxi", modeloAcceso.getTaxi())
                .add("VisitantesPeatonales", modeloAcceso.getVisitantesPeatonales())
                .add("VisitanteEnVehiculo", modeloAcceso.getVisitanteEnVehiculo())
                .add("EmpleadasDomesticas", modeloAcceso.getEmpleadasDomesticas())
                .add("TrabajadorEnVehiculo", modeloAcceso.getTrabajadorEnVehiculo())
                .add("TrabajadorPeatonal", modeloAcceso.getTrabajadorPeatonal())
                .add("AccesoARC", modeloAcceso.getAccesoARC())
                .add("Empleados", modeloAcceso.getEmpleados())
                .add("Incidentes", modeloAcceso.getIncidentes())
                .add("Recorridos", modeloAcceso.getRecorridos())
                .add("Otros", modeloAcceso.getOtros())
                .add("Longitud", modeloAcceso.getLongitud())
                .add("Latitud", modeloAcceso.getLatitud())
                .add("Usuario", modeloAcceso.getUsuario())
                .build();
        Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Acceso/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR SU REGISTRO, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                funciones.ProcesandoDissmis(Supervisiones.this);
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Supervisiones.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                funciones.ProcesandoDissmis(Supervisiones.this);
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                limpiarCampos();
                                Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                /*toast = Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                if( v != null) v.setGravity(Gravity.CENTER);
                                toast.show();*/
                            }else{
                                funciones.ProcesandoDissmis(Supervisiones.this);
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, USTED YA COMPLETÓ SU FORMULARIO DEL DÍA", Toast.LENGTH_SHORT).show();
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
    public void limpiarCampos(){
        txtFechaRegistroSup.setText("");
        txtTaxis.setText("");
        txtVisitantesPeatonales.setText("");
        txtVisitanteenVehiculo.setText("");
        txtEmpleadasDomesticas.setText("");
        txtTrabajadorenVehiculo.setText("");
        txtTrabajadorPeatonal.setText("");
        txtAccesoARC.setText("");
        txtEmpleados.setText("");
        txtIncidentes.setText("");
        txtRecorridos.setText("");
        txtOtros.setText("");
    }

    /*********************Apartir de aqui empezamos a obtener la direciones y coordenadas************************/
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Supervisiones.Localizacion Local = new Supervisiones.Localizacion();
        Local.setSupervisiones(this);
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
        Supervisiones supervisiones;

        public Supervisiones getSupervisiones() {
            return supervisiones;
        }

        public void setSupervisiones(Supervisiones supervisiones1) {
            this.supervisiones = supervisiones1;
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