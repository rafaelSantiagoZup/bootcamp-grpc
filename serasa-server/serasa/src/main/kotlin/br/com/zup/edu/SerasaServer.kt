package br.com.zup.edu

import com.google.protobuf.Any
import com.google.rpc.Code
import com.google.rpc.Status
import io.grpc.protobuf.StatusProto
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import io.grpc.Status as Status1

@Singleton
class SerasaServer : SerasaGrpcServiceGrpc.SerasaGrpcServiceImplBase(){
    private val logger =  LoggerFactory.getLogger(this::class.java)

    override fun verificarSituacaoDoCliente(
        request: SituacaoDoClienteRequest?,
        responseObserver: StreamObserver<SituacaoDoClienteResponse>?
    ) {
        logger.info("Checando status do cliente:${request}")

        val cpf = request!!.cpf

        println(cpf.endsWith("12"))

        if(request?.cpf == null || request?.cpf == ""){
            val e = Status1.INVALID_ARGUMENT
                .withDescription("O cpf não deve ser nulo")
                .asRuntimeException()
            responseObserver?.onError(e)
        }
        if(!request!!.cpf.matches("[0-9]{3}.[0-9]{3}.[0-9]{3}-[0-9]{2}".toRegex())){
            val e = Status1.INVALID_ARGUMENT
                .withDescription("O cpf é inválido")
                .augmentDescription("O formato esperado é: ___.___.___-__")
                .asRuntimeException()
            responseObserver?.onError(e)
        }
        if(cpf.endsWith("12")){
            val statusProto = Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED_VALUE)
                .setMessage("usuário não pode acessar esse recurso")
                .addDetails(Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("Token Expirado")
                    .build()))
                .build()
            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)
        }
        val response = SituacaoDoClienteResponse.newBuilder()
            .setSituacao(SituacaoDoClienteResponse.Situacao.REGULAR)
            .build()
        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}
