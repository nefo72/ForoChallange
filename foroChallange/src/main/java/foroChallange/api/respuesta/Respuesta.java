package foroChallange.api.respuesta;


import foroChallange.api.topico.Topico;
import foroChallange.api.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name= "respuestas")
@Entity(name = "Respuesta")

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensaje;

    private Boolean solucion;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @Column(name = "fecha_Creacion")
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    public Respuesta(NuevaRespuesta nuevaRespuesta) {
        this.mensaje = nuevaRespuesta.mensaje();
        this.topico = nuevaRespuesta.topico();
        this.fechaCreacion = LocalDateTime.now();
        this.autor = nuevaRespuesta.usuario();
        this.solucion = true;
    }

    public void actualizarDeRespuesta(ActualizarRespuesta actualizarRespuesta) {
        if (actualizarRespuesta.mensaje() != null) {
            this.mensaje = actualizarRespuesta.mensaje();
        }
    }

    public void desactivarTopico() {
        this.solucion = false;
    }


}
