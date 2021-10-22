package br.com.edu.zup.carro

import br.com.edu.zup.CarroRequest
import br.com.edu.zup.CarroResponse
import br.com.edu.zup.CarrosGrpcServiceGrpc
import io.grpc.Status
import io.grpc.stub.StreamObserver
import jakarta.inject.Inject
import jakarta.inject.Singleton


import javax.validation.ConstraintViolationException

var id:Long = 1
@Singleton
class CarrosEndpoint(@Inject val repository: CarroRepository):CarrosGrpcServiceGrpc.CarrosGrpcServiceImplBase() {
    override fun cadastrarCarro(request: CarroRequest?, responseObserver: StreamObserver<CarroResponse>?) {
        if(repository.existsByPlaca(request!!.placa)){
            responseObserver!!.onError(Status.ALREADY_EXISTS.withDescription("carro cadastrado").asRuntimeException())
            return
        }
        val carro:Carro = Carro(placa = request.placa,modelo = request.modelo)
        try{
            repository.save(carro)
        }catch (e:ConstraintViolationException){
            responseObserver!!.onError(Status.INVALID_ARGUMENT.withDescription("dados inválidos").asRuntimeException())
            return
        }
        responseObserver!!.onNext(CarroResponse.newBuilder().setId(carro.id!!).build())
        responseObserver.onCompleted()
        id++
    }

}