package br.com.ot6.pix.dtos

import br.com.ot6.AccountType
import br.com.ot6.KeyType
import br.com.ot6.NewPixKeyRequest
import br.com.ot6.pix.shared.validations.ValidPixKey
import io.micronaut.core.annotation.Introspected
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Introspected
@ValidPixKey
data class RegisterPixKeyRequest(
    @field:NotNull val tipoDeChave: PixKeyTypeRequest?,
    @field:Size(max = 77) val chave: String?,
    @field:NotNull val tipoDeConta: AccountTypeRequest?
) {

    fun toGrpcRequest(clientId: UUID): NewPixKeyRequest? {
        return NewPixKeyRequest
            .newBuilder()
            .setId(clientId.toString())
            .setKeyType(tipoDeChave?.grpcKeyType ?: KeyType.UNKNOWN_KEY_TYPE)
            .setKeyValue(chave)
            .setAccountType(tipoDeConta?.grpcAccountType ?: AccountType.UNKNOWN_ACCOUNT_TYPE)
            .build()
    }
}

enum class PixKeyTypeRequest(val grpcKeyType: KeyType) {
    CPF(KeyType.CPF) {
        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) return false
            if (key.count() > 11) return false

            return CPFValidator().run {
                initialize(null)
                isValid(key, null)
            }
        }
    },
    CELULAR(KeyType.CELULAR) {
        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) return false
            return key.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    EMAIL(KeyType.EMAIL) {
        override fun validate(key: String?): Boolean {
            if (key.isNullOrBlank()) return false

            return EmailValidator().run {
                initialize(null)
                isValid(key, null)
            }
        }
    },
    ALEATORIA(KeyType.ALEATORIA) {
        override fun validate(key: String?): Boolean = key.isNullOrBlank()
    };

    abstract fun validate(key: String?): Boolean
}

enum class AccountTypeRequest(val grpcAccountType: AccountType) {
    CONTA_CORRENTE(AccountType.CONTA_CORRENTE),
    CONTA_POUPANCA(AccountType.CONTA_POUPANCA)
}
