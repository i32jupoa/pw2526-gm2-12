package es.uco.pw.demo.controller.Embarcacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

@Controller
public class FindEmbarcacionByMatriculaController {

    private EmbarcacionRepository embarcacionRepository;

    public FindEmbarcacionByMatriculaController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/findEmbarcacionByMatricula")
    public ModelAndView findEmbarcacionByMatricula(
            @RequestParam(value = "matricula", required = false) String matricula
    ) {
        // Si no hay matrícula, mostrar el formulario
        if (matricula == null) {
            return new ModelAndView("embarcacion/findEmbarcacionByMatricula.html");
        }

        Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matricula);
        ModelAndView model = new ModelAndView("embarcacion/findEmbarcacionByMatricula.html");
        model.addObject("embarcacion", embarcacion);
        return model;
    }
}
