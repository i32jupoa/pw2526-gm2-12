package es.uco.pw.demo.controller.AlquilerSocio;

import es.uco.pw.demo.model.domain.AlquilerSocio;
import es.uco.pw.demo.model.repository.AlquilerSocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FindAlquilerSocioController {

    @Autowired
    private AlquilerSocioRepository alquilerSocioRepository;

    /**
     * Buscar por idAlquiler:
     * /findAlquilerSocioById?idAlquiler=10
     */
    @GetMapping("/findAlquilerSocioById")
    public ModelAndView findByIdAlquiler(@RequestParam("idAlquiler") int idAlquiler) {
        ModelAndView mv;
        List<AlquilerSocio> results = alquilerSocioRepository.findByIdAlquiler(idAlquiler);
        if (results != null && !results.isEmpty()) {
            mv = new ModelAndView("alquilerSocios/findAlquilerSocioByIdViewSuccess");
            mv.addObject("alquilerSocios", results);
            mv.addObject("idAlquiler", idAlquiler);
        } else {
            mv = new ModelAndView("alquilerSocios/findAlquilerSocioByIdViewFail");
            mv.addObject("idAlquiler", idAlquiler);
        }
        return mv;
    }

    /**
     * Buscar por dniSocio:
     * /findAlquilerSocioByDni?dni=12345678A
     */
    @GetMapping("/findAlquilerSocioByDni")
    public ModelAndView findByDni(@RequestParam("dni") String dni) {
        ModelAndView mv;
        List<AlquilerSocio> results = alquilerSocioRepository.findByDniSocio(dni);
        if (results != null && !results.isEmpty()) {
            mv = new ModelAndView("alquilerSocios/findAlquilerSocioByDniViewSuccess");
            mv.addObject("alquilerSocios", results);
            mv.addObject("dni", dni);
        } else {
            mv = new ModelAndView("alquilerSocios/findAlquilerSocioByDniViewFail");
            mv.addObject("dni", dni);
        }
        return mv;
    }
}
