package br.com.edu.zup.carro

import com.sun.istack.NotNull
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Carro(placa:String,modelo:String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null
    @field:NotBlank
    @Column(nullable = false)
    val placa:String? = placa
    @field:NotBlank
    @Column(nullable = false)
    val modelo:String? = modelo


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Carro) return false

        if (id != other.id) return false
        if (placa != other.placa) return false
        if (modelo != other.modelo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (placa?.hashCode() ?: 0)
        result = 31 * result + (modelo?.hashCode() ?: 0)
        return result
    }


}