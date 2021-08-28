package br.com.ot6.pix

import br.com.ot6.DeletePixKeyRequest
import br.com.ot6.PixKeymanagerDeleteServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable

@Controller(value = "api/v1/clientes/{clientId}")
class DeletePixKeyController(
    private val grpClient: PixKeymanagerDeleteServiceGrpc.PixKeymanagerDeleteServiceBlockingStub
) {

    @Delete("/pix/{pixId}")
    fun delete(
        @PathVariable clientId: String, @PathVariable pixId: String
    ): HttpResponse<Any> {
        grpClient.delete(
            DeletePixKeyRequest
                .newBuilder()
                .setClientId(clientId)
                .setPixId(pixId)
                .build()
        )

        return HttpResponse.ok()
    }
}