package br.com.ot6.pix

import br.com.ot6.PixKeymanagerRegisterServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import java.net.URI
import java.util.*
import javax.validation.Valid

@Validated
@Controller("api/v1/clientes/{clientId}")
class RegisterPixKeyController(
    private val grpcClient: PixKeymanagerRegisterServiceGrpc.PixKeymanagerRegisterServiceBlockingStub
) {

    @Post("/pix")
    fun register(
        @PathVariable clientId: UUID,
        @Body @Valid request: RegisterPixKeyRequest
    ): HttpResponse<Any> {
        val grpcResponse = grpcClient.register(request.toGrpcRequest(clientId))
        val uri = HttpResponse.uri("api/v1/clientes/$clientId/pix/${grpcResponse.pixId}")
        return HttpResponse.created(uri)
    }
}