package ec.edu.monster.modelo;

/**
 *
 * @author Grupo 10
 *
 */

public class Cuenta {
    private String codigo;
    private String monedaCodigo;
    private String sucursalCodigo;
    private String empleadoCreacion;
    private String clienteCodigo;
    private double saldo;
    private String fechaCreacion;
    private String estado;
    private int contadorMovimientos;
    private String clave;

    public Cuenta() {}

    public Cuenta(String codigo, String monedaCodigo, String sucursalCodigo,
                  String empleadoCreacion, String clienteCodigo, double saldo,
                  String fechaCreacion, String estado, int contadorMovimientos, String clave) {
        this.codigo = codigo;
        this.monedaCodigo = monedaCodigo;
        this.sucursalCodigo = sucursalCodigo;
        this.empleadoCreacion = empleadoCreacion;
        this.clienteCodigo = clienteCodigo;
        this.saldo = saldo;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
        this.contadorMovimientos = contadorMovimientos;
        this.clave = clave;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getMonedaCodigo() { return monedaCodigo; }
    public void setMonedaCodigo(String monedaCodigo) { this.monedaCodigo = monedaCodigo; }
    public String getSucursalCodigo() { return sucursalCodigo; }
    public void setSucursalCodigo(String sucursalCodigo) { this.sucursalCodigo = sucursalCodigo; }
    public String getEmpleadoCreacion() { return empleadoCreacion; }
    public void setEmpleadoCreacion(String empleadoCreacion) { this.empleadoCreacion = empleadoCreacion; }
    public String getClienteCodigo() { return clienteCodigo; }
    public void setClienteCodigo(String clienteCodigo) { this.clienteCodigo = clienteCodigo; }
    public double getSaldo() { return saldo; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public int getContadorMovimientos() { return contadorMovimientos; }
    public void setContadorMovimientos(int contadorMovimientos) { this.contadorMovimientos = contadorMovimientos; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
}