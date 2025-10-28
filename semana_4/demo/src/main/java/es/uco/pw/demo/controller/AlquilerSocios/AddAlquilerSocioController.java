package main.java.es.uco.pw.demo.controller.AlquilerSocios;

import main.java.es.uco.pw.demo.model.AlquilerSocio;
import main.java.es.uco.pw.demo.model.AlquilerSocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("alquilerSocio")
public class AddAlquilerSocioController {

    @Autowired
    private AlquilerSocioRepository alquilerSocioRepository;

    @GetMapping("/addAlquilerSocio")
    public ModelAndView showAddAlquilerSocioForm() {
        ModelAndView modelAndView = new ModelAndView("addAlquilerSocioForm");
        modelAndView.addObject("alquilerSocio", new AlquilerSocio(0, ""));
        return modelAndView;
    }

    @PostMapping("/addAlquilerSocio")
    public ModelAndView processAddAlquilerSocio(@ModelAttribute("alquilerSocio") AlquilerSocio alquilerSocio, SessionStatus status) {
        ModelAndView modelAndView;
        boolean success = alquilerSocioRepository.addAlquilerSocio(alquilerSocio);
        if (success) {
            modelAndView = new ModelAndView("addAlquilerSocioViewSuccess");
            modelAndView.addObject("alquilerSocio", alquilerSocio);
        } else {
            modelAndView = new ModelAndView("addAlquilerSocioViewFail");
        }
        status.setComplete();
        return modelAndView;
    }
}
