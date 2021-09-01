package br.com.ot6.pix.dtos

import br.com.ot6.ListPixKeyResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class PixKeyResponse(pixKey: ListPixKeyResponse.PixKey) {

    val id = pixKey.pixId
    val key = pixKey.key
    val type = pixKey.type
    val accountType = pixKey.accountType
    val createdAt = pixKey.createdAt.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }

}
