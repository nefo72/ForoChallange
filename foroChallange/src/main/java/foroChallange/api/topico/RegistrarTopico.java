package foroChallange.api.topico;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarTopico(@NotNull @Size(min = 1, max = 255) String titulo,
                              @NotNull @Size(min = 1, max = 1000) String mensaje,
                              @NotNull Long autorId,
                              @NotNull Long cursoId) {
}
