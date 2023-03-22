package com.example.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.entities.Estudiante;
import com.example.entities.Facultad;
import com.example.entities.Telefono;
import com.example.services.EstudianteService;
import com.example.services.FacultadService;
import com.example.services.TelefonoService;

@Controller
@RequestMapping("/") // El controlador en el patron MVC de Spring responde a una request concreta
// y la delega posteriormente en un metodo que tiene en cuenta el metodo
// utilizado
// Verbs: Accion mediante la cual llega la informacion viajando a traves del
// protocolo http: put, delete, option, get, post...
public class MainController {

    private static final Logger LOG = Logger.getLogger("MainController");
    @Autowired
    private EstudianteService estudianteService;
    @Autowired
    private FacultadService facultadService;
    @Autowired
    private TelefonoService telefonoService;

    /**
     * El metodo siguiente devuelve un listado de estudiantes
     * 
     */
    @GetMapping("/listar")
    public ModelAndView listar() {

        List<Estudiante> estudiantes = estudianteService.findAll();

        ModelAndView mav = new ModelAndView("views/listarEstudiantes");
        mav.addObject("estudiantes", estudiantes);
        return mav;
    }

    @GetMapping("/frm")
    public String formularioAltaEstudiante(Model model) {

        List<Facultad> facultades = facultadService.findAll();

        model.addAttribute("estudiante", new Estudiante());
        model.addAttribute("facultades", facultades);

        return "views/formularioAltaEstudiante";

    }

    @PostMapping("/altaEstudiante")
    public String altaEstudiante(@ModelAttribute Estudiante estudiante,
            @RequestParam(name = "numerosTelefonos") String telefonosRecibidos) {
        LOG.info("Telefonos recibidos:" + telefonosRecibidos);
        List<String> listadoNumerosTelefonos = null;
        if (telefonosRecibidos != null) {
            String[] arrayTelefonos = telefonosRecibidos.split(";");
            List<String> listadonumerosTelefono = Arrays.asList(arrayTelefonos);
        }

        estudianteService.save(estudiante);

        if (listadoNumerosTelefonos != null) {
            listadoNumerosTelefonos.stream().forEach(n -> {
                Telefono telefonoObject = Telefono.builder().telefono(n).build();
                
                telefonoService.save(telefonoObject); 
          
           
            });
        }

        return "redirect:/listar";
    }


    /** FOrmulario para actualizar  un estudiante */
@GetMapping("/frmActualizar/{id}")
    public String actualizaEstudiante(@PathVariable(name = "id") int idEstudiante) {
        return "redirect:/listar"; 
    }
}
