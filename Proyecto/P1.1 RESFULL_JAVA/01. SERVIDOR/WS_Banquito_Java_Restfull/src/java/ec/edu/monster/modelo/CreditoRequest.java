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

public class CreditoRequest {

    @NotNull(message = "La cédula es obligatoria.")
    private String cedula; // Número de cédula del cliente

    @NotNull(message = "El monto es obligatorio.")
    @DecimalMin(value = "1.0", message = "El monto debe ser mayor a 0.")
    private double monto; // Monto del crédito

    @NotNull(message = "El plazo es obligatorio.")
    @Min(value = 3, message = "El plazo mínimo es de 3 meses.")
    @Max(value = 18, message = "El plazo máximo es de 24 meses.")
    private int plazo; // Plazo en meses

    // Constructor vacío
    public CreditoRequest() {}

    // Getters y Setters
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }
}
