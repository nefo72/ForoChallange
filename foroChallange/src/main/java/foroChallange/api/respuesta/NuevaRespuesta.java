package foroChallange.api.respuesta;

import foroChallange.api.topico.Topico;
import foroChallange.api.usuario.Usuario;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record NuevaRespuesta(@NotNull
                             String mensaje,
                             Topico topico,
                             LocalDateTime fechaCreacion,
                             Usuario usuario,
                             String solucion) {
}
