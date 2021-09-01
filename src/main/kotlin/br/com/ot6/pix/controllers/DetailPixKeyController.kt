package br.com.ot6.pix.controllers

import br.com.ot6.DetailPixKeyRequest
import br.com.ot6.PixKeymanagerDetailServiceGrpc
import br.com.ot6.pix.dtos.PixKeyDetailsResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import java.util.*

@Controller(value = "api/v1/clientes/{clientId}")
class DetailPixKeyController(
    private val grpClient: PixKeymanagerDetailServiceGrpc.PixKeymanagerDetailServiceBlockingStub
) {

    @Get("/pix/{pixId}")
    fun detail(
        @PathVariable clientId: UUID, @PathVariable pixId: UUID
    ): HttpResponse<Any> {
        val grpcResponse = grpClient.detail(
            DetailPixKeyRequest
                .newBuilder()
                .setPixId(
                    DetailPixKeyRequest.FilterByPixId
                        .newBuilder()
                        .setClientId(clientId.toString())
                        .setPixId(pixId.toString())
                        .build()
                )
                .build()
        )

        return HttpResponse.ok(PixKeyDetailsResponse(grpcResponse))
    }
}