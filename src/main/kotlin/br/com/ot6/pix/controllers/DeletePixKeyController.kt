package br.com.ot6.pix.controllers

import br.com.ot6.DeletePixKeyRequest
import br.com.ot6.PixKeymanagerDeleteServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller(value = "api/v1/clientes/{clientId}")
class DeletePixKeyController(
    private val grpClient: PixKeymanagerDeleteServiceGrpc.PixKeymanagerDeleteServiceBlockingStub
) {

    @Delete("/pix/{pixId}")
    fun delete(
        @PathVariable clientId: UUID, @PathVariable pixId: UUID
    ): HttpResponse<Any> {
        grpClient.delete(
            DeletePixKeyRequest
                .newBuilder()
                .setClientId(clientId.toString())
                .setPixId(pixId.toString())
                .build()
        )

        return HttpResponse.ok()
    }
}