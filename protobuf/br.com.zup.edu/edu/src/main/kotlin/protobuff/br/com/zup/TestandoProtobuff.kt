package protobuff.br.com.zup

import br.com.zup.edu.Cargo
import br.com.zup.edu.Endereco
import br.com.zup.edu.FuncionarioRequest
import java.io.FileInputStream
import java.io.FileOutputStream

fun main(args: Array<String>) {

    val request = FuncionarioRequest.newBuilder()
        .setName("Rafael Santiago")
        .setCpf("123.123.234.56")
        .setIdade(26)
        .setSalario(2500.00)
        .setCargo(Cargo.DEV)
        .setAtivo(true)
        .addEnderecos(Endereco.newBuilder()
            .setLogradouro("AntonioFortunato")
            .setBairro("Santa Monica")
            .setComplemento("Apto 102").build()
        ).build()
    println(request)
    request.writeTo(FileOutputStream("funcionario-request.bin"))

    val r2 = FuncionarioRequest.newBuilder().mergeFrom(FileInputStream("funcionario-request.bin"))

    println(r2)
}