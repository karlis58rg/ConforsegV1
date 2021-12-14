package mx.ssp.conforseg.ui.salir;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

import mx.ssp.conforseg.Activity.Login;
import mx.ssp.conforseg.Activity.Supervision;
import mx.ssp.conforseg.Fragment.Servicios;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.SqLite.DataHelper;

import static android.content.Context.MODE_PRIVATE;

public class Salir extends Fragment {

    private SalirViewModel mViewModel;
    SharedPreferences share;
    SharedPreferences.Editor editor;
    String cargarServicio;

    public static Salir newInstance() {
        return new Salir();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.salir_fragment, container, false);
        deleteServicios();
        eliminarDatos();
        //CIERRAS SESIÓN Y TERMINAS LA VENTANA ACTUAL
        getActivity().finishAffinity();
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SalirViewModel.class);
        // TODO: Use the ViewModel
    }

    private void eliminarDatos(){
        share = getActivity().getSharedPreferences("main",MODE_PRIVATE);
        editor = share.edit();
        editor.remove("Servicio").apply();
        editor.remove("Usuario").apply();
        editor.remove("ServicioCargado").apply();
    }

    private boolean deleteServicios() {
        DataHelper dataHelper = new DataHelper(getContext());
        boolean delete = dataHelper.DeleteTempoServiciosSup();
        System.out.println(delete);
        System.out.println("ELIMINANDO REGISTRO SQLITE");
        return  delete;
    }

}