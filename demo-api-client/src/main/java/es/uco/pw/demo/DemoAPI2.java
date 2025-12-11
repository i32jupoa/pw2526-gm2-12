package es.uco.pw.demo;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.domain.Alquiler;
import es.uco.pw.demo.model.domain.Inscripcion;
import es.uco.pw.demo.model.domain.Reserva;
import es.uco.pw.demo.model.domain.TipoInscripcion;

public class DemoAPI2 {

    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate();
        String baseURL = "http://localhost:8080/api";

        System.out.println("\n=== INICIO DEMO CLIENTE 2 (INSCRIPCIONES Y ALQUILERES) ===\n");

        // TEST 1: POST Inscripci贸n
        try {
            System.out.println("--> 1. POST /api/inscripciones");
            Inscripcion insc = new Inscripcion(0, LocalDate.now(), TipoInscripcion.INDIVIDUAL, 300.0, "CLIENTE01");
            ResponseEntity<Inscripcion> response = rest.postForEntity(baseURL + "/inscripciones", insc, Inscripcion.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Inscripci贸n creada con ID: " + response.getBody().getId());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error al crear inscripci贸n: " + e.getStatusCode());
        }

        // TEST 2: GET Inscripciones
        try {
            System.out.println("\n--> 2. GET /api/inscripciones");
            ResponseEntity<Inscripcion[]> response = rest.getForEntity(baseURL + "/inscripciones", Inscripcion[].class);
            Arrays.asList(response.getBody()).forEach(i -> System.out.println("   - ID: " + i.getId() + " (Titular: " + i.getDniTitular() + ")"));
        } catch (Exception e) {
            System.err.println("ERROR TEST 2: " + e.getMessage());
        }

        // TEST 3: POST Alquiler
        try {
            System.out.println("\n--> 3. POST /api/alquileres");
            Alquiler alquiler = new Alquiler(0, 1, "CLIENTE-EMB", LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), 2, 200.0);
            ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/alquileres", alquiler, Alquiler.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Alquiler creado ID: " + response.getBody().getId());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error creando alquiler: " + e.getStatusCode());
        }

        // TEST 4: POST Reserva (Evento)
        try {
            System.out.println("\n--> 4. POST /api/reservas");
            Reserva reserva = new Reserva(0, "CLIENTE01", "MAT001", LocalDate.now().plusDays(20), 3, "Fiesta API", 120.0);
            ResponseEntity<Reserva> response = rest.postForEntity(baseURL + "/reservas", reserva, Reserva.class);
            System.out.println("Status: " + response.getStatusCode());
            System.out.println("   Reserva creada ID: " + response.getBody().getId());
        } catch (HttpClientErrorException e) {
            System.out.println("   [!] Error creando reserva: " + e.getStatusCode() + " (Revise disponibilidad o patr贸n)");
        }

        System.out.println("\n=== FIN DEMO 2 ===");
    }
}