package com.example.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        Estudiante estudiante = new Estudiante();

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("facultades", facultades);

        return "views/formularioAltaEstudiante";

    }

    @PostMapping("/altaModificacionEstudiante")
    public String altaModificacionEstudiante(@ModelAttribute Estudiante estudiante,
            @RequestParam(name = "numerosTelefonos") String telefonosRecibidos) {
        LOG.info("Telefonos recibidos:" + telefonosRecibidos);

        estudianteService.save(estudiante);

        /**
         * if(estudiante.getId() !=0) {
         * 
         * estudianteService.deleteById(estudiante.getId());
         * }
         * List<String> listadoNumerosTelefonos = null;
         * if (telefonosRecibidos != null) {
         * String[] arrayTelefonos = telefonosRecibidos.split(";");
         * List<String> listadonumerosTelefono = Arrays.asList(arrayTelefonos);
         * }
         **/
        estudianteService.save(estudiante);
        List<String> listadoNumerosTelefonos = null;

        if (telefonosRecibidos != null) {
            String[] arrayTelefonos = telefonosRecibidos.split(";");
            listadoNumerosTelefonos = Arrays.asList(arrayTelefonos);
        }

        // Borrar todos los telefonos que tenga el estudiante si hay que insertar nuevos

        if (listadoNumerosTelefonos != null) {
            listadoNumerosTelefonos.stream().forEach(n -> {
                Telefono telefonoObject = Telefono.builder().telefono(n).estudiante(estudiante).build();

                telefonoService.save(telefonoObject);

            });
        }

        return "redirect:/listar";
    }

    /** FOrmulario para actualizar un estudiante */
    @GetMapping("/frmActualizar/{id}")
    public String frmActualizarEstudiante(@PathVariable(name = "id") int idEstudiante, Model model) {
        Estudiante estudiante = estudianteService.findById(idEstudiante);
        List<Telefono> todosTelefonos = telefonoService.findAll();
        List<Telefono> telefonosDelEstudiante = todosTelefonos.stream()
                .filter(telefono -> telefono.getEstudiante().getId() == idEstudiante).collect(Collectors.toList());
        List<Facultad> facultades = facultadService.findAll();
        String numerosDeTelefono = telefonosDelEstudiante.stream().map(telefono -> telefono.getTelefono())
                .collect(Collectors.joining(";"));

        model.addAttribute("estudiante", estudiante);
        model.addAttribute("telefonos", numerosDeTelefono);
        model.addAttribute("facultades", facultades);

        return "views/formularioAltaEstudiante";
    }

    @GetMapping("/borrar/{id}")
    public String borrarEstudiante(@PathVariable(name = "id") int idEstudiante) {
        estudianteService.delete(estudianteService.findById(idEstudiante));

        return "redirect:/listar";
    }

    @GetMapping("/detalles/{id}")
    public ModelAndView detalles(@PathVariable(name = "id") int id) {

        Estudiante estudiante = estudianteService.findById(id);
        List<Telefono> telefonosEstudiante = telefonoService.findByEstudiante(estudiante);
        List<String> numerosEstudiante = telefonosEstudiante.stream()
                .map(t -> t.getTelefono())
                .toList();
        ModelAndView mav = new ModelAndView("views/detailsEstudiante");

        mav.addObject("telefonos", numerosEstudiante);
        mav.addObject("estudiante", estudiante);

        return mav;
    }

}
