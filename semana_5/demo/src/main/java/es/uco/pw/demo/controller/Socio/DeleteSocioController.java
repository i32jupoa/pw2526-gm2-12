package main.java.es.uco.pw.demo.controller.socio;

import main.java.es.uco.pw.demo.model.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteSocioController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/deleteSocio")
    public ModelAndView deleteSocio(@RequestParam("dni") String dni) {
        socioRepository.setSQLQueriesFileName("src/main/resources/sql/socio_queries.sql");
        boolean deleted = socioRepository.deleteSocio(dni);

        ModelAndView mv;
        if (deleted) {
            mv = new ModelAndView("deleteSocioViewSuccess");
            mv.addObject("dni", dni);
        } else {
            mv = new ModelAndView("deleteSocioViewFail");
        }
        return mv;
    }
}