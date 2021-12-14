package mx.ssp.conforseg.Utilidades;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;

import mx.ssp.conforseg.Activity.IncidenciasPendientes;
import mx.ssp.conforseg.R;

public class Funciones {
    ProgressDialog progressDialog;

    //***************** Calendario Picker **************************//
    public void calendar(Integer idCajadeTextoCalendario, Context context, Activity activity){
        Calendar c;
        DatePickerDialog dpd;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        dpd = new DatePickerDialog(context, R.style.DatePickerDialog, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText CajadeTextoCalendario;
                CajadeTextoCalendario = (EditText) activity.findViewById(idCajadeTextoCalendario);
                CajadeTextoCalendario.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        },year,month,day);
        dpd.show();
    }

    //***************** AJUSTA EL TAMAÑO DEL LIST VIEW **************************//
    public void  ajustaAlturaListView(ListView listView, int factorAlto){
        int ancho = 1200;
        int alto = 120;
        Log.i("LISTVIEW", "TOTAL DE ITEMS" + listView.getAdapter().getCount());

        alto = (listView.getAdapter().getCount())*factorAlto;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        listView.setLayoutParams(params);
    }

    public void Procesando(Activity activity, String Titulo, String Mensaje){
        progressDialog = ProgressDialog.show(activity, Titulo,
                Mensaje, true);
    }
    public void ProcesandoDissmis(Activity activity){
        progressDialog.dismiss();
    }
}
