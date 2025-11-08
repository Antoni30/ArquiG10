package ec.edu.monster.utils;

import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author rodri
 */
public class Login {
     private static final String USUARIO_VALIDO = "MONSTER";
    private static final String CLAVE_VALIDO = "MONSTER9";
    private static final int INTENTOS_MAX = 3;
    
    public boolean login(BufferedReader br) throws IOException {
        System.out.println("=== LOGIN ===");
        int intentos = 0;

        while (intentos < INTENTOS_MAX) {
            System.out.print("Usuario: ");
            String usuario = br.readLine();

            System.out.print("Contraseña: ");
            String clave = br.readLine();
            System.out.println(); // nueva línea después de ingresar la contraseña

            if (USUARIO_VALIDO.equals(usuario) && CLAVE_VALIDO.equals(clave)) {
                System.out.println("✅ Login exitoso. Bienvenido " + usuario + "!");
                return true;
            } else {
                intentos++;
                System.out.println("❌ Usuario o contraseña incorrectos. Intentos restantes: " + (INTENTOS_MAX - intentos));
            }
        }
        return false;
    }
    
}
