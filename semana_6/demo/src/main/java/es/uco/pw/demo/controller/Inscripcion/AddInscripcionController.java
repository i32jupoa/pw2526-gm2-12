package es.uco.pw.demo.controller.Inscripcion;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uco.pw.demo.model.domain.Inscripcion;
import es.uco.pw.demo.model.repository.InscripcionRepository;
import es.uco.pw.demo.model.domain.TipoInscripcion;

@Controller
public class AddInscripcionController {

    private InscripcionRepository inscripcionRepository;
        
    public AddInscripcionController(InscripcionRepository inscripcionRepository){
        this.inscripcionRepository = inscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // Mostrar el formulario
    @GetMapping("/addInscripcionForm")
    public String showAddInscripcionForm() {
        return "inscripcion/addInscripcionForm";
    }

    // Procesar el formulario
    @GetMapping("/addInscripcion")
    public String addInscripcion(
        @RequestParam("tipo") String tipo,
        @RequestParam("cuotaActual") double cuotaActual,
        @RequestParam("dniTitular") String dniTitular,
        Model model
    ) {
        LocalDate fechaCreacion = LocalDate.now();
        Inscripcion inscripcion = new Inscripcion(
            0,
            fechaCreacion,
            TipoInscripcion.valueOf(tipo.toUpperCase()),
            cuotaActual,
            dniTitular
        );

        boolean success = inscripcionRepository.addInscripcion(inscripcion);
        String nextPage;
        if(success) {
            model.addAttribute("inscripcion", inscripcion);
            nextPage = "inscripcion/addInscripcionViewSuccess";
        } else {
            nextPage = "inscripcion/addInscripcionViewFail";
        }
        return nextPage;
    }
}