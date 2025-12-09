package es.uco.pw.demo.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.repository.SocioRepository;

@RestController
@RequestMapping(path="/api/socios", produces="application/json")
public class SocioRestController {

    @Autowired
    private SocioRepository socioRepository;

    // A.1: Obtener la lista completa de socios (GET /api/socios)
    @GetMapping
    public ResponseEntity<List<Socio>> getAllSocios() {
        List<Socio> socios = socioRepository.findAllSocios();
        if (socios == null || socios.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(socios, HttpStatus.OK);
    }

    // A.2: Obtener la información de un socio por DNI (GET /api/socios/{dni})
    @GetMapping("/{dni}")
    public ResponseEntity<Socio> getSocioByDni(@PathVariable String dni) {
        Socio socio = socioRepository.findSocioByDni(dni);
        if (socio != null) {
            return new ResponseEntity<>(socio, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // A.3: Crear un nuevo socio sin inscripción (POST /api/socios)
    @PostMapping(consumes="application/json")
    public ResponseEntity<Socio> createSocio(@RequestBody Socio socio) {
        // Validación simple: comprobar si ya existe
        if (socioRepository.findSocioByDni(socio.getDni()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        
        boolean created = socioRepository.addSocio(socio);
        if (created) {
            return new ResponseEntity<>(socio, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}