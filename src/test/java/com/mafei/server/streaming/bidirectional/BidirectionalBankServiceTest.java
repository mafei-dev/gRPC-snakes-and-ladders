package com.mafei.server.streaming.bidirectional;

import com.mafei.model.BidirectionalBankServiceGrpc;
import com.mafei.model.ClientSideStreamBankServiceGrpc;
import com.mafei.model.TransferRequest;
import com.mafei.model.TransferResponse;
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
public class BidirectionalBankServiceTest {
    private static final String host = "localhost";
    private BidirectionalBankServiceGrpc.BidirectionalBankServiceStub bidirectionalBankServiceStub;

    @BeforeAll
    void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, GRPCServer.port)
                .usePlaintext()
                .build();
        this.bidirectionalBankServiceStub = BidirectionalBankServiceGrpc.newStub(channel);
    }

    @Test
    void transfer() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        StreamObserver<TransferRequest> transfer = bidirectionalBankServiceStub.transfer(new StreamObserver<TransferResponse>() {

            @Override
            public void onNext(TransferResponse transferResponse) {
                transferResponse.getAccountsList().stream().map(account -> {
                    return account.getAccountNumber() + " : " + account.getAmount();
                }).forEach(System.out::println);
                System.out.println("----------------------------");
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("throwable = " + throwable.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println(" Done ");
                countDownLatch.countDown();
            }
        });

        for (int i = 0; i < 100; i++) {
            TransferRequest request = TransferRequest.newBuilder()
                    .setFromAccount(1)
                    .setToAccount(2)
                    .setAmount(2)
                    .build();
            transfer.onNext(request);
        }
        transfer.onCompleted();
        countDownLatch.await();
    }
}