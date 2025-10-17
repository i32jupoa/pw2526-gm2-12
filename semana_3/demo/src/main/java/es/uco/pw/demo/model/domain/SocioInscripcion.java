package es.uco.pw.demo.model;

public class SocioInscripcion {
    private int id;
    private int idInscripcion;
    private String dniSocio;

    public SocioInscripcion(int id, int idInscripcion, String dniSocio) {
        this.id = id;
        this.idInscripcion = idInscripcion;
        this.dniSocio = dniSocio;
    }

    public int getId() { return id; }
    public int getIdInscripcion() { return idInscripcion; }
    public String getDniSocio() { return dniSocio; }

    public void setId(int id) { this.id = id; }
    public void setIdInscripcion(int idInscripcion) { this.idInscripcion = idInscripcion; }
    public void setDniSocio(String dniSocio) { this.dniSocio = dniSocio; }
}