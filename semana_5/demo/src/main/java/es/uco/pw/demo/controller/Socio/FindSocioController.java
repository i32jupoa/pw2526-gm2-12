package es.uco.pw.demo.controller.Socio;

import es.uco.pw.demo.model.Socio;
import es.uco.pw.demo.model.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindSocioController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/findSocio")
    public ModelAndView findSocio(@RequestParam("dni") String dni) {
        socioRepository.setSQLQueriesFileName("src/main/resources/sql/socio_queries.sql");
        Socio socio = socioRepository.findSocioByDni(dni);

        ModelAndView mv;
        if (socio != null) {
            mv = new ModelAndView("socio/findSocioViewSuccess");
            mv.addObject("socio", socio);
        } else {
            mv = new ModelAndView("socio/findSocioViewFail");
        }
        return mv;
    }
}