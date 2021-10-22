package br.com.edu.zup.carro

import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface CarroRepository:CrudRepository<Carro,Long>{
    fun existsByPlaca(placa:String):Boolean
}