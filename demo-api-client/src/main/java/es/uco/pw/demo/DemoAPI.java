package es.uco.pw.demo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;

public class DemoAPI {

    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate();
        String baseURL = "http://localhost:8080/api";

        System.out.println("\n=============================================");
        System.out.println("   DEMO API 1: RECURSOS BÁSICOS");
        System.out.println("=============================================\n");

        System.out.println("--> 1. Creando Socio (POST)...");
        Socio nuevoSocio = new Socio(
            "CLIENTE01", 
            "Juan", 
            "Pérez", 
            LocalDate.of(1990, 5, 20), 
            "Av. Libertad 12", 
            LocalDate.now(), 
            true, // esTitular
            true  // tieneTituloPatron
        );

        try {
            ResponseEntity<Socio> res = rest.postForEntity(baseURL + "/socios", nuevoSocio, Socio.class);
            System.out.println("   [OK] Socio creado: " + res.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [INFO] El socio ya existía (" + e.getStatusCode() + ")");
        } catch (Exception e) {
            System.err.println("   [ERROR] " + e.getMessage());
        }

        System.out.println("\n--> 2. Creando Patrón (POST)...");
        Patron nuevoPatron = new Patron(
            "PATRON-CLI", 
            "Capitán", 
            "Haddock", 
            LocalDate.of(1975, 2, 10), 
            LocalDate.of(2000, 1, 15)
        );

        try {
            ResponseEntity<Patron> res = rest.postForEntity(baseURL + "/patrones", nuevoPatron, Patron.class);
            System.out.println("   [OK] Patrón creado: " + res.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [INFO] El patrón ya existía (" + e.getStatusCode() + ")");
        }

        System.out.println("\n--> 3. Creando Embarcación (POST)...");
        Embarcacion nuevaEmb = new Embarcacion(
            "CLIENTE-EMB", 
            "La Perla Negra", 
            TipoEmbarcacion.VELERO, 
            6, 
            "12 metros", 
            null 
        );

        try {
            ResponseEntity<Embarcacion> res = rest.postForEntity(baseURL + "/embarcaciones", nuevaEmb, Embarcacion.class);
            System.out.println("   [OK] Embarcación creada: " + res.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [INFO] La embarcación ya existía (" + e.getStatusCode() + ")");
        }

        System.out.println("\n--> 4. Verificando datos (GET)...");
        try {
            ResponseEntity<Socio[]> socios = rest.getForEntity(baseURL + "/socios", Socio[].class);
            System.out.println("   Socios en BD: " + (socios.getBody() != null ? socios.getBody().length : 0));
            
            ResponseEntity<Embarcacion[]> barcos = rest.getForEntity(baseURL + "/embarcaciones", Embarcacion[].class);
            System.out.println("   Barcos en BD: " + (barcos.getBody() != null ? barcos.getBody().length : 0));
        } catch (Exception e) {
            System.err.println("   [ERROR] Fallo al listar: " + e.getMessage());
        }

        System.out.println("\n=== FIN DEMO 1 ===");
    }
}