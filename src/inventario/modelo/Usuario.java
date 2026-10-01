package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String tipo = getClass().getSimpleName();
    private String username;
    private String correo;
    private String password;
    private String nombre;

    public Usuario(String username, String correo, String password, String nombre) {
        this.username = username;
        this.correo = correo;
        this.password = password;
        this.nombre = nombre;
    }

    public String getUsername() { return username; }
    public String getCorreo() { return correo; }
    public String getPassword() { return password; }
    public String getNombre() { return nombre; }
    
    public boolean esAdmin() { 
        return false; 
    }
}




