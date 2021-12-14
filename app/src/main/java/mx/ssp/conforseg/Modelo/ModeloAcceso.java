package mx.ssp.conforseg.Modelo;

public class ModeloAcceso {

    private String IdServicio;
    private String Fecha;
    private String Taxi;
    private String SerPublicos;
    private String Visitante;
    private String Proveedores;
    private String Peatones;
    private String Empleados;
    private String Incidentes;
    private String Recorridos;
    private String Otros;
    private String Longitud;
    private String Latitud;
    private String Usuario;


    public ModeloAcceso(String servicio, String s, String idServicio, String fecha, String taxi, String serPublicos, String visitante, String proveedores,
                        String peatones, String empleados, String incidentes, String recorridos, String otros,
                        String longitud, String latitud, String usuario) {
        IdServicio = idServicio;
        Fecha = fecha;
        Taxi = taxi;
        SerPublicos = serPublicos;
        Visitante = visitante;
        Proveedores = proveedores;
        Peatones = peatones;
        Empleados = empleados;
        Incidentes = incidentes;
        Recorridos = recorridos;
        Otros = otros;
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

    public String getTaxi() {
        return Taxi;
    }

    public void setTaxi(String taxi) {
        Taxi = taxi;
    }

    public String getSerPublicos() {
        return SerPublicos;
    }

    public void setSerPublicos(String serPublicos) {
        SerPublicos = serPublicos;
    }

    public String getVisitante() {
        return Visitante;
    }

    public void setVisitante(String visitante) {
        Visitante = visitante;
    }

    public String getProveedores() {
        return Proveedores;
    }

    public void setProveedores(String proveedores) {
        Proveedores = proveedores;
    }

    public String getPeatones() {
        return Peatones;
    }

    public void setPeatones(String peatones) {
        Peatones = peatones;
    }

    public String getEmpleados() {
        return Empleados;
    }

    public void setEmpleados(String empleados) {
        Empleados = empleados;
    }

    public String getIncidentes() {
        return Incidentes;
    }

    public void setIncidentes(String incidentes) {
        Incidentes = incidentes;
    }

    public String getRecorridos() {
        return Recorridos;
    }

    public void setRecorridos(String recorridos) {
        Recorridos = recorridos;
    }

    public String getOtros() {
        return Otros;
    }

    public void setOtros(String otros) {
        Otros = otros;
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
