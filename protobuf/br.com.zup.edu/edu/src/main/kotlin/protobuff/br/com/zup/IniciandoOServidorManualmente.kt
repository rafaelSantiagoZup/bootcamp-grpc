package protobuff.br.com.zup

import br.com.zup.edu.*
import com.google.protobuf.Timestamp
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.time.LocalDateTime
import java.time.ZoneId

fun main() {
    val server = ServerBuilder
        .forPort(50051)
        .addService(FuncionarioEndpoint())
        .build()
    server.start()
    server.awaitTermination()
}

class FuncionarioEndpoint:EduServiceGrpc.EduServiceImplBase() {
    override fun send(request: FuncionarioRequest?, responseObserver: StreamObserver<FuncionarioResponse>?) {
        println(request!!)

        val instant = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        val criadoEm = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
        var nome = request?.name
        if(!request.hasField(FuncionarioRequest.getDescriptor().findFieldByName("name"))){
            nome = "[???]"
        }
        val response = FuncionarioResponse.newBuilder()
            .setName(nome)
            .setCriadoEm(criadoEm)
            .build()

        println(response)

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}
