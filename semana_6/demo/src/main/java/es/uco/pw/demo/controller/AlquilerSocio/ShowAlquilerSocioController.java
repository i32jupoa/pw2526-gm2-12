package main.java.es.uco.pw.demo.controller.AlquilerSocio;

import main.java.es.uco.pw.demo.model.AlquilerSocio;
import main.java.es.uco.pw.demo.model.AlquilerSocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ShowAlquilerSocioController {

    @Autowired
    private AlquilerSocioRepository alquilerSocioRepository;

    @GetMapping("/showAlquilerSocio")
    public ModelAndView showAlquilerSocio() {
        List<AlquilerSocio> alquilerSocio = alquilerSocioRepository.findAll();
        ModelAndView mv = new ModelAndView("showAlquilerSocioView");
        mv.addObject("alquilerSocio", alquilerSocio);
        return mv;
    }
}
