package foroChallange.api.Controller;



import foroChallange.api.perfil.Perfil;
import foroChallange.api.perfil.PerfilRepository;
import foroChallange.api.perfil.DatosRegistroPerfil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;

    @PostMapping
    public void registrarPerfil(@RequestBody DatosRegistroPerfil datosRegistroPerfil){
        perfilRepository.save(new Perfil(datosRegistroPerfil));
    }
}
