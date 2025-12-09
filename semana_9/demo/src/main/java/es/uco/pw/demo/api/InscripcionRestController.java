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

    @GetMapping
    public ResponseEntity<List<Inscripcion>> getAllInscripciones() {
        List<Inscripcion> lista = inscripcionRepository.findAllInscripciones();
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/individual")
    public ResponseEntity<List<Inscripcion>> getInscripcionesIndividuales() {
        List<Inscripcion> lista = inscripcionRepository.findInscripcionesByTipo(TipoInscripcion.INDIVIDUAL);
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/familiar")
    public ResponseEntity<List<Inscripcion>> getInscripcionesFamiliares() {
        List<Inscripcion> lista = inscripcionRepository.findInscripcionesByTipo(TipoInscripcion.FAMILIAR);
        if (lista == null || lista.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(lista, HttpStatus.OK);
    }

    @GetMapping("/titular/{dni}")
    public ResponseEntity<Inscripcion> getInscripcionByTitular(@PathVariable String dni) {
        Inscripcion inscripcion = inscripcionRepository.findInscripcionByDniTitular(dni);
        
        if (inscripcion != null) {
            return new ResponseEntity<>(inscripcion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes="application/json")
    public ResponseEntity<Inscripcion> createInscripcion(@RequestBody Inscripcion inscripcion) { 
        boolean created = inscripcionRepository.addInscripcion(inscripcion);
        if (created) {
            return new ResponseEntity<>(inscripcion, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}