package br.com.ot6.pix.dtos

import br.com.ot6.AccountType
import br.com.ot6.DetailPixKeyResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class PixKeyDetailsResponse(grpcResponse: DetailPixKeyResponse) {

    val pixId = grpcResponse.pixId
    val type = grpcResponse.key.type
    val key = grpcResponse.key.key
    val createdAt = grpcResponse.key.creationDate.let {
        LocalDateTime.ofInstant(Instant.ofEpochSecond(it.seconds, it.nanos.toLong()), ZoneOffset.UTC)
    }
    val accountType = when (grpcResponse.key.account.type) {
        AccountType.CONTA_CORRENTE -> "CONTA_CORRENTE"
        AccountType.CONTA_POUPANCA -> "CONTA_POUPANCA"
        else -> "NAO_RECONHECIDA"
    }
    val conta: AccountResponse = AccountResponse(grpcResponse.key)
}

class AccountResponse(responseKey: DetailPixKeyResponse.PixKey) {
    val instituicao = responseKey.account.institution
    val ownerName = responseKey.account.ownerName
    val ownerCpf = responseKey.account.ownerCpf
    val agency = responseKey.account.agency
    val number = responseKey.account.number
}
