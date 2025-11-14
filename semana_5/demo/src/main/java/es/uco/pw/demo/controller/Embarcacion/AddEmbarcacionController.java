package es.uco.pw.demo.controller.Embarcacion;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AddEmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;

    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    // ------------------ Añadir embarcación ------------------
    @GetMapping("/addEmbarcacion")
    public String addEmbarcacion(
            @RequestParam("matricula") String matricula,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipo") String tipo,
            @RequestParam("numPlazas") int numPlazas,
            @RequestParam("dimensiones") String dimensiones,
            @RequestParam(value = "dniPatron", required = false) String dniPatron,
            Model model
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

        if (success) {
            model.addAttribute("embarcacion", embarcacion);
            return "embarcacion/addEmbarcacionViewSuccess";
        } else {
            return "embarcacion/addEmbarcacionViewFail";
        }
    }

    // ------------------ Buscar por matrícula ------------------
    @GetMapping("/findEmbarcacionByMatricula")
    public String findEmbarcacionByMatricula(
            @RequestParam(value = "matricula", required = false) String matricula,
            Model model
    ) {
        if (matricula != null && !matricula.isEmpty()) {
            Embarcacion embarcacion = embarcacionRepository.findEmbarcacionByMatricula(matricula);
            model.addAttribute("embarcacion", embarcacion);
        }
        return "embarcacion/findEmbarcacionByMatricula";
    }

    // ------------------ Buscar por tipo ------------------
    @GetMapping("/findEmbarcacionesByTipo")
    public String findEmbarcacionesByTipo(
            @RequestParam(value = "tipo", required = false) String tipo,
            Model model
    ) {
        if (tipo != null && !tipo.isEmpty()) {
            List<Embarcacion> embarcaciones = embarcacionRepository.findEmbarcacionesByTipo(TipoEmbarcacion.valueOf(tipo.toUpperCase()));
            model.addAttribute("embarcaciones", embarcaciones);
        }
        return "embarcacion/findEmbarcacionesByTipo";
    }
}
