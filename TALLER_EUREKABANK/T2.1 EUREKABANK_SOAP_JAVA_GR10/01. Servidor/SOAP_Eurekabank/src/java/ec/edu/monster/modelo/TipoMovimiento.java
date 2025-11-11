package ec.edu.monster.modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Grupo 10
 *
 */
@XmlRootElement
public class TipoMovimiento {
    private String codigo;
    private String descripcion;
    private String accion;
    private String estado;

    public TipoMovimiento() {}

    public TipoMovimiento(String codigo, String descripcion, String accion, String estado) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.accion = accion;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}