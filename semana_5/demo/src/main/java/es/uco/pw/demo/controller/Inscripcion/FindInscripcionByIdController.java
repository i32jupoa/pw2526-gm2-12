package es.uco.pw.demo.controller.Inscripcion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.domain.Inscripcion;
import es.uco.pw.demo.model.repository.InscripcionRepository;

@Controller
public class FindInscripcionByIdController {

    private InscripcionRepository inscripcionRepository;

    public FindInscripcionByIdController(InscripcionRepository inscripcionRepository){
        this.inscripcionRepository = inscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.inscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/findInscripcionById")
    public ModelAndView findInscripcionById(@RequestParam("id") int id){
        Inscripcion inscripcion = inscripcionRepository.findInscripcionById(id);
        ModelAndView model = new ModelAndView("inscripcion/findInscripcionById.html");
        model.addObject("inscripcion", inscripcion);
        return model;
    }
}
