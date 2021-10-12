package mx.ssp.conforseg.Modelo;

public class ModeloIncidencias {
    private String IdNota;
    private String IdServicio;
    private String Nota;
    private String Latitud;
    private String Longitud;
    private String Usuario;

    public ModeloIncidencias(String idNota, String idServicio, String nota,
                             String latitud, String longitud, String usuario) {
        IdNota = idNota;
        IdServicio = idServicio;
        Nota = nota;
        Latitud = latitud;
        Longitud = longitud;
        Usuario = usuario;
    }

    public String getIdNota() {
        return IdNota;
    }

    public void setIdNota(String idNota) {
        IdNota = idNota;
    }

    public String getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }

    public String getNota() {
        return Nota;
    }

    public void setNota(String nota) {
        Nota = nota;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

}
