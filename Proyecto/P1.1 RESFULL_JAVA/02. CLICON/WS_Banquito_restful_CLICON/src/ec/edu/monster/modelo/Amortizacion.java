/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.modelo;

/**
 *
 * @author ckan1
 */
public class Amortizacion {
    private int numeroCuota;
    private double valorCuota;
    private double interes;
    private double capitalPagado;
    private double saldo;

    // Constructor
    public Amortizacion(int numeroCuota, double valorCuota, double interes, double capitalPagado, double saldo) {
        this.numeroCuota = numeroCuota;
        this.valorCuota = valorCuota;
        this.interes = interes;
        this.capitalPagado = capitalPagado;
        this.saldo = saldo;
    }

    // Getters y Setters
    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getValorCuota() {
        return valorCuota;
    }

    public void setValorCuota(double valorCuota) {
        this.valorCuota = valorCuota;
    }

    public double getInteres() {
        return interes;
    }

    public void setInteres(double interes) {
        this.interes = interes;
    }

    public double getCapitalPagado() {
        return capitalPagado;
    }

    public void setCapitalPagado(double capitalPagado) {
        this.capitalPagado = capitalPagado;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public String toString() {
        return "Amortizacion{" +
                "numeroCuota=" + numeroCuota +
                ", valorCuota=" + valorCuota +
                ", interes=" + interes +
                ", capitalPagado=" + capitalPagado +
                ", saldo=" + saldo +
                '}';
    }
}
