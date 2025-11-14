package es.uco.pw.demo.controller.Embarcacion;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class AddEmbarcacionController {

    private EmbarcacionRepository embarcacionRepository;

    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(
            @RequestParam("matricula") String matricula,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipo") String tipo,
            @RequestParam("numPlazas") int numPlazas,
            @RequestParam("dimensiones") String dimensiones,
            @RequestParam(value = "dniPatron", required = false) String dniPatron
    ) {
        Embarcacion embarcacion = new Embarcacion(
                matricula, nombre, TipoEmbarcacion.valueOf(tipo.toUpperCase()), numPlazas, dimensiones, dniPatron
        );

        boolean success = embarcacionRepository.addEmbarcacion(embarcacion);
        ModelAndView model;

        if (success) {
            model = new ModelAndView("embarcacion/addEmbarcacionViewSuccess");
            model.addObject("embarcacion", embarcacion);
        } else {
            model = new ModelAndView("embarcacion/addEmbarcacionViewFail");
        }

        return model;
    }

    @GetMapping("/findEmbarcacionesByTipo")
    public ModelAndView findEmbarcacionesByTipo(@RequestParam("tipo") String tipo) {
        TipoEmbarcacion tipoEnum = TipoEmbarcacion.valueOf(tipo.toUpperCase());
        List<Embarcacion> embarcaciones = embarcacionRepository.findEmbarcacionesByTipo(tipoEnum);

        ModelAndView model = new ModelAndView("embarcacion/findEmbarcacionByTipo");
        model.addObject("embarcaciones", embarcaciones);
        return model;
    }
}
