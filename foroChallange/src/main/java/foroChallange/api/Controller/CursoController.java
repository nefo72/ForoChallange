package foroChallange.api.Controller;

import foroChallange.api.curso.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    //Registrar curso
    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo curso",
            description = "Crea y registra un nuevo curso en la base de datos. Todos los campos son obligatorios.",
            tags = { "Cursos"})
    public ResponseEntity<DatosRegistroCurso> nuevoCurso(@RequestBody @Valid NuevoCurso nuevoCurso,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        Curso curso = cursoRepository.save(new Curso(nuevoCurso));
        DatosRegistroCurso datosCurso = new DatosRegistroCurso(curso.getId(), curso.getNombre(), curso.getCategoria());
        URI url = uriComponentsBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
        return ResponseEntity.created(url).body(datosCurso);
    }

    //Listar cursos
    @GetMapping
    @Operation(summary = "Listar todos los cursos",
            description = "Obtiene una lista paginada de todos los cursos registrados.",
            tags = { "Cursos"})
    public ResponseEntity<Page<ListaCursos>> listadoDeCursos(@PageableDefault(size = 10) Pageable paginacion) {
        var page = cursoRepository.findByStatusTrue(paginacion).map(ListaCursos::new);
        return ResponseEntity.ok(page);
    }

    //Actualizar curso
    @PutMapping
    @Transactional
    @Operation(summary = "Actualiza un curso",
            description = "Actualiza los datos de un curso existente identificado por su ID.",
            tags = { "Cursos"})
    public ResponseEntity<?> actualizarCurso(@RequestBody @Valid ActualizarCurso actualizarCurso) {
        try {
            Curso curso = cursoRepository.findById(actualizarCurso.id())
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
            curso.actualizarCurso(actualizarCurso);
            return ResponseEntity.ok(new DatosRegistroCurso(curso.getId(), curso.getNombre(), curso.getCategoria()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminar un curso registrado",
            description = "Inactiva un curso existente identificado por su ID. El curso no se elimina físicamente de la base de datos.",
            tags = {"Cursos"})
    public ResponseEntity<?> eliminarCurso(@PathVariable Long id) {
        try {
            Curso curso = cursoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));
            curso.desactivarCurso();
            cursoRepository.save(curso); // Guarda el cambio de estado
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al inactivar el curso");
        }
    }
}