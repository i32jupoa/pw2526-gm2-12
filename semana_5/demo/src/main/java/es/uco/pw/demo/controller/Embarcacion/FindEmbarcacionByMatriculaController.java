package es.uco.pw.demo.controller.Embarcacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

@Controller
public class FindEmbarcacionByMatriculaController {

    private final EmbarcacionRepository embarcacionRepository;

    public FindEmbarcacionByMatriculaController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/findEmbarcacionByMatricula")
    public ModelAndView findEmbarcacionByMatricula(
            @RequestParam(value = "matricula", required = false) String matricula
    ) {
        ModelAndView model = new ModelAndView("embarcacion/findEmbarcacionByMatricula.html");

        // Solo buscar si hay parámetro
        if (matricula != null && !matricula.isEmpty()) {
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matricula);
            model.addObject("embarcacion", embarcacion);
            model.addObject("buscado", true); // Indicador para el HTML
        } else {
            model.addObject("buscado", false);
        }

        return model;
    }
}
