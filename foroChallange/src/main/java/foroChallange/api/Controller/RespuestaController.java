package foroChallange.api.Controller;

import foroChallange.api.curso.DatosRegistroCurso;
import foroChallange.api.respuesta.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    //Registrar respuesta
    @PostMapping
    @Transactional
    @Operation(summary = "Registra un nueva respuesta",
            description = "",
            tags = { "Respuestas"})
    public ResponseEntity<DatosRegistroRespuesta> nuevaRespuesta(@RequestBody @Valid NuevaRespuesta nuevaRespuesta,
                                                                 UriComponentsBuilder uriComponentsBuilder){
        Respuesta respuesta = respuestaRepository.save(new Respuesta(nuevaRespuesta));
        DatosRegistroRespuesta datosRespuesta = new DatosRegistroRespuesta(respuesta.getId(), respuesta.getMensaje(),
                respuesta.getFechaCreacion());
        URI url = uriComponentsBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(url).body(datosRespuesta);
    }

    //Listar respuestas
    @GetMapping
    @Operation(summary = "Listar todas las respuestas activas",
            description = "",
            tags = { "Respuestas"})
    public ResponseEntity<Page<ListaRespuesta>> listadoDeRespuestas(@PageableDefault(size = 10) Pageable paginacion){
        //return ResponseEntity.ok(respuestaRepository.findAll(paginacion).map(ListadoDeRespuestas::new)); //Listar todos los registros
        return ResponseEntity.ok(respuestaRepository.findBySolucionTrue(paginacion).map(ListaRespuesta::new)); //Lista solo los registros activos
    }

    //Actualiza respuesta
    @PutMapping
    @Transactional
    @Operation(summary = "Actualiza una respuesta",
            description = "",
            tags = { "Respuestas"})
    public ResponseEntity actualizarRespuesta(@RequestBody @Valid ActualizarRespuesta actualizarRespuesta){
        Respuesta respuesta = respuestaRepository.getReferenceById(actualizarRespuesta.id());
        respuesta.actualizarDeRespuesta(actualizarRespuesta);
        return ResponseEntity.ok(new DatosRegistroRespuesta(respuesta.getId(), respuesta.getMensaje(), respuesta.getFechaCreacion()));

    }

    // DELETE LOGICO
    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Elimina una respuesta - inactivo",
            description = "",
            tags = { "Respuestas"})
    public ResponseEntity eliminarRespuesta(@PathVariable Long id) {
        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        respuesta.desactivarTopico();
        return ResponseEntity.noContent().build();
    }
}