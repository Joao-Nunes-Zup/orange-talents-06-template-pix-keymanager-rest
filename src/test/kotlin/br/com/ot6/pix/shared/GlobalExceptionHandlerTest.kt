package br.com.ot6.pix.shared

import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.hateoas.JsonError
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class GlobalExceptionHandlerTest {

    val genericRequest =  HttpRequest.GET<Any>("/")

    @Test
    fun `should return status code 404 when StatusRuntimeException code is NOT_FOUND`() {
        val message = "Não encontrado"
        val notFoundException =
            StatusRuntimeException(Status.NOT_FOUND.withDescription(message))

        val response = GlobalExceptionHandler().handle(genericRequest, notFoundException)

        response.run {
            assertEquals(HttpStatus.NOT_FOUND, status)
            assertNotNull(body())
            assertEquals(message, (body() as JsonError).message)
        }
    }

    @Test
    fun `should return status code 422 when StatusRuntimeException code is ALREADY_EXISTS`() {
        val message = "Chave já em uso"
        val alreadyExistsException =
            StatusRuntimeException(Status.ALREADY_EXISTS.withDescription(message))

        val response = GlobalExceptionHandler().handle(genericRequest, alreadyExistsException)

        response.run {
            assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, status)
            assertNotNull(body())
            assertEquals(message, (body() as JsonError).message)
        }
    }

    @Test
    fun `should return status code 400 when StatusRuntimeException code is INVALID_ARGUMENT`() {
        val alreadyExistsException =
            StatusRuntimeException(Status.INVALID_ARGUMENT)

        val response = GlobalExceptionHandler().handle(genericRequest, alreadyExistsException)

        response.run {
            assertEquals(HttpStatus.BAD_REQUEST, status)
            assertNotNull(body())
            assertEquals("Dados inválidos", (body() as JsonError).message)
        }
    }

    @Test
    fun `should return status code 500 when there is other error types`() {
        val alreadyExistsException =
            StatusRuntimeException(Status.UNKNOWN.withDescription("Erro desconhecido"))

        val response = GlobalExceptionHandler().handle(genericRequest, alreadyExistsException)

        response.run {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, status)
            assertNotNull(body())
            assertEquals(
                "Requisição incompleta devido ao erro: ${alreadyExistsException.status.description} - ${alreadyExistsException.status.code}",
                (body() as JsonError).message)
        }
    }
}