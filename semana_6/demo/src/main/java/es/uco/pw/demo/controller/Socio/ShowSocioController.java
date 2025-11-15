package es.uco.pw.demo.controller.Socio;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.repository.SocioRepository;

@Controller
public class ShowSocioController {
    
    private SocioRepository socioRepository;
    
    public ShowSocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "db/sql.properties";
        this.socioRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }
    
    @GetMapping("/showSocio")
    public ModelAndView showSocio() {
        ModelAndView modelAndView = new ModelAndView("socio/showSocioView");
        
        // Obtener todos los socios
        List<Socio> allSocios = this.socioRepository.findAllSocios();
        
        modelAndView.addObject("socios", allSocios);
        
        return modelAndView;
    }
}