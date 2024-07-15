package foroChallange.api.curso;

public record ListaCursos(Long id,
                          String nombre,
                          String categoria) {
    public ListaCursos(Curso curso){
        this(curso.getId(), curso.getNombre(), curso.getCategoria());
    }
}
