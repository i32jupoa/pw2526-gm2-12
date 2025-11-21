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

        System.out.println("\n=== INICIO DEMO CLIENTE 1 (SOCIOS Y FLOTA) ===\n");

        // TEST 1: GET Socios
        try {
            System.out.println("--> 1. GET /api/socios");
            ResponseEntity<Socio[]> response = rest.getForEntity(baseURL + "/socios", Socio[].class);
            List<Socio> socios = Arrays.asList(response.getBody());
            System.out.println("Status: " + response.getStatusCode());
            if (socios.isEmpty()) System.out.println("   [!] No hay socios.");
            else socios.forEach(s -> System.out.println("   - " + s.getNombre() + " (DNI: " + s.getDni() + ")"));
        } catch (Exception e) {
            System.err.println("ERROR TEST 1: " + e.getMessage());
        }

        // TEST 2: POST Socio
        try {
            System.out.println("\n--> 2. POST /api/socios");
            Socio nuevo = new Socio("CLIENTE01", "Test", "Api", LocalDate.of(1990, 1, 1), "Calle Test", LocalDate.now(), false, true);
            ResponseEntity<Socio> response = rest.postForEntity(baseURL + "/socios", nuevo, Socio.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Creado: " + response.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error esperado o conflicto: " + e.getStatusCode());
        } catch (Exception e) {
            System.err.println("ERROR TEST 2: " + e.getMessage());
        }

        // TEST 3: GET Embarcaciones
        try {
            System.out.println("\n--> 3. GET /api/embarcaciones");
            ResponseEntity<Embarcacion[]> response = rest.getForEntity(baseURL + "/embarcaciones", Embarcacion[].class);
            Arrays.asList(response.getBody()).forEach(e -> System.out.println("   - " + e.getNombre() + " (" + e.getMatricula() + ")"));
        } catch (Exception e) {
            System.err.println("ERROR TEST 3: " + e.getMessage());
        }

        // TEST 4: POST Embarcacion
        try {
            System.out.println("\n--> 4. POST /api/embarcaciones");
            Embarcacion nuevaEmb = new Embarcacion("CLIENTE-EMB", "La Rapida", TipoEmbarcacion.LANCHA, 4, "5m", null);
            ResponseEntity<Embarcacion> response = rest.postForEntity(baseURL + "/embarcaciones", nuevaEmb, Embarcacion.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Creada: " + response.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error (posible duplicado): " + e.getStatusCode());
        }

        // TEST 5: POST Patron
        try {
            System.out.println("\n--> 5. POST /api/patrones");
            Patron nuevoPatron = new Patron("PATRON-CLI", "Capitan", "Hook", LocalDate.of(1980, 5, 5), LocalDate.of(2010, 1, 1));
            ResponseEntity<Patron> response = rest.postForEntity(baseURL + "/patrones", nuevoPatron, Patron.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Creado: " + response.getBody().getNombre());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error (posible duplicado): " + e.getStatusCode());
        }

        System.out.println("\n=== FIN DEMO 1 ===");
    }
}

