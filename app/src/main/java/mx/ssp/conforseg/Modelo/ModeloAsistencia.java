package mx.ssp.conforseg.Modelo;

public class ModeloAsistencia {

    private String IdServicio;
    private String IdVigilante;
    private String Fecha;
    private String Asistencia;
    private String Longitud;
    private String Latitud;
    private String Usuario;
    private String Observaciones;

    public ModeloAsistencia(String idServicio, String idVigilante, String fecha,
                            String asistencia, String longitud, String latitud, String usuario,String observaciones) {
        IdServicio = idServicio;
        IdVigilante = idVigilante;
        Fecha = fecha;
        Asistencia = asistencia;
        Longitud = longitud;
        Latitud = latitud;
        Usuario = usuario;
        Observaciones = observaciones;
    }

    public String getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }

    public String getIdVigilante() {
        return IdVigilante;
    }

    public void setIdVigilante(String idVigilante) {
        IdVigilante = idVigilante;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getAsistencia() {
        return Asistencia;
    }

    public void setAsistencia(String asistencia) {
        Asistencia = asistencia;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

}
