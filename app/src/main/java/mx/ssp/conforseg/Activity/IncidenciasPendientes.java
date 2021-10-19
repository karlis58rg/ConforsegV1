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
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.Utilidades.Funciones;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class IncidenciasPendientes extends AppCompatActivity {
    ListView listAsistenciasPendientes;
    EditText txtObservacionesIncidenciasP;
    Button btnIncidenciasP;
    ImageView imgMicrofonoIncidenciasP,imgHomeWhiteIncidenciasP;
    private static final  int REQ_CODE_SPEECH_INPUT=100;
    String cargarServicio, cargarUsuario,latitud = "null", longitud = "null";
    Double lat, lon;
    String mensaje1, mensaje2;
    SharedPreferences share;
    ArrayList<String> ListaFolio,ListaFecha,ListaUsuario,ListaNota;
    String[] ArrayListaFolio;
    Funciones funciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias_pendientes);
        cargarDatos();
        cargarIncidenciasPendientes();
        listAsistenciasPendientes = findViewById(R.id.listAsistenciasPendientes);
        txtObservacionesIncidenciasP = findViewById(R.id.txtObservacionesIncidenciasP);
        btnIncidenciasP = findViewById(R.id.btnIncidenciasP);
        imgMicrofonoIncidenciasP = findViewById(R.id.imgMicrofonoIncidenciasP);
        imgHomeWhiteIncidenciasP = findViewById(R.id.imgHomeWhiteIncidenciasP);
        funciones = new Funciones();

        btnIncidenciasP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                longitud = String.valueOf(lon);
                latitud = String.valueOf(lat);
                if(txtObservacionesIncidenciasP.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, DEBE AGREGAR LOS HECHOS PARA PODER CONTINUAR", Toast.LENGTH_SHORT).show();
                }else if(latitud.equals("null")&&longitud.equals("null")){
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, DEBE HABILITAR LOS SERVICIOS DE GPS. ", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imgMicrofonoIncidenciasP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarEntradadeVoz();
            }
        });

        imgHomeWhiteIncidenciasP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(IncidenciasPendientes.this, Servicios.class);
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
                    String textoActual = txtObservacionesIncidenciasP.getText().toString();
                    txtObservacionesIncidenciasP.setText(textoActual+" " + result.get(0));
                }
                break;
            }
        }
    }

    /*********************Apartir de aqui empezamos a obtener la direciones y coordenadas************************/
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        IncidenciasPendientes.Localizacion Local = new IncidenciasPendientes.Localizacion();
        Local.setIncidenciasPendientes(this);
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
        IncidenciasPendientes incidenciasPendientes;

        public IncidenciasPendientes getIncidenciasPendientes() {
            return incidenciasPendientes;
        }

        public void setIncidenciasPendientes(IncidenciasPendientes incidenciasPendientes1) {
            this.incidenciasPendientes = incidenciasPendientes1;
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

    //***************** CONSULTA A LA BD MEDIANTE EL WS **************************//
    public void cargarIncidenciasPendientes() {
        System.out.println("BUSCANDO DATOS");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/IncidenciasPendientes?usuario=" + cargarUsuario)
                .build();

        System.out.println("BUSCANDO DATOS RESPUESTA");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL CONSULTAR OBJETOS, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    try {
                        IncidenciasPendientes.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                ListaFolio = new ArrayList<String>();
                                ListaFecha = new ArrayList<String>();
                                ListaUsuario = new ArrayList<String>();
                                ListaNota = new ArrayList<String>();

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
                                    String[] ArrayIncidenciasPendientesList = ArregloJson.split(Pattern.quote("},"));
                                    ArrayListaFolio = ArrayIncidenciasPendientesList;

                                    Log.i("Asistencia", "ArrayListaDelictivo:" + ArrayIncidenciasPendientesList[0]);

                                    //RECORRE EL ARREGLO PARA AGREGAR EL FOLIO CORRESPONDIENTE DE CADA OBJETJSN
                                    int contadordeDetenido = 0;
                                    while (contadordeDetenido < ArrayListaFolio.length) {
                                        try {
                                            JSONObject jsonjObject = new JSONObject(ArrayListaFolio[contadordeDetenido] + "}");
                                            //ListaIdAsistencia.add(jsonjObject.getString("IdVigilante"));
                                            ListaFolio.add(
                                                    ((jsonjObject.getString("Folio")).equals("null") ? " - " : jsonjObject.getString("Folio"))
                                            );
                                            ListaFecha.add(
                                                    ((jsonjObject.getString("Fecha")).equals("null") ? " - " : jsonjObject.getString("Fecha"))
                                            );
                                            ListaUsuario.add(
                                                    ((jsonjObject.getString("Usuario")).equals("null") ? " - " : jsonjObject.getString("Usuario"))
                                            );
                                            ListaNota.add(
                                                    ((jsonjObject.getString("Nota")).equals("null") ? " - " : jsonjObject.getString("Nota"))
                                            );
                                            //ListaRadioBoolean.add("NO");

                                        } catch (JSONException e) {
                                            Log.i("ASISTENCIAS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                IncidenciasPendientes.MyAdapter adapter = new IncidenciasPendientes.MyAdapter(getApplicationContext(),ListaFolio, ListaFecha, ListaUsuario,ListaNota);
                                listAsistenciasPendientes.setAdapter(adapter);
                                funciones.ajustaAlturaListView(listAsistenciasPendientes,250);
                                //*************************
                            }
                        });
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "ERROR AL CONSULTAR ANEXO C, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarServicio = share.getString("Servicio", "SIN INFORMACION");
        cargarUsuario = share.getString("Usuario", "SIN INFORMACION");
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaFolioIP, ListaFechaIP, ListaUsuarioIP,ListaNotasIP;

        MyAdapter(Context c,ArrayList<String> ListaFolio, ArrayList<String> ListaFecha, ArrayList<String> ListaUsuario, ArrayList<String> ListaNota) {
            super(c, R.layout.row_incidencias_pendientes, ListaFolio);
            this.context = c;
            this.ListaFolioIP = ListaFolio;
            this.ListaFechaIP = ListaFecha;
            this.ListaUsuarioIP = ListaUsuario;
            this.ListaNotasIP = ListaNota;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Log.i("ASISTENCIAS", "ADAPTER");

            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row_incidencias_pendientes, parent, false);

            TextView lblFolioIP = row.findViewById(R.id.lblFolioIP);
            TextView lblFechaIP = row.findViewById(R.id.lblFechaIP);
            TextView lblUserIP = row.findViewById(R.id.lblUserIP);
            TextView lblNotaIP = row.findViewById(R.id.lblNotaIP);


            // Asigna los valores
            lblFolioIP.setText(ListaFolioIP.get(position));
            lblFechaIP.setText(ListaFechaIP.get(position));
            lblUserIP.setText(ListaUsuarioIP.get(position));
            lblNotaIP.setText(ListaNotasIP.get(position));

            return row;
        }

    }
}