package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Inscripcion {
    private int id;
    private LocalDate fechaCreacion;
    private TipoInscripcion tipo;
    private double cuotaActual;
    private String dniTitular;

    public Inscripcion(int id, LocalDate fechaCreacion, TipoInscripcion tipo, double cuotaActual, String dniTitular) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.tipo = tipo;
        this.cuotaActual = cuotaActual;
        this.dniTitular = dniTitular;
    }

    public int getId() { return id; }
    public LocalDate getFechaCreacion() { return fechaCreacion; }
    public TipoInscripcion getTipo() { return tipo; }
    public double getCuotaActual() { return cuotaActual; }
    public String getDniTitular() { return dniTitular; }

    public void setId(int id) { this.id = id; }
    public void setFechaCreacion(LocalDate fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public void setTipo(TipoInscripcion tipo) { this.tipo = tipo; }
    public void setCuotaActual(double cuotaActual) { this.cuotaActual = cuotaActual; }
    public void setDniTitular(String dniTitular) { this.dniTitular = dniTitular; }
}