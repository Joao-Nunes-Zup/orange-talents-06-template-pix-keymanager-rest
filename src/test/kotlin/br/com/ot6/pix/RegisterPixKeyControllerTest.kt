package br.com.ot6.pix

import br.com.ot6.NewPixKeyResponse
import br.com.ot6.PixKeymanagerRegisterServiceGrpc
import br.com.ot6.pix.dtos.AccountTypeRequest
import br.com.ot6.pix.dtos.PixKeyTypeRequest
import br.com.ot6.pix.dtos.RegisterPixKeyRequest
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
internal class RegisterPixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: PixKeymanagerRegisterServiceGrpc.PixKeymanagerRegisterServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun `should register a new pix key`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        val grpcResponse = NewPixKeyResponse
                                .newBuilder()
                                .setClientId(clientId)
                                .setPixId(pixId)
                                .build()

        given(grpcClient.register(Mockito.any())).willReturn(grpcResponse)

        val newPixKey = RegisterPixKeyRequest(
            tipoDeChave = PixKeyTypeRequest.CPF,
            chave = "42306320020",
            tipoDeConta = AccountTypeRequest.CONTA_CORRENTE
        )

        val request = HttpRequest.POST("api/v1/clientes/$clientId/pix", newPixKey)
        val response = httpClient.toBlocking().exchange(request, RegisterPixKeyRequest::class.java)

        response.run {
            assertEquals(HttpStatus.CREATED, status)
            assertTrue(headers.contains("Location"))
            assertTrue(header("Location")!!.contains(pixId))
        }
    }

    @Factory
    @Replaces(factory = GrpcStubsFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(PixKeymanagerRegisterServiceGrpc.PixKeymanagerRegisterServiceBlockingStub::class.java)
    }
}