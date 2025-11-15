/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.modelo;

/**
 *
 * @author sdiegx
 */
import javax.validation.constraints.*;

public class AmortizacionRequest {

    @NotNull(message = "El monto del crédito es obligatorio.")
    @DecimalMin(value = "0.01", message = "El monto del crédito debe ser mayor a 0.")
    private double montoCredito; // Monto del crédito

    @NotNull(message = "El plazo en meses es obligatorio.")
    @Min(value = 1, message = "El plazo debe ser al menos de 1 mes.")
    private int plazoMeses; // Plazo en meses

    // Constructor vacío
    public AmortizacionRequest() {}

    // Getters y Setters
    public double getMontoCredito() {
        return montoCredito;
    }

    public void setMontoCredito(double montoCredito) {
        this.montoCredito = montoCredito;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }
}
