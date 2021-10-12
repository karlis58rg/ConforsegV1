package mx.ssp.conforseg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
    ArrayList<String> ListaIdAsistencia,ListaAsistencia;
    String[] ArrayListaAsistencia;
    SharedPreferences share;
    String cargarServicio, cargarUsuario,idAsistenciaUser,fecha,varAsistencia,
            latitud = "null", longitud = "null";
    Funciones funciones;
    /*************************************************************/
    Double lat, lon;
    String mensaje1, mensaje2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asistencia);
        cargarDatos();
        cargarAsistencia();
        lblServiciosAsistencia = findViewById(R.id.lblServiciosAsistencia);
        btnAsistencias = findViewById(R.id.btnAsistencias);
        listAsistenciasPrincipal = findViewById(R.id.listAsistenciasPrincipal);
        funciones = new Funciones();

        lblServiciosAsistencia.setText(cargarServicio);

        btnAsistencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listAsistenciasPrincipal.getAdapter().getCount();
                System.out.println("REGISTROS TOTALES " + listAsistenciasPrincipal.getAdapter().getCount());
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
                .url("http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/Vigilantes?tipoServicio=" + idServicio)
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
                        Asistencia.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String resp = myResponse;
                                ListaIdAsistencia = new ArrayList<String>();
                                ListaAsistencia = new ArrayList<String>();
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

                                        } catch (JSONException e) {
                                            Log.i("ASISTENCIAS", "catch:" + e.toString());

                                            e.printStackTrace();
                                            Toast.makeText(getApplicationContext(), "ERROR AL DESEREALIZAR EL JSON", Toast.LENGTH_SHORT).show();
                                        }
                                        contadordeDetenido++;
                                    }
                                }

                                //AGREGA LOS DATOS AL LISTVIEW MEDIANTE EL ADAPTADOR
                                Asistencia.MyAdapter adapter = new Asistencia.MyAdapter(getApplicationContext(),ListaIdAsistencia, ListaAsistencia, "Nombre");
                                listAsistenciasPrincipal.setAdapter(adapter);
                                funciones.ajustaAlturaListView(listAsistenciasPrincipal,250);
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

    private void insertAsistencias() {
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
                (idServicio, idAsistenciaUser, fecha,
                        varAsistencia, longitud, latitud, cargarUsuario);

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
                .url("http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/Asistencias/")
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

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        ArrayList<String> ListaIdAsistenciaAr,ListaAsistenciasAr;
        String titulo;

        MyAdapter(Context c,ArrayList<String> ListaIdAsistencia, ArrayList<String> ListaAsistencias, String titulo) {
            super(c, R.layout.row_asistencia, ListaAsistencias);
            this.context = c;
            this.ListaIdAsistenciaAr = ListaIdAsistencia;
            this.ListaAsistenciasAr = ListaAsistencias;
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

            // Asigna los valores
            lblIUser.setText(ListaIdAsistenciaAr.get(position));
            lblDatosUser.setText(ListaAsistenciasAr.get(position));

            return row;
        }

    }
}