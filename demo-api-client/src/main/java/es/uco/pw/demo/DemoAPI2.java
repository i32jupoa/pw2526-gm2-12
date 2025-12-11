package es.uco.pw.demo;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import es.uco.pw.demo.model.domain.Alquiler;
import es.uco.pw.demo.model.domain.AlquilerSocio;
import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.Inscripcion;
import es.uco.pw.demo.model.domain.Reserva;
import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.domain.SocioInscripcion;
import es.uco.pw.demo.model.domain.TipoInscripcion;

public class DemoAPI2 {

    public static void main(String[] args) {
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String baseURL = "http://localhost:8080/api";
<<<<<<< HEAD
        
        Integer idInscripcionGenerado = null;
        Integer idAlquilerGenerado = null;
        Integer idReservaGenerada = null;

        System.out.println("\n=============================================");
        System.out.println("   DEMO API 2: OPERACIONES Y PRUEBAS SEMANA 2");
        System.out.println("=============================================\n");
        
        System.out.println("--- PASO 1: CREACIÓN (POST) ---");

        try {
            Inscripcion insc = new Inscripcion(LocalDate.now(), TipoInscripcion.INDIVIDUAL, 300.0, "CLIENTE01");
            ResponseEntity<Inscripcion> response = rest.postForEntity(baseURL + "/inscripciones", insc, Inscripcion.class);
            if (response.getBody() != null) {
                idInscripcionGenerado = response.getBody().getId();
                System.out.println("   [OK] Inscripción creada ID: " + idInscripcionGenerado);
            }
        } catch (HttpClientErrorException e) {
            System.out.println("   [INFO] Error al crear inscripción (¿Ya existe?): " + e.getStatusCode());
        }

        if (idInscripcionGenerado != null) {
            try {
                Alquiler alquiler = new Alquiler(idInscripcionGenerado, "CLIENTE-EMB", LocalDate.now().plusDays(10), LocalDate.now().plusDays(13), 2, 200.0);
                ResponseEntity<Alquiler> response = rest.postForEntity(baseURL + "/alquileres", alquiler, Alquiler.class);
                if (response.getBody() != null) {
                    idAlquilerGenerado = response.getBody().getId();
                    System.out.println("   [OK] Alquiler creado ID: " + idAlquilerGenerado);
                }
            } catch (HttpClientErrorException e) { System.out.println("   [ERROR] Fallo alquiler: " + e.getStatusCode()); }
        }

        try {
            Reserva reserva = new Reserva("CLIENTE01", "CLIENTE-EMB", LocalDate.now().plusDays(25), 3, "Fiesta API", 120.0);
            ResponseEntity<Reserva> response = rest.postForEntity(baseURL + "/reservas", reserva, Reserva.class);
            if (response.getBody() != null) {
                idReservaGenerada = response.getBody().getId();
                System.out.println("   [OK] Reserva creada ID: " + idReservaGenerada);
            }
        } catch (HttpClientErrorException e) { System.out.println("   [INFO] Fallo reserva (¿Falta patrón?): " + e.getStatusCode()); }

        System.out.println("\n--- PASO 2: MODIFICACIONES (PATCH/PUT) ---");

        try {
            System.out.println("--> Vinculando Patrón a Barco...");
            Embarcacion updateBarco = new Embarcacion(null, null, null, 0, null, "PATRON-CLI");
            rest.patchForObject(baseURL + "/embarcaciones/CLIENTE-EMB", updateBarco, Embarcacion.class);
            System.out.println("   [OK] Patrón asignado.");
        } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }

        if (idInscripcionGenerado != null) {
            try {
                System.out.println("--> Convirtiendo Inscripción a Familiar...");
                Inscripcion cambio = new Inscripcion(null, TipoInscripcion.FAMILIAR, 450.0, null);
                rest.put(baseURL + "/inscripciones/" + idInscripcionGenerado, cambio);
                System.out.println("   [OK] Tipo actualizado a FAMILIAR.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        if (idAlquilerGenerado != null) {
            try {
                System.out.println("--> Añadiendo pasajero a Alquiler...");
                Socio pasajero = new Socio("PASAJERO1", "Pepe", "Test", LocalDate.of(2000, 1, 1), "C/Mar", LocalDate.now(), false, false);
                try { rest.postForEntity(baseURL + "/socios", pasajero, Socio.class); } catch (Exception e) {}

                AlquilerSocio vinculacion = new AlquilerSocio(0, "PASAJERO1");
                Alquiler actualizado = rest.patchForObject(baseURL + "/alquileres/" + idAlquilerGenerado + "/socios", vinculacion, Alquiler.class);
                System.out.println("   [OK] Pasajero añadido. Nuevo Precio: " + actualizado.getPrecioTotal());
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        if (idReservaGenerada != null) {
            try {
                System.out.println("--> Modificando fecha de Reserva...");
                Reserva cambioFecha = new Reserva(null, null, LocalDate.now().plusDays(30), 0, null, 0.0);
                rest.patchForObject(baseURL + "/reservas/" + idReservaGenerada + "/fecha", cambioFecha, Reserva.class);
                System.out.println("   [OK] Fecha retrasada 5 días.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        System.out.println("\n--- PASO 3: BORRADOS Y RESTRICCIONES (DELETE) ---");

        try {
            System.out.println("--> Intentando borrar Patrón activo (Esperando Error)...");
            rest.delete(baseURL + "/patrones/PATRON-CLI");
            System.out.println("   [FAIL] El patrón se borró y no debería.");
        } catch (HttpClientErrorException e) {
            System.out.println("   [OK] Bloqueado correctamente: " + e.getStatusCode());
        }

        if (idReservaGenerada != null) {
            try {
                System.out.println("--> Cancelando Reserva futura...");
                rest.delete(baseURL + "/reservas/" + idReservaGenerada);
                System.out.println("   [OK] Reserva cancelada.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
=======
       
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
>>>>>>> 4b13f8b36e84c5a388a185ea5f457ee2a5270052
        }

        System.out.println("\n=== FIN DEMO 2 ===");
    }
}