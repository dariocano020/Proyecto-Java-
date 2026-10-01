package inventario.modelo;

import inventario.utilidades.*;

import java.io.Serializable;

public class UsuarioAdmin extends Usuario implements Serializable {
    public UsuarioAdmin(String username, String correo, String password, String nombre) {
        super(username, correo, password, nombre);
    }
    
    @Override
    public boolean esAdmin() {
        return true;
    }
}




