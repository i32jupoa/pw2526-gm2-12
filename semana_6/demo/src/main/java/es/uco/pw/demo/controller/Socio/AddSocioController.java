package es.uco.pw.demo.controller.Socio;

import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.repository.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddSocioController {

    @Autowired
    private SocioRepository socioRepository;

    @GetMapping("/addSocioForm")
    public String mostrarFormulario() {
        return "socio/addSocioForm";
    }

    @PostMapping("/addSocio")
    public ModelAndView addSocio(
            @RequestParam String dni,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam String fechaNacimiento,
            @RequestParam String direccion,
            @RequestParam(required = false, defaultValue = "false") boolean esTitular,
            @RequestParam(required = false, defaultValue = "false") boolean tieneTituloPatron
    ) {
        Socio socio = new Socio(
                dni,
                nombre,
                apellidos,
                LocalDate.parse(fechaNacimiento),
                direccion,
                LocalDate.now(),
                esTitular,
                tieneTituloPatron
        );

        socioRepository.setSQLQueriesFileName("db/sql.properties");
        boolean success = socioRepository.addSocio(socio);

        if (success) {
            ModelAndView mv = new ModelAndView("socio/addSocioViewSuccess");
            mv.addObject("socio", socio);
            return mv;
        } else {
            return new ModelAndView("socio/addSocioViewFail");
        }
    }
}