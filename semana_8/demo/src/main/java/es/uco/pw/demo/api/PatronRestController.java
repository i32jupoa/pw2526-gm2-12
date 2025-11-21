package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.repository.PatronRepository;

@RestController
@RequestMapping(path="/api/patrones", produces="application/json")
public class PatronRestController {

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping
    public ResponseEntity<List<Patron>> getAllPatrones() {
        List<Patron> patrones = patronRepository.findAllPatrones();
        if (patrones == null || patrones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(patrones, HttpStatus.OK);
    }

    @PostMapping(consumes="application/json")
    public ResponseEntity<Patron> createPatron(@RequestBody Patron patron) {
        if (patronRepository.findPatronByDni(patron.getDni()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); 
        }

        boolean created = patronRepository.addPatron(patron);
        if (created) {
            return new ResponseEntity<>(patron, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}