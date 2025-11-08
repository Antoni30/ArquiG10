/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.mosnter.modelos;

/**
 *
 * @author rodri
 */
public class Resultado {
    private double resultado;
    private String mensaje;

    public Resultado() {}

    public Resultado(double resultado, String mensaje) {
        this.resultado = resultado;
        this.mensaje = mensaje;
    }

    public double getResultado() {
        return resultado;
    }

    public void setResultado(double resultado) {
        this.resultado = resultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
