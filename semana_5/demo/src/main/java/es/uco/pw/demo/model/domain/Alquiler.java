package es.uco.pw.demo.model;


import java.time.LocalDate;

public class Alquiler {
    private int id;
    private int idInscripcion;
    private String matricula;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int plazasReservadas;
    private double precioTotal;

    public Alquiler(int id, int idInscripcion, String matricula, LocalDate fechaInicio,
                    LocalDate fechaFin, int plazasReservadas, double precioTotal) {
        this.id = id;
        this.idInscripcion = idInscripcion;
        this.matricula = matricula;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.plazasReservadas = plazasReservadas;
        this.precioTotal = precioTotal;
    }

    public int getId() { return id; }
    public int getIdInscripcion() { return idInscripcion; }
    public String getMatricula() { return matricula; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public int getPlazasReservadas() { return plazasReservadas; }
    public double getPrecioTotal() { return precioTotal; }

    public void setId(int id) { this.id = id; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public void setPlazasReservadas(int plazasReservadas) { this.plazasReservadas = plazasReservadas; }
    public void setPrecioTotal(double precioTotal) { this.precioTotal = precioTotal; }
}
