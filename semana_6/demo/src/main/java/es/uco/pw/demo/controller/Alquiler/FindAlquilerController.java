package es.uco.pw.demo.controller.Alquiler;

import es.uco.pw.demo.model.domain.Alquiler;
import es.uco.pw.demo.model.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @GetMapping("/findAlquiler")
    public ModelAndView showForm() {
        return new ModelAndView("alquiler/findAlquiler");
    }

    @PostMapping("/findAlquiler")
    public ModelAndView processForm(@RequestParam("id") int id) {
        ModelAndView modelAndView;
        Alquiler alquiler = alquilerRepository.findAlquilerById(id);
        if (alquiler != null) {
            modelAndView = new ModelAndView("alquiler/findAlquilerViewSuccess");
            modelAndView.addObject("alquiler", alquiler);
        } else {
            modelAndView = new ModelAndView("alquiler/findAlquilerViewFail");
        }
        return modelAndView;
    }
}
