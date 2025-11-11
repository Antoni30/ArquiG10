package ec.edu.monster.modelo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Grupo 10
 *
 */
@XmlRootElement
public class Moneda {
    private String codigo;
    private String descripcion;

    public Moneda() {}

    public Moneda(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}