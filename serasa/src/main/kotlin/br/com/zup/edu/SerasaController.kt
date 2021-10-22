package br.com.zup.edu

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.validation.Validated
import jakarta.inject.Inject

@Validated
@Controller
class SerasaController(@Inject val grpcClient: SerasaGrpcServiceGrpc.SerasaGrpcServiceBlockingStub) {

    @Post("/api/serasa/clientes/verificar-situacao")
    fun verificar(cpf:String): HttpResponse<Any> {

        val request = SituacaoDoClienteRequest.newBuilder().setCpf(cpf).build()
        try{
            val response = grpcClient.verificarSituacaoDoCliente(request)
            val situacaoFromResponse = response.situacao.toString()
            val situacao = when{
                situacaoFromResponse == "REGULAR" -> Situacao.REGULARIZADA
                situacaoFromResponse == "IRREGULAR" ->Situacao.NAO_REGULARIZADA
                else -> Situacao.SEM_INFORMACOES
            }
            return HttpResponse.ok(SituacaoNoSerasaResponse(cpf, situacao))
        }catch (e:StatusRuntimeException){
            val status = e.status.code
            val message = e.message
            if(status == Status.Code.INVALID_ARGUMENT){
                throw HttpStatusException(HttpStatus.BAD_REQUEST,message)
            }
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR,e.message)
        }

    }
}

data class SituacaoNoSerasaResponse(
    val cpf: String,
    val situacao: Situacao
)

enum class Situacao {
    SEM_INFORMACOES,
    REGULARIZADA,
    NAO_REGULARIZADA
}