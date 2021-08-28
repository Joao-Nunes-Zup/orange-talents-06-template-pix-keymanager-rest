package br.com.ot6.pix.shared.grpc

import br.com.ot6.PixKeymanagerRegisterServiceGrpc
import br.com.ot6.PixKeymanagerRegisterServiceGrpc.PixKeymanagerRegisterServiceBlockingStub
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import jakarta.inject.Singleton

@Factory
class GrpcStubsFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun register(): PixKeymanagerRegisterServiceBlockingStub =
        PixKeymanagerRegisterServiceGrpc.newBlockingStub(channel)
}