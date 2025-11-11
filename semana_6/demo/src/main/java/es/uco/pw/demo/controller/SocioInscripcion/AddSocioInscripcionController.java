package main.java.es.uco.pw.demo.controller.socioInscripcion;

import main.java.es.uco.pw.demo.model.SocioInscripcion;
import main.java.es.uco.pw.demo.model.SocioInscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddSocioInscripcionController {

    @Autowired
    private SocioInscripcionRepository socioInscripcionRepository;

    @GetMapping("/addSocioInscripcion")
    public ModelAndView addSocioInscripcion() {
        SocioInscripcion socioInscripcion = new SocioInscripcion(
                1,
                100,
                "12345678A"
        );

        socioInscripcionRepository.setSQLQueriesFileName("src/main/resources/sql/socio_inscripcion_queries.sql");
        boolean success = socioInscripcionRepository.addSocioInscripcion(socioInscripcion);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("addSocioInscripcionViewSuccess");
            mv.addObject("socioInscripcion", socioInscripcion);
        } else {
            mv = new ModelAndView("addSocioInscripcionViewFail");
        }
        return mv;
    }
}
