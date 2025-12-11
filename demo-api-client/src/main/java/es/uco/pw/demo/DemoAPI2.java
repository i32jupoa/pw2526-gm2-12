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
       
        // Variables para guardar los IDs generados dinámicamente
        Integer idInscripcionGenerado = null;

        System.out.println("\n=============================================");
        System.out.println("   DEMO API 2: OPERACIONES COMPLEJAS");
        System.out.println("=============================================\n");

        // --- 1. CREAR INSCRIPCIÓN (POST) ---
        // Requisito: Debe existir el socio "CLIENTE01" (Creado en DemoAPI 1)
        System.out.println("--> 1. Creando Inscripción para CLIENTE01...");
        try {
            // Usamos constructor sin ID, la BD lo genera
            Inscripcion insc = new Inscripcion(
                LocalDate.now(),
                TipoInscripcion.INDIVIDUAL,
                300.0,
                "CLIENTE01"
            );
           
            ResponseEntity<Inscripcion> response = rest.postForEntity(baseURL + "/inscripciones", insc, Inscripcion.class);
            Inscripcion creada = response.getBody();
           
            if (creada != null) {
                idInscripcionGenerado = creada.getId(); 
                System.out.println("   [OK] Inscripción creada con ID: " + idInscripcionGenerado);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("   [ERROR] " + e.getStatusCode() + ": " + e.getResponseBodyAsString());
            System.out.println("   (¿Ejecutaste DemoAPI 1 primero?)");
            return; // Si falla esto, no podemos seguir con el alquiler
        }

        // --- 2. CREAR ALQUILER (POST) ---
        // Requisito: Usar el ID de inscripción que acabamos de crear
        if (idInscripcionGenerado != null) {
            System.out.println("\n--> 2. Creando Alquiler vinculado a Inscripción ID " + idInscripcionGenerado + "...");
            try {
                Alquiler alquiler = new Alquiler(
                    idInscripcionGenerado,
                    "CLIENTE-EMB", // Matrícula creada en DemoAPI 1
                    LocalDate.now().plusDays(10), // Fecha futura
                    LocalDate.now().plusDays(13),
                    2, // Plazas
                    200.0 // Precio
                );
               
                ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/alquileres", alquiler, Alquiler.class);
                System.out.println("   [OK] Alquiler creado con ID: " + response.getBody().getId());
               
            } catch (HttpClientErrorException e) {
                System.out.println("   [ERROR] " + e.getStatusCode() + ". ¿El barco 'CLIENTE-EMB' existe o está ocupado?");
            }
        }

        // --- 3. CREAR RESERVA (POST) ---
        // Requisito: El socio debe ser mayor de edad (CLIENTE01 lo es) y el barco tener patrón (ojo aquí)
        System.out.println("\n--> 3. Intentando crear Reserva (Evento)...");
        try {
            Reserva reserva = new Reserva(
                "CLIENTE01",
                "CLIENTE-EMB",
                LocalDate.now().plusDays(25),
                3,
                "Fiesta Privada",
                120.0
            );
           
            ResponseEntity<Reserva> response = rest.postForEntity(baseURL + "/reservas", reserva, Reserva.class);
            System.out.println("   [OK] Reserva creada con ID: " + response.getBody().getId());
           
        } catch (HttpClientErrorException e) {
            System.out.println("   [INFO] Fallo esperado o Error: " + e.getStatusCode());
            System.out.println("   Nota: Si falla con 500/400 puede ser porque el barco 'CLIENTE-EMB' no tiene patrón asignado aún.");
            System.out.println("         (En la Práctica 2 Semana 2, se añade el PATCH para asignar patrón).");
        }

        // --- 4. LISTAR RESULTADOS ---
        System.out.println("\n--> 4. Listado final de Inscripciones...");
        try {
            ResponseEntity<Inscripcion[]> response = rest.getForEntity(baseURL + "/inscripciones", Inscripcion[].class);
            Arrays.asList(response.getBody()).forEach(i ->
                System.out.println("   - ID: " + i.getId() + " | Titular: " + i.getDniTitular() + " | Tipo: " + i.getTipo())
            );
        } catch (Exception e) {
            System.err.println("   Error al listar: " + e.getMessage());
        }

        System.out.println("\n=== FIN DEMO 2 ===");
    }
}