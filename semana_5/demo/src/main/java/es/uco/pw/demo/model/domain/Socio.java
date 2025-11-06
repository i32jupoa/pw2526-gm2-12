package es.uco.pw.demo.model;

import java.time.LocalDate;

public class Socio {
    private String dni;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private LocalDate fechaInscripcion;
    private boolean esTitular;
    private boolean tieneTituloPatron;

    public Socio(String dni, String nombre, String apellidos, LocalDate fechaNacimiento,
                 String direccion, LocalDate fechaInscripcion, boolean esTitular, boolean tieneTituloPatron) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.fechaInscripcion = fechaInscripcion;
        this.esTitular = esTitular;
        this.tieneTituloPatron = tieneTituloPatron;
    }

    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getDireccion() { return direccion; }
    public LocalDate getFechaInscripcion() { return fechaInscripcion; }
    public boolean isEsTitular() { return esTitular; }
    public boolean isTieneTituloPatron() { return tieneTituloPatron; }

    public void setDni(String dni) { this.dni = dni; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setFechaInscripcion(LocalDate fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    public void setEsTitular(boolean esTitular) { this.esTitular = esTitular; }
    public void setTieneTituloPatron(boolean tieneTituloPatron) { this.tieneTituloPatron = tieneTituloPatron; }
}