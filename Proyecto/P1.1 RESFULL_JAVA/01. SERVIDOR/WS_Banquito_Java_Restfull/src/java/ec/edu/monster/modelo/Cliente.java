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

import java.time.LocalDate;
import java.util.Date;

public class Cliente {

    private int cod_cliente; // Se debe usar el mismo nombre que en el JSON (en este caso, "codigo").
    private String cedula;
    private String nombre;
    private String genero; // 'M' o 'F'
    private LocalDate fecha_nacimiento; // Cambiamos a LocalDate

    // Constructor vacío
    public Cliente() {}

    // Getters y Setters
    public int getCodigo() {
        return cod_cliente;
    }

    public void setCodigo(int codigo) {
        this.cod_cliente = codigo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    // Getters y Setters
    public LocalDate getFechaNacimiento() {
        return fecha_nacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fecha_nacimiento = fechaNacimiento;
    }
}
