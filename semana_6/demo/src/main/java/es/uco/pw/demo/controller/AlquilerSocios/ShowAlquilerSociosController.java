package main.java.es.uco.pw.demo.controller.AlquilerSocios;

import main.java.es.uco.pw.demo.model.AlquilerSocio;
import main.java.es.uco.pw.demo.model.AlquilerSocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ShowAlquilerSociosController {

    @Autowired
    private AlquilerSocioRepository alquilerSocioRepository;

    @GetMapping("/showAlquilerSocios")
    public ModelAndView showAlquilerSocios() {
        List<AlquilerSocio> alquilerSocios = alquilerSocioRepository.findAll();
        ModelAndView mv = new ModelAndView("showAlquilerSociosView");
        mv.addObject("alquilerSocios", alquilerSocios);
        return mv;
    }
}
