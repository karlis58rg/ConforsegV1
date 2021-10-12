package mx.ssp.conforseg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import mx.ssp.conforseg.Activity.Login;
import mx.ssp.conforseg.Activity.Supervision;

public class Splash extends AppCompatActivity {
    String cargarInfoUsuario;
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        cargarDatos();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (cargarInfoUsuario.isEmpty() == true) {
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    finish();
                }else{
                    Intent intent = new Intent(Splash.this, Supervision.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2500);
    }

    public void cargarDatos(){
        share = getSharedPreferences("main",MODE_PRIVATE);
        cargarInfoUsuario = share.getString("Usuario","");
        System.out.println(cargarInfoUsuario);
    }
}