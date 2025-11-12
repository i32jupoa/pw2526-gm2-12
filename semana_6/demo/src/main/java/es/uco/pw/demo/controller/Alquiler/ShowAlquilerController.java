package main.java.es.uco.pw.demo.controller.Alquiler;

import main.java.es.uco.pw.demo.model.Alquiler;
import main.java.es.uco.pw.demo.model.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ShowAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @GetMapping("/showAlquileres")
    public ModelAndView showAlquileres() {
        List<Alquiler> alquileres = alquilerRepository.findAll();
        ModelAndView mv = new ModelAndView("showAlquileresView");
        mv.addObject("alquileres", alquileres);
        return mv;
    }
}
