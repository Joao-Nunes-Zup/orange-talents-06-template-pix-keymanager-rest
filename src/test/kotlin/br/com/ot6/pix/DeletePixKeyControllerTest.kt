package br.com.ot6.pix

import br.com.ot6.DeletePixKeyResponse
import br.com.ot6.PixKeymanagerDeleteServiceGrpc
import br.com.ot6.PixKeymanagerRegisterServiceGrpc
import br.com.ot6.pix.shared.grpc.GrpcStubsFactory
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.util.*

@MicronautTest
internal class DeletePixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: PixKeymanagerDeleteServiceGrpc.PixKeymanagerDeleteServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should delete a pix key`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = DeletePixKeyResponse
                                .newBuilder()
                                .setClientId(clientId)
                                .setPixId(pixId)
                                .build()

        given(grpcClient.delete(Mockito.any())).willReturn(grpcResponse)

        val request = HttpRequest.DELETE<Any>("api/v1/clientes/$clientId/pix/$pixId",)
        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        assertEquals(HttpStatus.OK, response.status)
    }

    @Factory
    @Replaces(factory = GrpcStubsFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(PixKeymanagerDeleteServiceGrpc.PixKeymanagerDeleteServiceBlockingStub::class.java)
    }
}