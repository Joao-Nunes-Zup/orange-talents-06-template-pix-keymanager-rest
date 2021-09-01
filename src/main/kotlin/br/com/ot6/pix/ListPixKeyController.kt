package br.com.ot6.pix

import br.com.ot6.ListPixKeyRequest
import br.com.ot6.PixKeymanagerListServiceGrpc
import br.com.ot6.pix.dtos.PixKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller(value = "api/v1/clientes/{clientId}")
class ListPixKeyController(
    private val grpClient: PixKeymanagerListServiceGrpc.PixKeymanagerListServiceBlockingStub
) {

    @Get("/pix")
    fun list(@PathVariable clientId: UUID): HttpResponse<Any> {
        val grpcResponse = grpClient.list(
            ListPixKeyRequest.newBuilder().setClientId(clientId.toString()).build()
        )

        val keys = grpcResponse.keysList.map { PixKeyResponse(it) }

        return HttpResponse.ok(keys)
    }
}