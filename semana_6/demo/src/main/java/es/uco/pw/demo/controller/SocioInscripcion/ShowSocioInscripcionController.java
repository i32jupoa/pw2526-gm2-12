package es.uco.pw.demo.controller.SocioInscripcion;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.domain.SocioInscripcion;
import es.uco.pw.demo.model.repository.SocioInscripcionRepository;

@Controller
public class ShowSocioInscripcionController {
    
    private SocioInscripcionRepository socioInscripcionRepository;
    
    public ShowSocioInscripcionController(SocioInscripcionRepository socioInscripcionRepository) {
        this.socioInscripcionRepository = socioInscripcionRepository;
        this.socioInscripcionRepository.setSQLQueriesFileName("db/sql.properties");
    }
    
    @GetMapping("/showSocioInscripcion")
    public ModelAndView showSocioInscripcion() {
        ModelAndView modelAndView = new ModelAndView("socioinscripcion/showSocioInscripcionView");
        
        // Obtener todas las relaciones
        List<SocioInscripcion> allRelaciones = this.socioInscripcionRepository.findAllSocioInscripciones();
        
        modelAndView.addObject("relaciones", allRelaciones);
        
        return modelAndView;
    }
}