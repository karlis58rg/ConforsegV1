package mx.ssp.conforseg.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import mx.ssp.conforseg.Activity.Asistencia;
import mx.ssp.conforseg.Activity.EvidenciaMultimedia;
import mx.ssp.conforseg.Activity.Incidencias;
import mx.ssp.conforseg.Activity.IncidenciasPendientes;
import mx.ssp.conforseg.R;
import mx.ssp.conforseg.Activity.Supervisiones;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    LinearLayout lySupervisiones,lyIncidencias,lyAsistencia,lyEvidenciaFotografica,lyIncidenciasPendientes;
    SharedPreferences share;
    String cargarServicio;
    TextView lblServicio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =  new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        /******************************************************************************************/
        cargarDatos();
        lySupervisiones = root.findViewById(R.id.lySupervisiones);
        lyIncidencias = root.findViewById(R.id.lyIncidencias);
        lyAsistencia = root.findViewById(R.id.lyAsistencia);
        lyEvidenciaFotografica = root.findViewById(R.id.lyEvidenciaFotografica);
        lyIncidenciasPendientes = root.findViewById(R.id.lyIncidenciasPendientes);
        lblServicio = root.findViewById(R.id.lblServicio);
        /*************************** EVENTOS *******************************/
        lblServicio.setText(cargarServicio);
        lySupervisiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Supervisiones.class);
                startActivity(i);
            }
        });
        lyIncidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Incidencias.class);
                i.putExtra("home",1);
                startActivity(i);
            }
        });
        lyAsistencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "ESTAMOS TRABAJANDO EN ESTA SECCIÓN, LAMENTAMOS LAS MOLESTIAS.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), Asistencia.class);
                startActivity(i);
            }
        });
        lyEvidenciaFotografica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EvidenciaMultimedia.class);
                startActivity(i);
            }
        });
        lyIncidenciasPendientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "ESTAMOS TRABAJANDO EN ESTA SECCIÓN, LAMENTAMOS LAS MOLESTIAS.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), IncidenciasPendientes.class);
                startActivity(i);
            }
        });

        /*********************************************************************************/

        return root;
    }

    public void cargarDatos() {
        share = getContext().getSharedPreferences("main", Context.MODE_PRIVATE);
        cargarServicio = share.getString("Servicio", "SIN INFORMACION");
    }

}