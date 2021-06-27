package com.mafei.server.streaming.clientside;

import com.mafei.model.ClientSideStreamBankServiceGrpc;
import com.mafei.model.DepositRequest;
import com.mafei.model.DepositResponse;
import com.mafei.model.StreamBankServerServiceGrpc;
import com.mafei.server.GRPCServer;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientSideStreamBankServiceTest {
    private static final String host = "localhost";
    private ClientSideStreamBankServiceGrpc.ClientSideStreamBankServiceStub clientSideStreamBankServiceStub;

    @BeforeAll
    void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, GRPCServer.port)
                .usePlaintext()
                .build();
        this.clientSideStreamBankServiceStub = ClientSideStreamBankServiceGrpc.newStub(channel);
    }

    @Test
    void cashDeposit() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        StreamObserver<DepositRequest> depositRequestStreamObserver = clientSideStreamBankServiceStub.cashDeposit(new StreamObserver<DepositResponse>() {
            CountDownLatch _countDownLatch = countDownLatch;

            @Override
            public void onNext(DepositResponse depositResponse) {
                System.out.println("depositResponse = " + depositResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(throwable.getMessage());
                _countDownLatch.countDown();

            }

            @Override
            public void onCompleted() {
                System.out.println("Done");
                _countDownLatch.countDown();
            }
        });
        for (int i = 0; i < 6; i++) {
            DepositRequest request = DepositRequest.newBuilder()
                    .setAmount(10)
                    .setAccountNumber(1)
                    .build();
            depositRequestStreamObserver.onNext(request);
        }
        depositRequestStreamObserver.onCompleted();
        countDownLatch.await();
    }
}