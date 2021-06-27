package com.mafei.server.streaming.server;

import com.google.common.util.concurrent.Uninterruptibles;
import com.mafei.model.BalanceDeductRequest;
import com.mafei.model.BalanceDeductResponse;
import com.mafei.model.BankServiceGrpc;
import com.mafei.model.StreamBankServerServiceGrpc;
import com.mafei.server.GRPCServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StreamBankServerServiceTest {
    private static final String host = "localhost";
    private StreamBankServerServiceGrpc.StreamBankServerServiceBlockingStub streamBankServerServiceBlockingStub;
    private StreamBankServerServiceGrpc.StreamBankServerServiceStub streamBankServerServiceGrpc;

    @BeforeAll
    void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, GRPCServer.port)
                .usePlaintext()
                .build();
        //the non-blocking stub means it is Asynchronous and non-blocking
        this.streamBankServerServiceBlockingStub = StreamBankServerServiceGrpc.newBlockingStub(channel);
        this.streamBankServerServiceGrpc = StreamBankServerServiceGrpc.newStub(channel);
    }

    @Test
    void deductBalance() {

        BalanceDeductRequest request = BalanceDeductRequest
                .newBuilder()
                .setAccountNumber(1)
                .setDeductAmount(5)
                .build();


        try {
            streamBankServerServiceBlockingStub.deductBalance(request).forEachRemaining(balanceDeductResponse -> {
                System.out.println("balanceDeductResponse = " + balanceDeductResponse);
            });
        } catch (StatusRuntimeException e) {
            System.err.println("e = " + e.getMessage());
        }
    }

    @Test
    void deductBalanceAsync() {
        BalanceDeductRequest request = BalanceDeductRequest
                .newBuilder()
                .setAccountNumber(1)
                .setDeductAmount(5)
                .build();

        streamBankServerServiceGrpc.deductBalance(request, new ResponseObserver());
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
    }
}
