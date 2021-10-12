package mx.ssp.conforseg.Utilidades;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Calendar;

import mx.ssp.conforseg.R;

public class Funciones {

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
}
