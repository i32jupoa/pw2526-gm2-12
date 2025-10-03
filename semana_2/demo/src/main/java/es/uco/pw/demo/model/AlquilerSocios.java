package es.uco.pw.demo.model;


public class AlquilerSocio {
    private int idAlquiler;
    private String dniSocio;

    public AlquilerSocio(int idAlquiler, String dniSocio) {
        this.idAlquiler = idAlquiler;
        this.dniSocio = dniSocio;
    }

    public int getIdAlquiler() { 
        return idAlquiler; 
    }
    
    public String getDniSocio() { 
        return dniSocio; 
    }

    public void setIdAlquiler(int idAlquiler) { 
        this.idAlquiler = idAlquiler; 
    }

    public void setDniSocio(String dniSocio) { 
        this.dniSocio = dniSocio; 
    }
}
