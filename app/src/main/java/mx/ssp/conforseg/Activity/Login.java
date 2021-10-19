package mx.ssp.conforseg.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import mx.ssp.conforseg.Modelo.ModeloUsuarios;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;
import mx.ssp.conforseg.Utilidades.Conexion;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    Button btnIniciar;
    EditText txtUser,txtPass;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String respuestaJson,usuario,nombre,cargarUsuario;
    int idServicio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        solicitarPermisos();

        btnIniciar = findViewById(R.id.btnIniciar);
        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtUser.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL USUARIO ES NECESARIO PARA INGRESAR",Toast.LENGTH_SHORT).show();
                }else if(txtPass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"LA CONTRASEÑA ES NECESARIA PARA INGRESAR",Toast.LENGTH_SHORT).show();
                }else if(txtUser.getText().length() < 3){
                    Toast.makeText(getApplicationContext(),"EL USUARIO NO PUEDE SER MENOR A TRES LETRAS",Toast.LENGTH_SHORT).show();
                }else if(txtPass.getText().length() < 3){
                    Toast.makeText(getApplicationContext(),"LA CONTRASEÑA NO PUEDE SER MENOR A TRES LETRAS",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                    getUsuario();
                }
            }
        });
    }

    private void guardarUsuario(String Usuario) {
        share = getSharedPreferences("main", MODE_PRIVATE);
        editor = share.edit();
        editor.putString("Usuario", Usuario );
        editor.apply();
    }

    private void getUsuario() {
        ModeloUsuarios modeloUsuarios = new ModeloUsuarios(
                txtUser.getText().toString(),
                txtPass.getText().toString());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Usuarios?usuario="+modeloUsuarios.getUsuario()+"&passsword="+modeloUsuarios.getContrasena())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                guardarUsuario(txtUser.getText().toString());
                                getServicios();
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                                txtUser.setText("");
                                txtPass.setText("");
                                Intent intent = new Intent(Login.this, Supervision.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, USUARIO O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }

        });
    }

    public void getServicios() {
        cargarDatos();
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://c5.hidalgo.gob.mx/WsConforseg/api/Supervisiones?usuario="+cargarUsuario)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idServicio = (ja.getJSONObject(i).getInt("IdServicio"));
                                                usuario = (ja.getJSONObject(i).getString("Usuario"));
                                                nombre = (ja.getJSONObject(i).getString("Nombre"));
                                                dataHelper.insertTempoServiciosSup(idServicio,usuario,nombre);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }

    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarUsuario = share.getString("Usuario", "SIN INFORMACION");
    }

    //***************************** SE OPTIENEN TODOS LOS PERMISOS AL INICIAR LA APLICACIÓN *********************************//
    public void solicitarPermisos() {
        if (ContextCompat.checkSelfPermission(Login.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Login.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Login.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
    }
}