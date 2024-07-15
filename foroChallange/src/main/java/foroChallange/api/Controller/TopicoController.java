package foroChallange.api.Controller;

import foroChallange.api.curso.Curso;
import foroChallange.api.curso.CursoRepository;
import foroChallange.api.topico.ListaTopico;
import foroChallange.api.topico.RegistrarTopico;
import foroChallange.api.topico.Topico;
import foroChallange.api.topico.TopicoRepository;
import foroChallange.api.usuario.Usuario;
import foroChallange.api.usuario.UsuarioRepository;
import foroChallange.api.validacion.Validacion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private Validacion validadorDeTopicos;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nuevo tópico",
            description = "Registra un nuevo tópico en la base de datos, vinculando a un usuario y un curso existente. " +
                    "Verifica que el título y mensaje del tópico sean únicos para evitar duplicados. " +
                    "Devuelve el tópico registrado con su identificador único y ubicación URI.",
            tags = { "Tópicos"})
    public ResponseEntity<?> registrarTopico(@RequestBody @Valid RegistrarTopico registrarTopico,
                                             UriComponentsBuilder uriComponentsBuilder) {
        Usuario autor = usuarioRepository.findById(registrarTopico.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Curso curso = cursoRepository.findById(registrarTopico.cursoId())
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        // Validar que el tópico no esté duplicado
        try {
            validadorDeTopicos.validarTituloYMensajeUnicos(registrarTopico.titulo(), registrarTopico.mensaje());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        Topico topico = new Topico(registrarTopico.titulo(), registrarTopico.mensaje(), autor, curso);

        try {
            topicoRepository.save(topico);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Error al guardar el tópico: " + e.getMessage());
        }

        DatosRespuestaTopico datosRespuestaTopico = new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        );
        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuestaTopico);
    }

    // Listar todos los tópicos activos
    @GetMapping
    @Operation(summary = "Listar todos los tópicos activos",
            description = "Devuelve una lista paginada de todos los tópicos activos en el sistema. " +
                    "Los resultados se ordenan por fecha de creación en orden descendente por defecto.",
            tags = { "Tópicos"})
    public ResponseEntity<Page<ListaTopico>> listadoTopicos(@PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(topicoRepository.findAllByStatusTrue(paginacion).map(ListaTopico::new));
    }

    // Listar tópicos filtrados por nombre de curso y año
    @GetMapping("/filtrar")
    @Operation(summary = "Listar tópicos filtrados por nombre de curso y año",
            description = "Devuelve una lista paginada de tópicos activos filtrados por el nombre del curso y el año de creación. " +
                    "Los resultados se ordenan por fecha de creación en orden ascendente.",
            tags = { "Tópicos"})
    public ResponseEntity<Page<ListaTopico>> listarPorCursoYYear(
            @RequestParam String cursoNombre,
            @RequestParam int year,
            @PageableDefault(size = 10) Pageable paginacion) {
        Page<ListaTopico> topicos = topicoRepository.findByCursoNombreAndYear(cursoNombre, year, paginacion).map(ListadoDeTopicos::new);
        return ResponseEntity.ok(topicos);
    }

    // Actualiza un tópico registrado
    @PutMapping
    @Transactional
    @Operation(summary = "Actualiza un tópico por ID",
            description = "Busca y actualiza los datos de un tópico existente en la base de datos según su id. " +
                    "Verifica que el título y mensaje del tópico actualizado sean únicos para evitar duplicados. " +
                    "Devuelve los datos actualizados del tópico con su id, título, mensaje, fecha de creación, " +
                    "ID del autor y ID del curso vinculado.",
            tags = { "Tópicos"})
    public ResponseEntity<?> actualizarTopico(@RequestBody @Valid ActualizarTopico actualizarTopico) {
        Topico topico = topicoRepository.findById(actualizarTopico.id())
                .orElseThrow(() -> new EntityNotFoundException("Tópico que intenta actualizar no existe"));

        // Validar que el tópico no esté duplicado
        try {
            validadorDeTopicos.validarTituloYMensajeUnicos(actualizarTopico.titulo(), actualizarTopico.mensaje());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        // Actualizar los datos del tópico
        topico.setTitulo(actualizarTopico.titulo());
        topico.setMensaje(actualizarTopico.mensaje());

        try {
            topicoRepository.save(topico);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Error al actualizar el tópico: " + e.getMessage());
        }

        return ResponseEntity.ok(new DatosRespuestaTopico(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        ));
    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Eliminar un tópico registrado",
            description = "Marca un tópico como inactivo sin eliminarlo físicamente de la base de datos.",
            tags = {"Tópicos"})
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        try {
            Topico topico = topicoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));
            topico.desactivarTopico();
            topicoRepository.save(topico); // Guarda el cambio de estado
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al inactivar el tópico");
        }
    }

    // Método para obtener los datos del tópico por ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene los datos del tópico por ID",
            description = "Devuelve los datos de un tópico específico dado su ID.",
            tags = { "Tópicos" })
    public ResponseEntity<?> retornaDatosTopico(@PathVariable Long id) {
        try {
            Topico topico = topicoRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Tópico no no existe"));
            DatosRespuestaTopico datosTopico = new DatosRespuestaTopico(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getMensaje(),
                    topico.getFechaCreacion(),
                    topico.getAutor().getId(),
                    topico.getCurso().getId());
            return ResponseEntity.ok(datosTopico);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}