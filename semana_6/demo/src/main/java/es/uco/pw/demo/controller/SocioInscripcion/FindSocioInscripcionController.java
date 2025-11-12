package main.java.es.uco.pw.demo.controller.SocioInscripcion;

import main.java.es.uco.pw.demo.model.SocioInscripcion;
import main.java.es.uco.pw.demo.model.SocioInscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindSocioInscripcionController {

    @Autowired
    private SocioInscripcionRepository socioInscripcionRepository;

    @GetMapping("/findSocioInscripcion")
    public ModelAndView findSocioInscripcion(@RequestParam("id") int id) {
        socioInscripcionRepository.setSQLQueriesFileName("src/main/resources/sql/socio_inscripcion_queries.sql");
        SocioInscripcion socioInscripcion = socioInscripcionRepository.findById(id);

        ModelAndView mv;
        if (socioInscripcion != null) {
            mv = new ModelAndView("findSocioInscripcionViewSuccess");
            mv.addObject("socioInscripcion", socioInscripcion);
        } else {
            mv = new ModelAndView("findSocioInscripcionViewFail");
        }
        return mv;
    }
}
