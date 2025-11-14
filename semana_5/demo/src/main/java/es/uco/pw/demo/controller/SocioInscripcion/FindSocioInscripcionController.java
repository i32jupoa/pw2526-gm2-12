package es.uco.pw.demo.controller.SocioInscripcion;

import es.uco.pw.demo.model.domain.SocioInscripcion;
import es.uco.pw.demo.model.repository.SocioInscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class FindSocioInscripcionController {

    @Autowired
    private SocioInscripcionRepository socioInscripcionRepository;

    // Buscar por ID
    @GetMapping("/findSocioInscripcionById")
    public ModelAndView findSocioInscripcionById(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView mv;
        
        // Si no hay ID, mostrar el formulario vacío
        if (id == null) {
            mv = new ModelAndView("socioInscripcion/findSocioInscripcionById");
            mv.addObject("socioInscripcion", null);
            return mv;
        }
        
        // Si hay ID, buscar la relación socio-inscripción
        socioInscripcionRepository.setSQLQueriesFileName("/db/sql.properties");
        SocioInscripcion socioInscripcion = socioInscripcionRepository.findById(id);

        mv = new ModelAndView("socioInscripcion/findSocioInscripcionById");
        mv.addObject("socioInscripcion", socioInscripcion);
        
        return mv;
    }

    // Buscar por DNI del Socio
    @GetMapping("/findSocioInscripcionByDni")
    public ModelAndView findSocioInscripcionByDni(@RequestParam(value = "dniSocio", required = false) String dniSocio) {
        ModelAndView mv;
        
        // Si no hay DNI, mostrar el formulario vacío
        if (dniSocio == null || dniSocio.isEmpty()) {
            mv = new ModelAndView("socioInscripcion/findSocioInscripcionesByDni");
            mv.addObject("socioInscripciones", null);
            return mv;
        }
        
        // Si hay DNI, buscar las inscripciones del socio
        socioInscripcionRepository.setSQLQueriesFileName("/db/sql.properties");
        List<SocioInscripcion> socioInscripciones = socioInscripcionRepository.findByDniSocio(dniSocio);

        mv = new ModelAndView("socioInscripcion/findSocioInscripcionesByDni");
        mv.addObject("socioInscripciones", socioInscripciones);
        
        return mv;
    }
}