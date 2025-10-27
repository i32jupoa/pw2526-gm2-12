package es.uco.pw.demo.controller.inscripcion;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uco.pw.demo.model.Inscripcion;
import es.uco.pw.demo.model.InscripcionRepository;
import es.uco.pw.demo.model.TipoInscripcion;

@Controller
public class AddInscripcionController {

    private InscripcionRepository inscripcionRepository;
        
    public AddInscripcionController(InscripcionRepository inscripcionRepository){
        this.inscripcionRepository = inscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/addInscripcion")
    public String addInscripcion(
        @RequestParam("tipo") String tipo,
        @RequestParam("cuotaActual") double cuotaActual,
        @RequestParam("dniTitular") String dniTitular
    ) {
        LocalDate fechaCreacion = LocalDate.now();
        Inscripcion inscripcion = new Inscripcion(
            0, // el ID se genera automáticamente si la tabla está en AUTO_INCREMENT
            fechaCreacion,
            TipoInscripcion.valueOf(tipo.toUpperCase()),
            cuotaActual,
            dniTitular
        );

        boolean success = inscripcionRepository.addInscripcion(inscripcion);
        String nextPage;
        if(success)
            nextPage = "addInscripcionViewSuccess.html";
        else
            nextPage = "addInscripcionViewFail.html";
        return nextPage;
    }
}
