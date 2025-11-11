package main.java.es.uco.pw.demo.controller.Alquiler;

import main.java.es.uco.pw.demo.model.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    /**
     * Elimina un alquiler por su id pasado como parámetro en la URL:
     * /deleteAlquiler?id=123
     */
    @GetMapping("/deleteAlquiler")
    public ModelAndView deleteAlquiler(@RequestParam("id") int id) {
        ModelAndView mv;
        boolean deleted = alquilerRepository.deleteAlquilerById(id);
        if (deleted) {
            mv = new ModelAndView("deleteAlquilerViewSuccess");
            mv.addObject("id", id);
        } else {
            mv = new ModelAndView("deleteAlquilerViewFail");
            mv.addObject("id", id);
        }
        return mv;
    }
}
