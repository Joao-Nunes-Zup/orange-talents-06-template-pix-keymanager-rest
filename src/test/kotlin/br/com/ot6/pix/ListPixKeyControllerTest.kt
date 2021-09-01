package br.com.ot6.pix

import br.com.ot6.*
import br.com.ot6.pix.shared.grpc.GrpcStubsFactory
import com.google.protobuf.Timestamp
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
import org.mockito.BDDMockito
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@MicronautTest
internal class ListPixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: PixKeymanagerListServiceGrpc.PixKeymanagerListServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    val EMAIL_KEY = "zoiodinho@damamae.com"
    val CPF_KEY = "71861834098"
    val EMAIL_KEY_TYPE = KeyType.EMAIL
    val CPF_KEY_TYPE = KeyType.CPF
    val CONTA_CORRENTE = AccountType.CONTA_CORRENTE
    val KEY_CREATION_DATE = LocalDateTime.now()

    @Test
    fun `should list pix keys`() {
        val clientId = UUID.randomUUID().toString()

        BDDMockito.given(grpcClient.list(Mockito.any()))
            .willReturn(grpcPixKeyListResponse(clientId))

        val request = HttpRequest.GET<Any>("api/v1/clientes/$clientId/pix")
        val response = httpClient.toBlocking().exchange(request, List::class.java)

        response.run {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(body()!!.size, 2)
        }
    }

    @Factory
    @Replaces(factory = GrpcStubsFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(PixKeymanagerListServiceGrpc.PixKeymanagerListServiceBlockingStub::class.java)
    }

    fun grpcPixKeyListResponse(clientId: String): ListPixKeyResponse {
        val cpfKey = ListPixKeyResponse.PixKey
            .newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setType(CPF_KEY_TYPE)
            .setKey(CPF_KEY)
            .setAccountType(CONTA_CORRENTE)
            .setCreatedAt(
                KEY_CREATION_DATE.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                }
            )
            .build()

        val emailKey = ListPixKeyResponse.PixKey
            .newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setType(EMAIL_KEY_TYPE)
            .setKey(EMAIL_KEY)
            .setAccountType(CONTA_CORRENTE)
            .setCreatedAt(
                KEY_CREATION_DATE.let {
                    val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                    Timestamp.newBuilder()
                        .setSeconds(createdAt.epochSecond)
                        .setNanos(createdAt.nano)
                        .build()
                }
            )
            .build()

        return ListPixKeyResponse
            .newBuilder()
            .setClientId(clientId)
            .addAllKeys(listOf(cpfKey, emailKey))
            .build()
    }
}