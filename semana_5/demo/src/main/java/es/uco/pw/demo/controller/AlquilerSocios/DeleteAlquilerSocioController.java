package main.java.es.uco.pw.demo.controller.AlquilerSocios;

import main.java.es.uco.pw.demo.model.AlquilerSocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteAlquilerSocioController {

    @Autowired
    private AlquilerSocioRepository alquilerSocioRepository;

    /**
     * Elimina un registro de la tabla AlquilerSocio mediante idAlquiler y dniSocio:
     * /deleteAlquilerSocio?idAlquiler=10&dni=12345678A
     */
    @GetMapping("/deleteAlquilerSocio")
    public ModelAndView deleteAlquilerSocio(@RequestParam("idAlquiler") int idAlquiler,
                                            @RequestParam("dni") String dni) {
        ModelAndView mv;
        boolean deleted = alquilerSocioRepository.deleteAlquilerSocio(idAlquiler, dni);
        if (deleted) {
            mv = new ModelAndView("deleteAlquilerSocioViewSuccess");
            mv.addObject("idAlquiler", idAlquiler);
            mv.addObject("dni", dni);
        } else {
            mv = new ModelAndView("deleteAlquilerSocioViewFail");
            mv.addObject("idAlquiler", idAlquiler);
            mv.addObject("dni", dni);
        }
        return mv;
    }
}
