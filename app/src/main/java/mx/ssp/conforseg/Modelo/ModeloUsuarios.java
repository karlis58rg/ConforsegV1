package mx.ssp.conforseg.Modelo;

public class ModeloUsuarios {
    private String Usuario;
    private String Contrasena;

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public ModeloUsuarios(String usuario, String contrasena) {
        Usuario = usuario;
        Contrasena = contrasena;
    }
}
