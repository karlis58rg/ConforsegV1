package mx.ssp.conforseg.Modelo;

public class ModeloMultimedia {
    private String IdServicio;
    private String Fecha;
    private String UrlImagen;
    private String Longitud;
    private String Latitud;
    private String Usuario;

    public ModeloMultimedia(String idServicio, String fecha, String urlImagen,
                            String longitud, String latitud, String usuario) {
        IdServicio = idServicio;
        Fecha = fecha;
        UrlImagen = urlImagen;
        Longitud = longitud;
        Latitud = latitud;
        Usuario = usuario;
    }

    public String getIdServicio() {
        return IdServicio;
    }

    public void setIdServicio(String idServicio) {
        IdServicio = idServicio;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getUrlImagen() {
        return UrlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        UrlImagen = urlImagen;
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

}
