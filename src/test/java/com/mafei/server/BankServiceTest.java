package com.mafei.server;

import com.mafei.model.BalanceCheckRequest;
import com.mafei.model.BalanceResponse;
import com.mafei.model.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {

    private static final String host = "localhost";
    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

    @BeforeAll
    void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, GRPCServer.port)
                .usePlaintext()
                .build();
        //the blocking stub means it is synchronous and blocking
        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(channel);
    }


    @Test
    public void balanceTest() {
        BalanceCheckRequest balanceCheckRequest = BalanceCheckRequest.newBuilder()
                .setAccountNumber(2)
                .build();

        BalanceResponse balance = this.bankServiceBlockingStub.getBalance(balanceCheckRequest);
        System.out.println("balance = " + balance);

    }
}