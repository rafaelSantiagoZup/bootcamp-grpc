package br.com.edu.zup

import br.com.edu.zup.carro.Carro
import br.com.edu.zup.carro.CarroRepository
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest

import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.Assert.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.validation.ConstraintViolationException


@MicronautTest(transactional = false)
class CarrosEndpointTest{
    @Inject
    lateinit var grpcClient: CarrosGrpcServiceGrpc.CarrosGrpcServiceBlockingStub
    @Inject
    lateinit var repository: CarroRepository

    @Test
    internal fun `deve adicionar novo carro`(){
        repository.deleteAll()

        val response = grpcClient.cadastrarCarro(CarroRequest.newBuilder()
                                                    .setModelo("Fiesta")
                                                    .setPlaca("HAJ-1990")
                                                    .build())
        with(response){
            assertNotNull(id)
            assertTrue(repository.existsById(id))
        }
    }
    @Test
    internal fun `nao deve adicionar carro com placa repetida`(){
        val existente = repository.save(Carro("HAJ-1990","Fiesta"))
            val error = assertThrows<StatusRuntimeException>{
            grpcClient.cadastrarCarro(CarroRequest.newBuilder()
            .setModelo("Fiesta")
            .setPlaca("HAJ-1990")
            .build())
        }
        with(error){
            assertEquals(Status.ALREADY_EXISTS.code,status.code)
        }
    }
    @Test
    internal fun `nao deve adicionar dados quando os valores forem invalidos`(){
        repository.deleteAll()

        val error = assertThrows<StatusRuntimeException>{
            grpcClient.cadastrarCarro(CarroRequest.newBuilder()
            .build())
        }
        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code,status.code)
            assertEquals("dados inválidos",status.description)
        }
    }

    @Factory
    class Clients{
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel):CarrosGrpcServiceGrpc.CarrosGrpcServiceBlockingStub{
            return CarrosGrpcServiceGrpc.newBlockingStub(channel)
        }
    }
}