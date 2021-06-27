package com.mafei.server;

import com.mafei.model.Die;
import com.mafei.model.GameServiceGrpc;
import com.mafei.model.GameState;
import com.mafei.model.Player;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameServiceTest {

    private GameServiceGrpc.GameServiceStub gameServiceStub;

    @BeforeAll
    void setUp() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", GRPCServer.port)
                .usePlaintext()
                .build();
        this.gameServiceStub = GameServiceGrpc.newStub(channel);
    }

    @Test
    void roll() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        GameStreamingResponse gameStreamingResponse = new GameStreamingResponse(countDownLatch);
        StreamObserver<Die> dieStreamObserver = this.gameServiceStub.roll(gameStreamingResponse);
        gameStreamingResponse.setDieStreamObserver(dieStreamObserver);
        gameStreamingResponse.roll();
        countDownLatch.await();
    }
}