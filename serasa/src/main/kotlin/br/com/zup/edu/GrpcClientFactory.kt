package br.com.zup.edu

import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcClientFactory {
    @Singleton
    fun serasaClientStub(@GrpcChannel("serasa") channel: ManagedChannel):SerasaGrpcServiceGrpc.SerasaGrpcServiceBlockingStub{
        return SerasaGrpcServiceGrpc
            .newBlockingStub(channel)
    }
}