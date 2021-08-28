package br.com.ot6.pix

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class PixKeyTypeRequestTest {

    @Nested
    inner class CPF {

        @Test
        fun `should be valid if cpf format is correct`() {
            PixKeyTypeRequest.CPF.run {
                assertTrue(this.validate("35504715059"))
            }
        }

        @Test
        fun `should not be valid if cpf format is incorrect`() {
            PixKeyTypeRequest.CPF.run {
                assertFalse(this.validate("35504715051"))
                assertFalse(this.validate("355047150591"))
                assertFalse(this.validate("3550471505a"))
            }
        }

        @Test
        fun `should not be valid if cpf is null or empty`() {
            PixKeyTypeRequest.CPF.run {
                assertFalse(this.validate(""))
                assertFalse(this.validate(null))
            }
        }
    }

    @Nested
    inner class CELULAR {

        @Test
        fun `should be valid if phone number format is correct`() {
            PixKeyTypeRequest.CELULAR.run {
                assertTrue(this.validate("+5585988714077"))
            }
        }

        @Test
        fun `should not be valid if phone number format is incorrect`() {
            PixKeyTypeRequest.CELULAR.run {
                assertFalse(this.validate("14998271234"))
                assertFalse(this.validate("+5a585988714077"))
            }
        }

        @Test
        fun `should not be valid if phone number is null or empty`() {
            PixKeyTypeRequest.CELULAR.run {
                assertFalse(this.validate(""))
                assertFalse(this.validate(null))
            }
        }
    }

    @Nested
    inner class EMAIL {

        @Test
        fun `should be valid if email format is correct`() {
            PixKeyTypeRequest.EMAIL.run {
                assertTrue(this.validate("fulano@detal.com.br"))
                assertTrue(this.validate("fulano@detal.com"))
            }
        }

        @Test
        fun `should not be valid if email format is incorrect`() {
            PixKeyTypeRequest.EMAIL.run {
                assertFalse(this.validate("fulanodetal.com.br"))
                assertFalse(this.validate("fulano@detal.com."))
            }
        }

        @Test
        fun `should not be valid if email is null or empty`() {
            PixKeyTypeRequest.EMAIL.run {
                assertFalse(this.validate(""))
                assertFalse(this.validate(null))
            }
        }
    }

    @Nested
    inner class ALEATORIA {

        @Test
        fun `should be valid if random key comes null or valid`() {
            PixKeyTypeRequest.ALEATORIA.run {
                assertTrue(this.validate(null))
                assertTrue(this.validate(""))
            }
        }

        @Test
        fun `should not be valid if random key has a value`() {
            PixKeyTypeRequest.ALEATORIA.run {
                assertFalse(this.validate("value"))
            }
        }
    }
}