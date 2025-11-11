package ec.edu.monster.modelo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MovimientoRequest {
    private String cuentaCodigo;
    private double importe;
    private String empleadoCodigo;

    // Constructores
    public MovimientoRequest() {}

    // Getters y Setters
    public String getCuentaCodigo() { return cuentaCodigo; }
    public void setCuentaCodigo(String cuentaCodigo) { this.cuentaCodigo = cuentaCodigo; }
    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }
    public String getEmpleadoCodigo() { return empleadoCodigo; }
    public void setEmpleadoCodigo(String empleadoCodigo) { this.empleadoCodigo = empleadoCodigo; }
}