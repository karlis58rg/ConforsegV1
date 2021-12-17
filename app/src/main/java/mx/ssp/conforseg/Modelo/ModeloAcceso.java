package mx.ssp.conforseg.Modelo;

public class ModeloAcceso {

    private String IdServicio;
    private String Fecha;
    private String Taxi;
    private String VisitantesPeatonales;
    private String VisitanteEnVehiculo;
    private String EmpleadasDomesticas;
    private String TrabajadorEnVehiculo;
    private String TrabajadorPeatonal;
    private String AccesoARC;
    private String Empleados;
    private String Incidentes;
    private String Recorridos;
    private String Otros;
    private String Longitud;
    private String Latitud;
    private String Usuario;


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

    public String getVisitantesPeatonales() {
        return VisitantesPeatonales;
    }

    public void setVisitantesPeatonales(String visitantesPeatonales) {
        VisitantesPeatonales = visitantesPeatonales;
    }

    public String getVisitanteEnVehiculo() {
        return VisitanteEnVehiculo;
    }

    public void setVisitanteEnVehiculo(String visitanteEnVehiculo) {
        VisitanteEnVehiculo = visitanteEnVehiculo;
    }

    public String getEmpleadasDomesticas() {
        return EmpleadasDomesticas;
    }

    public void setEmpleadasDomesticas(String empleadasDomesticas) {
        EmpleadasDomesticas = empleadasDomesticas;
    }

    public String getTrabajadorEnVehiculo() {
        return TrabajadorEnVehiculo;
    }

    public void setTrabajadorEnVehiculo(String trabajadorEnVehiculo) {
        TrabajadorEnVehiculo = trabajadorEnVehiculo;
    }

    public String getTrabajadorPeatonal() {
        return TrabajadorPeatonal;
    }

    public void setTrabajadorPeatonal(String trabajadorPeatonal) {
        TrabajadorPeatonal = trabajadorPeatonal;
    }

    public String getAccesoARC() {
        return AccesoARC;
    }

    public void setAccesoARC(String accesoARC) {
        AccesoARC = accesoARC;
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

    public ModeloAcceso(String idServicio, String fecha, String taxi, String visitantesPeatonales, String visitanteEnVehiculo, String empleadasDomesticas,
                        String trabajadorEnVehiculo, String trabajadorPeatonal, String accesoARC, String empleados, String incidentes,
                        String recorridos, String otros, String longitud, String latitud, String usuario) {
        IdServicio = idServicio;
        Fecha = fecha;
        Taxi = taxi;
        VisitantesPeatonales = visitantesPeatonales;
        VisitanteEnVehiculo = visitanteEnVehiculo;
        EmpleadasDomesticas = empleadasDomesticas;
        TrabajadorEnVehiculo = trabajadorEnVehiculo;
        TrabajadorPeatonal = trabajadorPeatonal;
        AccesoARC = accesoARC;
        Empleados = empleados;
        Incidentes = incidentes;
        Recorridos = recorridos;
        Otros = otros;
        Longitud = longitud;
        Latitud = latitud;
        Usuario = usuario;
    }


}
