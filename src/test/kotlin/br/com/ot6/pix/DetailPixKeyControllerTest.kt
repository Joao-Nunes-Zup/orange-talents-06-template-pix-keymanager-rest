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
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@MicronautTest
internal class DetailPixKeyControllerTest {

    @field:Inject
    lateinit var grpcClient: PixKeymanagerDetailServiceGrpc.PixKeymanagerDetailServiceBlockingStub

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    val CPF_KEY = "71861834098"
    val CPF_KEY_TYPE = KeyType.CPF
    val CONTA_CORRENTE = AccountType.CONTA_CORRENTE
    val INSTITUICAO = "ITAÚ UNIBANCO S.A."
    val OWNER_NAME = "Mike Wazowski"
    val OWNER_CPF = "71861834098"
    val AGENCY = "0001"
    val ACCOUNT_NUMBER = "111111"
    val KEY_CREATION_DATE = LocalDateTime.now()

    @Test
    fun `should detail a pix key`() {
        val clientId = UUID.randomUUID().toString()
        val pixId = UUID.randomUUID().toString()

        given(grpcClient.detail(Mockito.any()))
            .willReturn(grpcPixKeyResponse(clientId, pixId))

        val request = HttpRequest.GET<Any>("api/v1/clientes/$clientId/pix/$pixId")
        val response = httpClient.toBlocking().exchange(request, Any::class.java)

        response.run {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
        }
    }

    @Factory
    @Replaces(factory = GrpcStubsFactory::class)
    internal class MockitoStubFactory {

        @Singleton
        fun stubMock() =
            Mockito.mock(PixKeymanagerDetailServiceGrpc.PixKeymanagerDetailServiceBlockingStub::class.java)
    }

    fun grpcPixKeyResponse(clientId: String, pixId: String): DetailPixKeyResponse {
        return DetailPixKeyResponse
            .newBuilder()
            .setClientId(clientId)
            .setPixId(pixId)
            .setKey(
                DetailPixKeyResponse.PixKey
                    .newBuilder()
                    .setType(CPF_KEY_TYPE)
                    .setKey(CPF_KEY)
                    .setAccount(
                        DetailPixKeyResponse.PixKey.AccountInfo
                            .newBuilder()
                            .setType(CONTA_CORRENTE)
                            .setInstitution(INSTITUICAO)
                            .setOwnerName(OWNER_NAME)
                            .setOwnerCpf(OWNER_CPF)
                            .setAgency(AGENCY)
                            .setNumber(ACCOUNT_NUMBER)
                            .build()
                    )
                    .setCreationDate(
                        KEY_CREATION_DATE.let {
                            val createdAt = it.atZone(ZoneId.of("UTC")).toInstant()
                            Timestamp.newBuilder()
                                .setSeconds(createdAt.epochSecond)
                                .setNanos(createdAt.nano)
                                .build()
                        }
                    )
                    .build()
            )
            .build()
    }
}