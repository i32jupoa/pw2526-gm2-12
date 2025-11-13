package es.uco.pw.demo.controller.Socio;

import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddSocioController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/addSocio")
    public ModelAndView addSocio() {
        Socio socio = new Socio(
                "12345678A",
                "Juan",
                "García López",
                LocalDate.of(1985, 5, 15),
                "Calle Mayor 123, Madrid",
                LocalDate.now(),
                true,
                false
        );

        socioRepository.setSQLQueriesFileName("src/main/resources/sql/socio_queries.sql");
        boolean success = socioRepository.addSocio(socio);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("socio/addSocioViewSuccess");
            mv.addObject("socio", socio);
        } else {
            mv = new ModelAndView("socio/addSocioViewFail");
        }
        return mv;
    }
}