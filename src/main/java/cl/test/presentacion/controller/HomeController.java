package cl.test.presentacion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cl.test.presentacion.service.IEjemploService;

import java.util.Date;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class); 
     
    @Autowired
    private IEjemploService ejemploService;

    @Value("${test.scheduled}")
    private String str;

    @Value("${test.hola}")
    private String salute;

    @Value("${test.externo}")
    private String externo;

    @Value("${jwt-service.endpoint}") // la idea es tener este valor en el properties externo.
    private String hola;


    @RequestMapping("/")
    public String welcome(Model model)  { 
        
        if (LOGGER.isDebugEnabled()) {
            LOGGER.info("1perfilUsuario=" + model );  
        }
        
        LOGGER.info("hola mundo/...1 - " +  new Date() );
        
        LOGGER.info("1perfilUsuario=" + model);  
        
        model.addAttribute("greeting", "Hola " + salute );
        model.addAttribute("tagline", "springmvc... " + str);
        model.addAttribute("meves", "me ves? port = " + externo);
        model.addAttribute("hola", "weblogic.Name: " + System.getProperty("weblogic.Name"));

        
        return "welcome";
    }

    
    
    
    @RequestMapping("/async")
    public String asynch(Model model) throws Exception  {

        LOGGER.info("async...");
        System.out.println("async...");
        
        model.addAttribute("greeting", "3" );
        model.addAttribute("tagline", "14");
        model.addAttribute("meves", "15");
        model.addAttribute("hola", "hola: " + hola);


        //test asynch:
        String respuesta = ejemploService.callback();
        System.out.println(respuesta);

        
        
        return "welcome";
    }
    
    

}

