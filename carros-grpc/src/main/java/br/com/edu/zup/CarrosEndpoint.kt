package br.com.edu.zup

import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory


import javax.validation.ConstraintViolationException

@Singleton
class CarrosEndpoint(@Inject val repository:CarroRepository):CarrosGrpcServiceGrpc.CarrosGrpcServiceImplBase() {
    private val logger =  LoggerFactory.getLogger(this::class.java)

    override fun adicionar(request: CarroRequest?, responseObserver: StreamObserver<CarroResponse>?) {
        if(repository.existsByPlaca(request!!.placa)){
            responseObserver?.onError(Status.ALREADY_EXISTS.
            withDescription("Já existe um cadastro dessa placa").
            asRuntimeException())
            return
        }
        val carro:Carro = Carro(request.placa,request.modelo)
        try {
            repository.save(carro)
        }catch (e:ConstraintViolationException){
            responseObserver?.onError(Status.INVALID_ARGUMENT.
            withDescription("dados de entrada inválidos").
            asRuntimeException())
            return
        }
        responseObserver?.onNext(CarroResponse.newBuilder().setId(carro.id!!).build())
        responseObserver?.onCompleted()
    }
}