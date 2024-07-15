package foroChallange.api.curso;

import foroChallange.api.respuesta.DatosRegistroRespuesta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name= "cursos")
@Entity(name = "Curso")

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String categoria;
    private Boolean estado;


    public Curso(NuevoCurso nuevoCurso) {
        this.nombre = nuevoCurso.nombre();
        this.categoria = nuevoCurso.categoria();
        this.estado = true; // Inicializa el status como activo
    }

    public void actualizarCurso(ActualizarCurso actualizarCurso) {
        if (actualizarCurso.nombre() != null) {
            this.nombre = actualizarCurso.nombre();
        }
        if (actualizarCurso.categoria() != null) {
            this.categoria = actualizarCurso.categoria();
        }
    }

    public void desactivarCurso() {
        this.estado = false;
    }


}
