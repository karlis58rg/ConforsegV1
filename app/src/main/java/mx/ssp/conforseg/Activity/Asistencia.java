package mx.ssp.conforseg.Activity;

import androidx.annotation.NonNull;
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
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.Modelo.ModeloAsistencia;
import mx.ssp.conforseg.Modelo.ModeloIncidencias;
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

public class Asistencia extends AppCompatActivity {

    Button btnAsistencias;
    TextView lblServiciosAsistencia;
    ListView listAsistenciasPrincipal;
    ArrayList<String> ListaIdAsistencia,ListaAsistencia,ListaRadioBoolean;
    String[] ArrayListaAsistencia;
    SharedPreferences share;
    String cargarServicio, cargarUsuario,fecha,
            latitud = "null", longitud = "null";
    Funciones funciones;
    ImageView imgHomeWhiteAsistencia;
    /*************************************************************/
    Double lat, lon;
    String mensaje1, mensaje2;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
        cargarDatos();
        cargarAsistencia();
        locationStart();
        lblServiciosAsistencia = findViewById(R.id.lblServiciosAsistencia);
        btnAsistencias = findViewById(R.id.btnAsistencias);
        listAsistenciasPrincipal = findViewById(R.id.listAsistenciasPrincipal);
        imgHomeWhiteAsistencia = findViewById(R.id.imgHomeWhiteAsistencia);
        funciones = new Funciones();
        funciones.Procesando(Asistencia.this,"","CARGANDO ASISTENCIAS, UN MOMENTO POR FAVOR...");
        lblServiciosAsistencia.setText(cargarServicio);

        btnAsistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                /*toast = Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT);
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                toast.show();*/
                for(int i = 0; i< listAsistenciasPrincipal.getCount();i++)
                {
                    Log.i("LIBRETA",ListaIdAsistencia.get(i));
                    Log.i("LIBRETA",ListaAsistencia.get(i));
                    Log.i("LIBRETA",ListaRadioBoolean.get(i));
                    insertAsistencias(ListaIdAsistencia.get(i),ListaRadioBoolean.get(i));
                }
            }
        });

        imgHomeWhiteAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Asistencia.this, Servicios.class);
                startActivity(i);
                finish();
            }
        });
    }

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    public void cargarAsistencia() {
        System.out.println("BUSCANDO DATOS");
        DataHelper dataHelper = new DataHelper(getApplication());
        int idDescServicio = dataHelper.getIdTempoServiciosSup(cargarServicio);
        String idServicio = String.valueOf(idDescServicio);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Vigilantes?tipoServicio=" + idServicio)
                .build();

        System.out.println("BUSCANDO DATOS RESPUESTA");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL CONSULTAR OBJETOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                funciones.ProcesandoDissmis(Asistencia.this);
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    try {
                        Asistencia.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                ListaIdAsistencia = new ArrayList<String>();
                                ListaAsistencia = new ArrayList<String>();
                                ListaRadioBoolean = new ArrayList<String>();
                                Log.i("ASISTENCIA", "RESP:" + resp);

                                //***************** RESPUESTA DEL WEBSERVICE **************************//

                                //CONVERTIR ARREGLO DE JSON A OBJET JSON
                                String ArregloJson = resp.replace("[", "");
                                ArregloJson = ArregloJson.replace("]", "");

                                if (ArregloJson.equals("")) {
                                    //Sin Información. Todos los campos vacíos.
                                    Log.i("ASISTENCIA", "SIN INFORMACIÓN");

                                } else {
                                    Log.i("ASISTENCIA", "CON INFORMACIÓN:");

                                    //SEPARAR CADA detenido EN UN ARREGLO
                                    String[] ArrayAsistencia = ArregloJson.split(Pattern.quote("},"));
                                    ArrayListaAsistencia = ArrayAsistencia;

                                    Log.i("Asistencia", "ArrayListaDelictivo:" + ArrayAsistencia[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido = 0;
                                    while (contadordeDetenido < ArrayListaAsistencia.length) {
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaAsistencia[contadordeDetenido] + "}");
                                            ListaIdAsistencia.add(jsonjObject.getString("IdVigilante"));
                                            ListaAsistencia.add(
                                                            ((jsonjObject.getString("Nombre")).equals("null") ? " - " : jsonjObject.getString("Nombre")) +
                                                                    " "+((jsonjObject.getString("APaterno")).equals("null")?"":jsonjObject.getString("APaterno"))
                                            );
                                            ListaRadioBoolean.add("NO");

                                        } catch (JSONException e) {
                                            Log.i("ASISTENCIAS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                Asistencia.MyAdapter adapter = new Asistencia.MyAdapter(getApplicationContext(),ListaIdAsistencia, ListaAsistencia, "Nombre",ListaRadioBoolean);
                                listAsistenciasPrincipal.setAdapter(adapter);
                                funciones.ajustaAlturaListView(listAsistenciasPrincipal,440);
                                funciones.ProcesandoDissmis(Asistencia.this);
                                //*************************
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                        /*toast = Toast.makeText(getApplicationContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT);
                        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                        if( v != null) v.setGravity(Gravity.CENTER);
                        toast.show();*/
                        funciones.ProcesandoDissmis(Asistencia.this);
                    }

                }
            }
        });
    }

    private void insertAsistencias(String idUser,String asistencia) {
        System.out.println("INSERT");
        DataHelper dataHelper = new DataHelper(getApplication());
        int idDescServicio = dataHelper.getIdTempoServiciosSup(cargarServicio);
        String idServicio = String.valueOf(idDescServicio);

        longitud = String.valueOf(lon);
        latitud = String.valueOf(lat);

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);

        ModeloAsistencia modeloAsistencia = new ModeloAsistencia
                (idServicio, idUser, fecha,
                        asistencia, longitud, latitud, cargarUsuario);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdServicio", modeloAsistencia.getIdServicio())
                .add("IdVigilante", modeloAsistencia.getIdVigilante())
                .add("Fecha", modeloAsistencia.getFecha())
                .add("Asistencia", modeloAsistencia.getAsistencia())
                .add("Longitud", modeloAsistencia.getLongitud())
                .add("Latitud", modeloAsistencia.getLatitud())
                .add("Usuario", modeloAsistencia.getUsuario())
                .build();
        Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Asistencias/")
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
                    Asistencia.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                /*toast = Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                if( v != null) v.setGravity(Gravity.CENTER);
                                toast.show();*/
                            }else{
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, USTED YA COMPLETÓ EL FORMULARIO DE ASISTENCIAS DEL DÍA", Toast.LENGTH_SHORT).show();
                                /*toast = Toast.makeText(getApplicationContext(), "LO SENTIMOS, USTED YA COMPLETÓ EL FORMULARIO DE ASISTENCIAS DEL DÍA", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                if( v != null) v.setGravity(Gravity.CENTER);
                                toast.show();*/
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    /*********************Apartir de aqui empezamos a obtener la direciones y coordenadas************************/
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Asistencia.Localizacion Local = new Asistencia.Localizacion();
        Local.setAsistencia(this);
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
        Asistencia asistencia;

        public Asistencia getAsistencia() {
            return asistencia;
        }

        public void setAsistencia(Asistencia asistencia1) {
            this.asistencia = asistencia1;
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

    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarServicio = share.getString("Servicio", "SIN INFORMACION");
        cargarUsuario = share.getString("Usuario", "SIN INFORMACION");
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdAsistenciaAr,ListaAsistenciasAr,ListaRadioBoolean;
        String titulo;

        MyAdapter(Context c,ArrayList<String> ListaIdAsistencia, ArrayList<String> ListaAsistencias, String titulo, ArrayList<String> ListaRadioBoolean) {
            super(c, R.layout.row_asistencia, ListaAsistencias);
            this.context = c;
            this.ListaIdAsistenciaAr = ListaIdAsistencia;
            this.ListaAsistenciasAr = ListaAsistencias;
            this.ListaRadioBoolean = ListaRadioBoolean;
            this.titulo = titulo;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("ASISTENCIAS", "ADAPTER");

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_asistencia, parent, false);

            TextView lblIUser = row.findViewById(R.id.lblIdUserAsistencia);
            TextView lblDatosUser = row.findViewById(R.id.lblUserAsistencia);

            RadioGroup rgAsistio =row.findViewById(R.id.rgAsistio);
            RadioButton rbSi = row.findViewById(R.id.rbSi);
            RadioButton rbNo = row.findViewById(R.id.rbNo);

            rgAsistio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    if (rbSi.isChecked())
                    {
                        ListaRadioBoolean.set(position,"SI");
                    }

                    if (rbNo.isChecked())
                    {
                        ListaRadioBoolean.set(position,"NO");
                    }

                }
            });

            // Asigna los valores
            lblIUser.setText(ListaIdAsistenciaAr.get(position));
            lblDatosUser.setText(ListaAsistenciasAr.get(position));

            return row;
        }

    }
}