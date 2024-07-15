package foroChallange.api.respuesta;

import java.time.LocalDateTime;
import java.util.Date;

public record DatosRegistroRespuesta(Long id, String mensaje, LocalDateTime fechaCreacion) {
}
