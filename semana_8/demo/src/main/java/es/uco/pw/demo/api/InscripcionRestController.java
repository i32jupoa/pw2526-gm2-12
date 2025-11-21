package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Inscripcion;
import es.uco.pw.demo.model.domain.TipoInscripcion;
import es.uco.pw.demo.model.repository.InscripcionRepository;

@RestController
@RequestMapping(path="/api/inscripciones", produces="application/json")
public class InscripcionRestController {

    @Autowired
    private InscripcionRepository inscripcionRepository;

    /**
     * Obtiene todas las inscripciones (Opcional, general).
     */
    @GetMapping
    public ResponseEntity<List<Inscripcion>> getAllInscripciones() {
        List<Inscripcion> lista = inscripcionRepository.findAllInscripciones();
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * Obtiene la lista de inscripciones INDIVIDUALES (Requisito A.4.1).
     * GET /api/inscripciones/individual
     */
    @GetMapping("/individual")
    public ResponseEntity<List<Inscripcion>> getInscripcionesIndividuales() {
        List<Inscripcion> lista = inscripcionRepository.findInscripcionesByTipo(TipoInscripcion.INDIVIDUAL);
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * Obtiene la lista de inscripciones FAMILIARES (Requisito A.4.2).
     * GET /api/inscripciones/familiar
     */
    @GetMapping("/familiar")
    public ResponseEntity<List<Inscripcion>> getInscripcionesFamiliares() {
        List<Inscripcion> lista = inscripcionRepository.findInscripcionesByTipo(TipoInscripcion.FAMILIAR);
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    /**
     * Obtiene la información de una inscripción dado el DNI del titular (Requisito A.4.3).
     * GET /api/inscripciones/titular/{dni}
     */
    @GetMapping("/titular/{dni}")
    public ResponseEntity<Inscripcion> getInscripcionByTitular(@PathVariable String dni) {
        // NOTA: Asegúrate de tener este método 'findInscripcionByDniTitular' en tu repositorio
        // Si no lo tienes, créalo en InscripcionRepository igual que los otros finders.
        Inscripcion inscripcion = inscripcionRepository.findInscripcionByDniTitular(dni);
        
        if (inscripcion != null) {
            return new ResponseEntity<>(inscripcion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una inscripción para un socio titular (Requisito A.4.4).
     * POST /api/inscripciones
     */
    @PostMapping(consumes="application/json")
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion inscripcion) {
        // Aquí podrías validar si el socio existe antes de crear la inscripción
        
        boolean created = inscripcionRepository.addInscripcion(inscripcion);
        if (created) {
            return new ResponseEntity<>(inscripcion, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}