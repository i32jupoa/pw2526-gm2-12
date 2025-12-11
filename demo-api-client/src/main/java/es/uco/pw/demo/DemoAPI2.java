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
        // Configuración para soportar PATCH (Semana 2)
        RestTemplate rest = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        String baseURL = "http://localhost:8080/api";
        
        Integer idInscripcionGenerado = null;
        Integer idAlquilerGenerado = null;
        Integer idReservaGenerada = null;

        System.out.println("\n=============================================");
        System.out.println("   DEMO API 2: OPERACIONES Y PRUEBAS SEMANA 2");
        System.out.println("=============================================\n");

        // ---------------------------------------------------------
        // PARTE 1: CREACIÓN DE RECURSOS (POST)
        // ---------------------------------------------------------
        
        System.out.println("--- PASO 1: CREACIÓN (POST) ---");

        // 1.1 Crear Inscripción
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

        // 1.2 Crear Alquiler
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

        // 1.3 Crear Reserva
        try {
            Reserva reserva = new Reserva("CLIENTE01", "CLIENTE-EMB", LocalDate.now().plusDays(25), 3, "Fiesta API", 120.0);
            ResponseEntity<Reserva> response = rest.postForEntity(baseURL + "/reservas", reserva, Reserva.class);
            if (response.getBody() != null) {
                idReservaGenerada = response.getBody().getId();
                System.out.println("   [OK] Reserva creada ID: " + idReservaGenerada);
            }
        } catch (HttpClientErrorException e) { System.out.println("   [INFO] Fallo reserva (¿Falta patrón?): " + e.getStatusCode()); }


        // ---------------------------------------------------------
        // PARTE 2: ACTUALIZACIONES (PUT / PATCH) - REQUISITOS SEMANA 2
        // ---------------------------------------------------------
        
        System.out.println("\n--- PASO 2: MODIFICACIONES (PATCH/PUT) ---");

        // 2.1 PATCH: Vincular Patrón a Barco
        try {
            System.out.println("--> Vinculando Patrón a Barco...");
            Embarcacion updateBarco = new Embarcacion(null, null, null, 0, null, "PATRON-CLI");
            rest.patchForObject(baseURL + "/embarcaciones/CLIENTE-EMB", updateBarco, Embarcacion.class);
            System.out.println("   [OK] Patrón asignado.");
        } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }

        // 2.2 PUT: Convertir Inscripción a Familiar
        if (idInscripcionGenerado != null) {
            try {
                System.out.println("--> Convirtiendo Inscripción a Familiar...");
                Inscripcion cambio = new Inscripcion(null, TipoInscripcion.FAMILIAR, 450.0, null);
                rest.put(baseURL + "/inscripciones/" + idInscripcionGenerado, cambio);
                System.out.println("   [OK] Tipo actualizado a FAMILIAR.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        // 2.3 PATCH: Añadir Socio a Alquiler (Recalculo de precio)
        if (idAlquilerGenerado != null) {
            try {
                System.out.println("--> Añadiendo pasajero a Alquiler...");
                // Primero creamos un socio extra para probar
                Socio pasajero = new Socio("PASAJERO1", "Pepe", "Test", LocalDate.of(2000, 1, 1), "C/Mar", LocalDate.now(), false, false);
                try { rest.postForEntity(baseURL + "/socios", pasajero, Socio.class); } catch (Exception e) {}

                AlquilerSocio vinculacion = new AlquilerSocio(0, "PASAJERO1");
                Alquiler actualizado = rest.patchForObject(baseURL + "/alquileres/" + idAlquilerGenerado + "/socios", vinculacion, Alquiler.class);
                System.out.println("   [OK] Pasajero añadido. Nuevo Precio: " + actualizado.getPrecioTotal());
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        // 2.4 PATCH: Modificar fecha Reserva
        if (idReservaGenerada != null) {
            try {
                System.out.println("--> Modificando fecha de Reserva...");
                Reserva cambioFecha = new Reserva(null, null, LocalDate.now().plusDays(30), 0, null, 0.0);
                rest.patchForObject(baseURL + "/reservas/" + idReservaGenerada + "/fecha", cambioFecha, Reserva.class);
                System.out.println("   [OK] Fecha retrasada 5 días.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }


        // ---------------------------------------------------------
        // PARTE 3: BORRADOS (DELETE) - REQUISITOS SEMANA 2
        // ---------------------------------------------------------
        
        System.out.println("\n--- PASO 3: BORRADOS Y RESTRICCIONES (DELETE) ---");

        // 3.1 Intentar borrar patrón con barco (Debe fallar - 409 Conflict)
        try {
            System.out.println("--> Intentando borrar Patrón activo (Esperando Error)...");
            rest.delete(baseURL + "/patrones/PATRON-CLI");
            System.out.println("   [FAIL] El patrón se borró y no debería.");
        } catch (HttpClientErrorException e) {
            System.out.println("   [OK] Bloqueado correctamente: " + e.getStatusCode());
        }

        // 3.2 Cancelar Reserva Futura (Debe funcionar)
        if (idReservaGenerada != null) {
            try {
                System.out.println("--> Cancelando Reserva futura...");
                rest.delete(baseURL + "/reservas/" + idReservaGenerada);
                System.out.println("   [OK] Reserva cancelada.");
            } catch (Exception e) { System.out.println("   [WARN] " + e.getMessage()); }
        }

        System.out.println("\n=== FIN DEMO 2 ===");
    }
}