package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion; 
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

@RestController
@RequestMapping(path="/api/embarcaciones", produces="application/json")
public class EmbarcacionRestController {

    @Autowired
    private EmbarcacionRepository embarcacionRepository;

    // B.1: Obtener lista completa (GET /api/embarcaciones)
    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        List<Embarcacion> flota = embarcacionRepository.findAllEmbarcaciones();
        return new ResponseEntity<>(flota, HttpStatus.OK);
    }

    // B.2: Obtener lista por tipo (GET /api/embarcaciones?tipo=VELERO)
    @GetMapping(params="tipo")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByTipo(@RequestParam String tipo) {
        try {
            // Convertir String a Enum (manejo de error si el tipo no existe)
            TipoEmbarcacion tipoEnum = TipoEmbarcacion.valueOf(tipo.toUpperCase());
            List<Embarcacion> flota = embarcacionRepository.findEmbarcacionByTipo(tipoEnum);
            return new ResponseEntity<>(flota, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // B.4: Crear nueva embarcación (POST /api/embarcaciones)
    @PostMapping(consumes="application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion embarcacion) {
        // Validar unicidad de matrícula
        if (embarcacionRepository.findEmbarcacionByMatricula(embarcacion.getMatricula()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        boolean created = embarcacionRepository.addEmbarcacion(embarcacion);
        if (created) {
            return new ResponseEntity<>(embarcacion, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion; 
import es.uco.pw.demo.model.repository.EmbarcacionRepository;
import es.uco.pw.demo.model.repository.PatronRepository;

@RestController
@RequestMapping(path="/api/embarcaciones", produces="application/json")
public class EmbarcacionRestController {

    @Autowired
    private EmbarcacionRepository embarcacionRepository;
    
    @Autowired
    private PatronRepository patronRepository;

    // Obtener lista completa
    @GetMapping
    public ResponseEntity<List<Embarcacion>> getAllEmbarcaciones() {
        List<Embarcacion> flota = embarcacionRepository.findAllEmbarcaciones();
        return (flota != null && !flota.isEmpty()) 
                ? new ResponseEntity<>(flota, HttpStatus.OK) 
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtener lista por tipo
    @GetMapping(params="tipo")
    public ResponseEntity<List<Embarcacion>> getEmbarcacionesByTipo(@RequestParam String tipo) {
        try {
            TipoEmbarcacion tipoEnum = TipoEmbarcacion.valueOf(tipo.toUpperCase());
            List<Embarcacion> flota = embarcacionRepository.findEmbarcacionByTipo(tipoEnum);
            return new ResponseEntity<>(flota, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Crear nueva embarcación
    @PostMapping(consumes="application/json")
    public ResponseEntity<Embarcacion> createEmbarcacion(@RequestBody Embarcacion embarcacion) {
        if (embarcacionRepository.findEmbarcacionByMatricula(embarcacion.getMatricula()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        boolean created = embarcacionRepository.addEmbarcacion(embarcacion);
        return created ? new ResponseEntity<>(embarcacion, HttpStatus.CREATED) 
                       : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Actualizar información, Vincular o Desvincular Patrón.

    @PatchMapping("/{matricula}")
    public ResponseEntity<Embarcacion> updateEmbarcacion(@PathVariable String matricula, @RequestBody Embarcacion updates) {
        Embarcacion barco = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        if (barco == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (updates.getNombre() != null) barco.setNombre(updates.getNombre());
        if (updates.getTipo() != null) barco.setTipo(updates.getTipo());
        if (updates.getNumPlazas() > 0) barco.setNumPlazas(updates.getNumPlazas()); // Validación básica > 0
        if (updates.getDimensiones() != null) barco.setDimensiones(updates.getDimensiones());

        
        
        if (updates.getDniPatron() != null) {
            if (updates.getDniPatron().isEmpty()) {
                barco.setDniPatron(null);
            } else {
                if (patronRepository.findPatronByDni(updates.getDniPatron()) != null) {
                    barco.setDniPatron(updates.getDniPatron());
                } else {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
                }
            }
        }

        boolean success = embarcacionRepository.updateEmbarcacion(barco);
        return success ? new ResponseEntity<>(barco, HttpStatus.OK) 
                       : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Eliminar embarcación si no tiene alquileres ni reservas (DELETE).

    @DeleteMapping("/{matricula}")
    public ResponseEntity<Void> deleteEmbarcacion(@PathVariable String matricula) {
        if (embarcacionRepository.findEmbarcacionByMatricula(matricula) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        int alquileres = embarcacionRepository.countAlquileres(matricula);
        int reservas = embarcacionRepository.countReservas(matricula);

        if (alquileres > 0 || reservas > 0) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }

        boolean deleted = embarcacionRepository.deleteEmbarcacion(matricula);
        return deleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
                       : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}