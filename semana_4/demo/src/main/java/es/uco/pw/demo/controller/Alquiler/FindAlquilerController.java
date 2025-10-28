package main.java.es.uco.pw.demo.controller.Alquiler;

import main.java.es.uco.pw.demo.model.Alquiler;
import main.java.es.uco.pw.demo.model.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @GetMapping("/findAlquiler")
    public ModelAndView findAlquiler(@RequestParam("id") int id) {
        ModelAndView modelAndView;
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler != null) {
            modelAndView = new ModelAndView("findAlquilerViewSuccess");
            modelAndView.addObject("alquiler", alquiler);
        } else {
            modelAndView = new ModelAndView("findAlquilerViewFail");
        }
        return modelAndView;
    }
}