package es.uco.pw.demo.controller.Socio;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import es.uco.pw.demo.model.domain.Socio;
import es.uco.pw.demo.model.repository.SocioRepository;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShowSociosByTitularController {
    
    SocioRepository socioRepository;
    private ModelAndView modelAndView = new ModelAndView();
    
    public ShowSociosByTitularController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.socioRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }
    
    @GetMapping("/showSociosByTitular")
    public ModelAndView showSociosByTitular() {
        this.modelAndView.setViewName("showSociosByTitularView.html");
        
        // Obtener todos los socios
        List<Socio> allSocios = this.socioRepository.findAllSocios();
        
        // Contar titulares y no titulares
        int numTitulares = 0;
        int numNoTitulares = 0;
        
        if (allSocios != null) {
            for (Socio socio : allSocios) {
                if (socio.isEsTitular()) {
                    numTitulares++;
                } else {
                    numNoTitulares++;
                }
            }
        }
        
        this.modelAndView.addObject("numTitulares", numTitulares);
        this.modelAndView.addObject("numNoTitulares", numNoTitulares);
        this.modelAndView.addObject("totalSocios", numTitulares + numNoTitulares);
        
        return modelAndView;
    }
}