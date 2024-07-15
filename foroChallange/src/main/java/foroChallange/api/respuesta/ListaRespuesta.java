package foroChallange.api.respuesta;

import java.time.LocalDateTime;

public record ListaRespuesta(Long id,
                             String mensaje,
                             LocalDateTime fechaCreacion) {

    public ListaRespuesta(Respuesta respuesta){
        this(respuesta.getId(), respuesta.getMensaje(), respuesta.getFechaCreacion());
    }


}
