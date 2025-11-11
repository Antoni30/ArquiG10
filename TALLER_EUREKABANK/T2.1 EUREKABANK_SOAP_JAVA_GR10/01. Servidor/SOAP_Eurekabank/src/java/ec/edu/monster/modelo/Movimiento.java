package ec.edu.monster.modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Grupo 10
 *
 */
@XmlRootElement
public class Movimiento {
    private String cuentaCodigo;
    private int numero;
    private String fecha;
    private String empleadoCodigo;
    private String tipoCodigo;
    private double importe;
    private String cuentaReferencia;

    public Movimiento() {}

    public Movimiento(String cuentaCodigo, int numero, String fecha,
                      String empleadoCodigo, String tipoCodigo, double importe, String cuentaReferencia) {
        this.cuentaCodigo = cuentaCodigo;
        this.numero = numero;
        this.fecha = fecha;
        this.empleadoCodigo = empleadoCodigo;
        this.tipoCodigo = tipoCodigo;
        this.importe = importe;
        this.cuentaReferencia = cuentaReferencia;
    }

    public String getCuentaCodigo() { return cuentaCodigo; }
    public void setCuentaCodigo(String cuentaCodigo) { this.cuentaCodigo = cuentaCodigo; }
    public int getNumero() { return numero; }
    public void setNumero(int numero) { this.numero = numero; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEmpleadoCodigo() { return empleadoCodigo; }
    public void setEmpleadoCodigo(String empleadoCodigo) { this.empleadoCodigo = empleadoCodigo; }
    public String getTipoCodigo() { return tipoCodigo; }
    public void setTipoCodigo(String tipoCodigo) { this.tipoCodigo = tipoCodigo; }
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    public String getCuentaReferencia() { return cuentaReferencia; }
    public void setCuentaReferencia(String cuentaReferencia) { this.cuentaReferencia = cuentaReferencia; }
}