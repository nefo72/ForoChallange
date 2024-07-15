package foroChallange.api.topico;

import java.time.LocalDateTime;

public record ListaTopico(Long id,
                          String titulo,
                          String mensaje,
                          LocalDateTime fechaCreacion,
                          Long autorId,
                          Long cursoId,
                          String cursoNombre) {


    public ListaTopico(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                Long.valueOf(topico.getAutor().getId()),
                topico.getCurso().getId(),
                topico.getCurso().getNombre());
    }


}
