package ec.edu.monster.modelo;

/**
 *
 * @author Grupo 10
 *
 */

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