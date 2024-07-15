package foroChallange.api.validacion;



import foroChallange.api.topico.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Validacion {

    @Autowired
    private TopicoRepository topicoRepository;

    public void validarTituloYMensajeUnicos(String titulo, String mensaje) {
        if (topicoRepository.existsByTituloAndMensaje(titulo, mensaje)) {
            throw new IllegalArgumentException("Tópico con el mismo título y mensaje ya existe");
        }
    }
}
