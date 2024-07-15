package foroChallange.api.topico;


import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;

public interface TopicoRepository extends JpaRepository<Topico, Long> {

    Page<Topico> findAll(Pageable paginacion);

    Page<Topico> findAllByStatusTrue(Pageable paginacion);

    @Query("SELECT t FROM Topico t WHERE t.status = true AND t.curso.nombre = :cursoNombre AND YEAR(t.fechaCreacion) = :year ORDER BY t.fechaCreacion ASC")
    Page<Topico> findByCursoNombreAndYear(String cursoNombre, int year, Pageable paginacion);


//    @Query("SELECT t FROM Topico t WHERE t.curso.nombre = :cursoNombre AND t.fechaCreacion BETWEEN :startDate AND :endDate ORDER BY t.fechaCreacion ASC")
//    Page<Topico> findByCursoNombreAndFechaCreacionBetween(String cursoNombre, LocalDateTime startDate, LocalDate endDate, Pageable paginacion);
//

    @Query("SELECT t FROM Topico t WHERE (t.titulo LIKE %:keyword% OR t.mensaje LIKE %:keyword%) ORDER BY t.fechaCreacion ASC")
    Page<Topico> findByKeywordInTituloOrMensaje(String keyword, Pageable paginacion);


//    @Query("SELECT t FROM Topico t WHERE t.curso.id = :cursoId AND size(t.respuestas) >= :numRespuestas ORDER BY t.fechaCreacion ASC")
//    Page<Topico> findByCursoIdAndNumeroRespuestasGreaterThanEqual(Long cursoId, int numRespuestas, Pageable paginacion);
//

    boolean existsByTituloAndMensaje(String titulo, String mensaje);

}
