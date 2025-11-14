package es.uco.pw.demo.controller.SocioInscripcion;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import es.uco.pw.demo.model.domain.SocioInscripcion;
import es.uco.pw.demo.model.repository.SocioInscripcionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchSocioInscripcionByInscripcionController {
    
    SocioInscripcionRepository socioInscripcionRepository;
    private ModelAndView modelAndView = new ModelAndView();
    
    public SearchSocioInscripcionByInscripcionController(SocioInscripcionRepository socioInscripcionRepository) {
        this.socioInscripcionRepository = socioInscripcionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioInscripcionRepository.setSQLQueriesFileName(sqlQueriesFileName);
        this.modelAndView.setViewName("viewName:searchSocioInscripcionByInscripcionView.html");
    }
    
    @GetMapping("/searchSocioInscripcionByInscripcion")
    public ModelAndView searchSocioInscripcionByInscripcion(@RequestParam(value = "idInscripcion", defaultValue = "0") int idInscripcion) {
        if (idInscripcion > 0) {
            List<SocioInscripcion> listOfSocioInscripciones = this.socioInscripcionRepository.findByIdInscripcion(idInscripcion);
            this.modelAndView.addObject("attributeName:listOfSocioInscripciones", listOfSocioInscripciones);
            this.modelAndView.addObject("idInscripcion", idInscripcion);
        }
        
        return this.modelAndView;
    }
}