package foroChallange.api.topico;

import foroChallange.api.curso.DatosRegistroCurso;
import foroChallange.api.usuario.DatosRegistroUsuario;

import java.time.LocalDateTime;

public record DatosRegistroTopico(Long id,
                                  String titulo,
                                  String mensaje,
                                  LocalDateTime fechaCreacion,
                                  Long autorId,
                                  Long cursoId) {
}
