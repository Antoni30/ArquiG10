/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author rodri
 */
public  class ServiceResponse {
        private double resultado;
        private String mensaje;

        public ServiceResponse(double resultado, String mensaje) {
            this.resultado = resultado;
            this.mensaje = mensaje;
        }

        public double getResultado() {
            return resultado;
        }

        public String getMensaje() {
            return mensaje;
        }
    }