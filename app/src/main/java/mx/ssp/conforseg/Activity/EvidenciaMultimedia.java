package mx.ssp.conforseg.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.Modelo.ModeloIncidencias;
import mx.ssp.conforseg.Modelo.ModeloMultimedia;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EvidenciaMultimedia extends AppCompatActivity {

    ImageView evidencia,imgEvidencia1,imgCam1,imgEvidencia2,imgCam2,imgEvidencia3,imgCam3;
    Button btnEvidencia1,btnEvidencia2,btnEvidencia3;
    TextView lblServiciosEMultimedia,lblEvidenciaEnviada1,lblEvidenciaEnviada2,lblEvidenciaEnviada3;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    int numImagen = 0;
    String cadena = "";
    SharedPreferences share;
    String cargarServicio,cargarUsuario,fecha,urlImagen,
            latitud = "null",longitud="null";
    ImageView imgHomeWhiteEMultimedia,imgLogOutWhiteEMultimedia;
    /*************************************************************/
    Double lat,lon;
    String mensaje1,mensaje2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidencia_multimedia);
        cargarDatos();
        lblServiciosEMultimedia = findViewById(R.id.lblServiciosEMultimedia);
        imgEvidencia1 = findViewById(R.id.imgEvidencia1);
        imgCam1 = findViewById(R.id.imgCam1);
        imgEvidencia2 = findViewById(R.id.imgEvidencia2);
        imgCam2 = findViewById(R.id.imgCam2);
        imgEvidencia3 = findViewById(R.id.imgEvidencia3);
        imgCam3 = findViewById(R.id.imgCam3);
        btnEvidencia1 = findViewById(R.id.btnEvidencia1);
        btnEvidencia2 = findViewById(R.id.btnEvidencia2);
        btnEvidencia3 = findViewById(R.id.btnEvidencia3);
        lblEvidenciaEnviada1 = findViewById(R.id.lblEvidenciaEnviada1);
        lblEvidenciaEnviada2 = findViewById(R.id.lblEvidenciaEnviada2);
        lblEvidenciaEnviada3 = findViewById(R.id.lblEvidenciaEnviada3);
        imgHomeWhiteEMultimedia = findViewById(R.id.imgHomeWhiteEMultimedia);
        imgLogOutWhiteEMultimedia = findViewById(R.id.imgLogOutWhiteEMultimedia);

        lblServiciosEMultimedia.setText(cargarServicio);

        lblEvidenciaEnviada1.setVisibility(View.INVISIBLE);
        lblEvidenciaEnviada2.setVisibility(View.INVISIBLE);
        lblEvidenciaEnviada3.setVisibility(View.INVISIBLE);

        imgCam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(EvidenciaMultimedia.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    numImagen = 1;
                    imgCam2.setEnabled(false);
                    imgCam3.setEnabled(false);
                    btnEvidencia1.setEnabled(true);
                    btnEvidencia2.setEnabled(false);
                    btnEvidencia3.setEnabled(false);
                    llamarItemAvatar();
                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(EvidenciaMultimedia.this);
                    dialogo1.setTitle("IMPORTANTE");
                    dialogo1.setMessage("SI NO ACEPTA LOS PERMISOS SOLICITADOS, LA APLICACION NO FUNCIONARA CORRECTAMENTE");
                    dialogo1.setPositiveButton("ENTERADO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            solicitarPermisosCamera();
                        }
                    });
                    dialogo1.show();
                }

            }
        });

        imgCam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(EvidenciaMultimedia.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    numImagen = 2;
                    imgCam1.setEnabled(false);
                    imgCam3.setEnabled(false);
                    btnEvidencia2.setEnabled(true);
                    btnEvidencia1.setEnabled(false);
                    btnEvidencia3.setEnabled(false);
                    llamarItemAvatar();
                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(EvidenciaMultimedia.this);
                    dialogo1.setTitle("IMPORTANTE");
                    dialogo1.setMessage("SI NO ACEPTA LOS PERMISOS SOLICITADOS, LA APLICACION NO FUNCIONARA CORRECTAMENTE");
                    dialogo1.setPositiveButton("ENTERADO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            solicitarPermisosCamera();
                        }
                    });
                    dialogo1.show();
                }
            }
        });

        imgCam3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(EvidenciaMultimedia.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    numImagen = 3;
                    imgCam1.setEnabled(false);
                    imgCam2.setEnabled(false);
                    btnEvidencia3.setEnabled(true);
                    btnEvidencia1.setEnabled(false);
                    btnEvidencia2.setEnabled(false);
                    llamarItemAvatar();
                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(EvidenciaMultimedia.this);
                    dialogo1.setTitle("IMPORTANTE");
                    dialogo1.setMessage("SI NO ACEPTA LOS PERMISOS SOLICITADOS, LA APLICACION NO FUNCIONARA CORRECTAMENTE");
                    dialogo1.setPositiveButton("ENTERADO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogo1, int id) {
                            solicitarPermisosCamera();
                        }
                    });
                    dialogo1.show();
                }
            }
        });

        btnEvidencia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numImagen == 1 && cadena.length() > 0){
                    insertImagen();
                }else{
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, SE REQUIERE TOMAR UNA IMAGEN", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnEvidencia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numImagen == 2 && cadena.length() > 0){
                    insertImagen();
                }else{
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, SE REQUIERE TOMAR UNA IMAGEN", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnEvidencia3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numImagen == 3 && cadena.length() > 0){
                    insertImagen();
                }else{
                    Toast.makeText(getApplicationContext(), "LO SENTIMOS, SE REQUIERE TOMAR UNA IMAGEN", Toast.LENGTH_LONG).show();
                }
            }
        });

        imgHomeWhiteEMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EvidenciaMultimedia.this, Servicios.class);
                startActivity(i);
                finish();
            }
        });

        imgLogOutWhiteEMultimedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EvidenciaMultimedia.this, Supervision.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void llamarItemAvatar() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if(numImagen == 1){
                imgEvidencia1.setImageBitmap(imageBitmap);
                imagenBase64();
                imgCam1.setVisibility(View.GONE);
            }else if(numImagen == 2){
                imgEvidencia2.setImageBitmap(imageBitmap);
                imagenBase64();
                imgCam2.setVisibility(View.GONE);
            }else{
                imgEvidencia3.setImageBitmap(imageBitmap);
                imagenBase64();
                imgCam3.setVisibility(View.GONE);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "LA ACCIÓN FUE CANCELADA", Toast.LENGTH_SHORT).show();
            if(numImagen == 1){
                imgEvidencia1.clearAnimation();
            }else if(numImagen == 2){
                imgEvidencia2.clearAnimation();
            }else{
                imgEvidencia3.clearAnimation();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void imagenBase64() {
        if(numImagen == 1){
            evidencia = imgEvidencia1;
            buildImg();
        }else if(numImagen == 2){
            evidencia = imgEvidencia2;
            buildImg();
        }else{
            evidencia = imgEvidencia3;
            buildImg();
        }
    }

    public void buildImg(){
        evidencia.buildDrawingCache();
        Bitmap bitmap = evidencia.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] imgBytes = baos.toByteArray();
        String imgString = android.util.Base64.encodeToString(imgBytes, android.util.Base64.NO_WRAP);
        cadena = imgString;

        imgBytes = android.util.Base64.decode(imgString, android.util.Base64.NO_WRAP);
        Bitmap decoded = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
        evidencia.setImageBitmap(decoded);
        System.out.print("IMAGEN" + evidencia);
    }

    public void insertImagen() {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("Description", cargarUsuario+"_"+cargarServicio+"_"+numImagen+".jpg")
                .add("ImageData", cadena)
                .build();
        Request request = new Request.Builder()
                .url("http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/MultimediaServicio/")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare(); // to be able to make toast
                Toast.makeText(getApplicationContext(), "ERROR AL ENVIAR SU REGISTRO, FAVOR DE VERIFICAR SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    EvidenciaMultimedia.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO  DE LA IMAGEN SE ENVIO CORRECTAMENTE");
                                //Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                if(numImagen == 1){
                                    urlImagen = "http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/MultimediaServicio/"+cargarUsuario+"_"+cargarServicio+"_"+numImagen+".jpg";
                                    insertEvidenciaMultimedia();
                                    imgCam1.setEnabled(false);
                                    btnEvidencia1.setVisibility(View.GONE);
                                    lblEvidenciaEnviada1.setVisibility(View.VISIBLE);
                                    imgCam2.setEnabled(true);
                                    imgCam3.setEnabled(true);
                                }else if(numImagen == 2){
                                    urlImagen = "http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/MultimediaServicio/"+cargarUsuario+"_"+cargarServicio+"_"+numImagen+".jpg";
                                    insertEvidenciaMultimedia();
                                    imgCam2.setEnabled(false);
                                    btnEvidencia2.setVisibility(View.GONE);
                                    lblEvidenciaEnviada2.setVisibility(View.VISIBLE);
                                    imgCam1.setEnabled(true);
                                    imgCam3.setEnabled(true);
                                }else{
                                    urlImagen = "http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/MultimediaServicio/"+cargarUsuario+"_"+cargarServicio+"_"+numImagen+".jpg";
                                    insertEvidenciaMultimedia();
                                    imgCam3.setEnabled(false);
                                    btnEvidencia3.setVisibility(View.GONE);
                                    lblEvidenciaEnviada3.setVisibility(View.VISIBLE);
                                    imgCam1.setEnabled(true);
                                    imgCam2.setEnabled(true);
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, EL ARCHIVO NO PUDO SER ENVIADO CORRECTAMENTE, VERIFIQUE SU CONEXIÓN", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }
        });
    }

    private void insertEvidenciaMultimedia() {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        fecha = dateFormat.format(date);

        longitud = String.valueOf(lon);
        latitud = String.valueOf(lat);

        ModeloMultimedia modeloMultimedia = new ModeloMultimedia
                (cargarServicio, fecha, urlImagen, longitud, latitud,cargarUsuario);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("IdServicio", modeloMultimedia.getIdServicio())
                .add("Fecha", modeloMultimedia.getFecha())
                .add("UrlImagen", modeloMultimedia.getUrlImagen())
                .add("Longitud", modeloMultimedia.getLongitud())
                .add("Latitud", modeloMultimedia.getLatitud())
                .add("Usuario", modeloMultimedia.getUsuario())
                .build();
        Request request = new Request.Builder()
                .url("http://c5i-ses.hidalgo.gob.mx/WsConforseg/api/Incidencias/")
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
                    EvidenciaMultimedia.this.runOnUiThread(new Runnable() {
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

    /*********************Apartir de aqui empezamos a obtener la direciones y coordenadas************************/
    public void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        EvidenciaMultimedia.Localizacion Local = new EvidenciaMultimedia.Localizacion();
        Local.setEvidenciaMultimedia(this);
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
        EvidenciaMultimedia evidenciaMultimedia;

        public EvidenciaMultimedia getEvidenciaMultimedia() {
            return evidenciaMultimedia;
        }

        public void setEvidenciaMultimedia(EvidenciaMultimedia evidenciaMultimedia1) {
            this.evidenciaMultimedia = evidenciaMultimedia1;
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

    public void solicitarPermisosCamera() {
        if (ContextCompat.checkSelfPermission(EvidenciaMultimedia.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EvidenciaMultimedia.this, new String[]{Manifest.permission.CAMERA}, 1000);
        }
    }
    public void cargarDatos() {
        share = getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarServicio = share.getString("Servicio", "SIN INFORMACION");
        cargarUsuario = share.getString("Usuario", "SIN INFORMACION");
    }
}