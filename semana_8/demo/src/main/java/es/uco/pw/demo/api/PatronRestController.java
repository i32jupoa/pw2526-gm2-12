package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.repository.PatronRepository;

/**
 * Controlador REST para la gestión de Patrones.
 * Se encarga de las operaciones CRUD expuestas vía API.
 */
@RestController
@RequestMapping(path="/api/patrones", produces="application/json")
public class PatronRestController {

    @Autowired
    private PatronRepository patronRepository;

    /**
     * Obtiene la lista completa de patrones (Requisito B.3).
     * @return Lista de patrones o NO_CONTENT si está vacía.
     */
    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        List<Patron> patrones = patronRepository.findAllPatrones();
        if (patrones == null || patrones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(patrones, HttpStatus.OK);
    }

    /**
     * Crea un nuevo patrón sin asociarlo a ninguna embarcación (Requisito B.5).
     * @param patron Objeto Patron recibido en el cuerpo de la petición.
     * @return El patrón creado (CREATED) o un error si ya existe (CONFLICT) o falla (INTERNAL_SERVER_ERROR).
     */
    @PostMapping(consumes="application/json")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron patron) {
        // Validación simple: comprobar si el DNI ya existe
        if (patronRepository.findPatronByDni(patron.getDni()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict: DNI duplicado
        }

        boolean created = patronRepository.addPatron(patron);
        if (created) {
            return new ResponseEntity<>(patron, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}