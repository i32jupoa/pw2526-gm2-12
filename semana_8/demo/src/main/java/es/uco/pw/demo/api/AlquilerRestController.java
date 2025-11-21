package es.uco.pw.demo.api;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Alquiler;
import es.uco.pw.demo.model.repository.AlquilerRepository;

@RestController
@RequestMapping(path="/api/alquileres", produces="application/json")
public class AlquilerRestController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @GetMapping
    public ResponseEntity<List<Alquiler>> getAllAlquileres() {
        List<Alquiler> alquileres = alquilerRepository.findAllAlquileres();
        if (alquileres == null || alquileres.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(alquileres, HttpStatus.OK);
    }

    @GetMapping(params = "fecha")
    public ResponseEntity<List<Alquiler>> getAlquileresFuturos(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
       
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alquiler> getAlquilerById(@PathVariable int id) {
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler != null) {
            return new ResponseEntity<>(alquiler, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(consumes="application/json")
    public ResponseEntity<Alquiler> createAlquiler(@RequestBody Alquiler alquiler) {

        boolean created = alquilerRepository.addAlquiler(alquiler);
        if (created) {
            return new ResponseEntity<>(alquiler, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}