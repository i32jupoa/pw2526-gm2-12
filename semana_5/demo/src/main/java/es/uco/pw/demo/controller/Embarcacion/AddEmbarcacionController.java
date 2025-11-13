package es.uco.pw.demo.controller.Embarcacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

@Controller
public class AddEmbarcacionController {

    private EmbarcacionRepository embarcacionRepository;
        
    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/addEmbarcacion")
    public String addEmbarcacion(
        @RequestParam("matricula") String matricula,
        @RequestParam("nombre") String nombre,
        @RequestParam("tipo") String tipo,
        @RequestParam("numPlazas") int numPlazas,
        @RequestParam("dimensiones") String dimensiones,
        @RequestParam(value = "dniPatron", required = false) String dniPatron
    ) {

        Embarcacion embarcacion = new Embarcacion(
            matricula,
            nombre,
            TipoEmbarcacion.valueOf(tipo.toUpperCase()),
            numPlazas,
            dimensiones,
            dniPatron
        );

        boolean success = embarcacionRepository.addEmbarcacion(embarcacion);

        String nextPage;
        if (success) {
            nextPage = "embarcacion/addEmbarcacionViewSuccess.html";
        } else {
            nextPage = "embarcacion/addEmbarcacionViewFail.html";
        }
        return nextPage;
    }
}
