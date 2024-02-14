package es.codeurjc.hellowordvscode;

import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import es.codeurjc.dataClasses.*;
import es.codeurjc.hellowordvscode.DataBase;
@Controller
public class AppRouter {

    @Autowired
	private UserService userService;

    @GetMapping("/")
    public String initial(Model model) {

        System.out.println("LOG DEL ROUTER INICIAL");  
        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("pool",pool);
        model.addAttribute("nMessages",pool.messages.size());
        return "pool";
    }

    @GetMapping("/pool")
    public String pool(Model model) {
        DataBase db = new DataBase();
        pool pool = db.getPool(0);
        model.addAttribute("pool",pool);
        return "pool";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        DataBase db = new DataBase();
        Person pers = db.getPerson(0);
        model.addAttribute("person",pers);
        return "profile";
    }
    @GetMapping("/pagePartPoolMsg")
    public String ppPoolMsg(@RequestParam("id") int id ,Model model) {
        System.out.println("PETICION DE PP POOL RECIBIDA");
        DataBase db = new DataBase();
        Message m = db.getMessage(id);
        model.addAttribute("message",m);
        model.addAttribute("id",id);
        return "poolMessege";
    }
    
    @PostMapping("/newUser")
	public String newPost(Model model, Lifeguard lifeguard, Employer employer, String typeUser, boolean reliability, boolean effort, boolean communication, boolean attitude, boolean problemsResolution, boolean leadership) throws IOException {
        if ("employer".equals(typeUser)){
			userService.saveEmployer(employer);
            model.addAttribute("message", "Nuevo empleado creado correctamente");
		}else if ("lifeguard".equals(typeUser)){
			userService.saveLifeguard(lifeguard);
			List<String> skills = new ArrayList<>();
			if (reliability){
				skills.add("Confianza");
			}
			if (effort){
				skills.add("Esfuerzo");
			}
			if(communication){
				skills.add("Comunicación");
			}
            if(attitude){
                skills.add("Actitud positiva");
            }
            if(problemsResolution){
                skills.add("Resolución de problemas");
            }
            if(leadership){
                skills.add("Liderazgo");
            }
			lifeguard.setSkills(skills);
			model.addAttribute("message", "Nuevo socorrista creado correctamente");
		}
		
		return "savedUser";
	}

}
