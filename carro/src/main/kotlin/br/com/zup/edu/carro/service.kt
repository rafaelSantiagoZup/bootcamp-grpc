package br.com.zup.edu.carro

import br.com.zup.edu.CarroReply
import br.com.zup.edu.CarroRequest
import br.com.zup.edu.CarroServiceGrpc
import com.google.protobuf.Timestamp
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import java.time.LocalDateTime
import java.time.ZoneId

var id:Long = 1L

fun main() {
    val server = ServerBuilder
        .forPort(50051)
        .addService(CarroEndpoint())
        .build()
    server.start()
    server.awaitTermination()
}

class CarroEndpoint:CarroServiceGrpc.CarroServiceImplBase() {
    override fun send(request: CarroRequest?, responseObserver: StreamObserver<CarroReply>?) {
        println(request)
        val instant = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        val criadoEm = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()
        val response =CarroReply
            .newBuilder()
            .setDataCriacao(criadoEm)
            .setId(id)
            .build()
        id++
        println(response)

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }
}
