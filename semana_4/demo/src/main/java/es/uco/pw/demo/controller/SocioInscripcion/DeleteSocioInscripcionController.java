package es.uco.pw.demo.controller.socioInscripcion;

import es.uco.pw.demo.model.SocioInscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteSocioInscripcionController {

    @Autowired
    private SocioInscripcionRepository socioInscripcionRepository;

    @GetMapping("/deleteSocioInscripcion")
    public ModelAndView deleteSocioInscripcion(@RequestParam("id") int id) {
        socioInscripcionRepository.setSQLQueriesFileName("src/main/resources/sql/socio_inscripcion_queries.sql");
        boolean deleted = socioInscripcionRepository.deleteSocioInscripcion(id);

        ModelAndView mv;
        if (deleted) {
            mv = new ModelAndView("deleteSocioInscripcionViewSuccess");
            mv.addObject("id", id);
        } else {
            mv = new ModelAndView("deleteSocioInscripcionViewFail");
        }
        return mv;
    }
}
