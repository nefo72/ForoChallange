package foroChallange.api.curso;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface CursoRepository extends JpaRepository<Curso,Long> {
    //Page<Curso> findAll(Pageable paginacion);
    Page<Curso> findByStatusTrue(Pageable paginacion);

}
