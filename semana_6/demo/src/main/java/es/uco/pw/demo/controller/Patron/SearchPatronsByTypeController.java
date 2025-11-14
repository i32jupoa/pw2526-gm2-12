package es.uco.pw.demo.controller.Patron;

import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchPatronsByTypeController {

    @Autowired
    private PatronRepository patronRepository;

    /**
     * Busca patrones por un "type" (p. ej. tipo de socio). 
     * Nota: asume que PatronRepository implementa findPatronesByType(String type).
     * Si prefieres que se haga el filtrado en memoria usando findAllPatrones(), dímelo y lo cambio.
     */
    @GetMapping("/searchPatronsByType")
    public ModelAndView searchPatronsByType(@RequestParam("type") String type) {
        patronRepository.setSQLQueriesFileName("src/main/resources/sql/patron_queries.sql");

        List<Patron> resultados = null;
        try {
            // Método esperado en PatronRepository: List<Patron> findPatronesByType(String type)
            resultados = patronRepository.findPatronesByType(type);
        } catch (Exception e) {
            System.err.println("Error searching patrons by type = " + type);
            e.printStackTrace();
        }

        ModelAndView mv = new ModelAndView("searchPatronsByTypeView");
        mv.addObject("type", type);
        mv.addObject("patrones", resultados);
        return mv;
    }
}
