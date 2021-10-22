package protobuff.br.com.zup

import br.com.zup.edu.Cargo
import br.com.zup.edu.EduServiceGrpc
import br.com.zup.edu.Endereco
import br.com.zup.edu.FuncionarioRequest
import io.grpc.ManagedChannelBuilder

fun main() {
    val chanel = ManagedChannelBuilder
        .forAddress("localhost",50051)
        .usePlaintext()
        .build()
    val client = EduServiceGrpc.newBlockingStub(chanel)

    val request = FuncionarioRequest.newBuilder()
        .setName("Rafael Santiago")
        .setCpf("123.123.234.56")
        .setIdade(26)
        .setSalario(2500.00)
        .setCargo(Cargo.DEV)
        .setAtivo(true)
        .addEnderecos(
            Endereco.newBuilder()
            .setLogradouro("AntonioFortunato")
            .setBairro("Santa Monica")
            .setComplemento("Apto 102").build()
        ).build()

    val response = client.send(request)

    println(response)
}