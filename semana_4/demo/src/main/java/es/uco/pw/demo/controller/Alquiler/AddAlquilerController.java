package es.uco.pw.demo.controller.Alquiler;

import es.uco.pw.demo.model.Alquiler;
import es.uco.pw.demo.model.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@SessionAttributes("alquiler")
public class AddAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @GetMapping("/addAlquiler")
    public ModelAndView showAddAlquilerForm() {
        ModelAndView modelAndView = new ModelAndView("addAlquilerForm");
        modelAndView.addObject("alquiler", new Alquiler(0, 0, "", LocalDate.now(), LocalDate.now(), 0, 0.0));
        return modelAndView;
    }

    @PostMapping("/addAlquiler")
    public ModelAndView processAddAlquiler(@ModelAttribute("alquiler") Alquiler alquiler, SessionStatus status) {
        ModelAndView modelAndView;
        boolean success = alquilerRepository.addAlquiler(alquiler);
        if (success) {
            modelAndView = new ModelAndView("addAlquilerViewSuccess");
            modelAndView.addObject("alquiler", alquiler);
        } else {
            modelAndView = new ModelAndView("addAlquilerViewFail");
        }
        status.setComplete();
        return modelAndView;
    }
}
