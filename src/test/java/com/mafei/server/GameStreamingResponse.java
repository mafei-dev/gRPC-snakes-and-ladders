package com.mafei.server;

import com.mafei.model.Die;
import com.mafei.model.GameState;
import com.mafei.model.Player;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author mafei
 * @Created 6/27/2021 11:55 PM
 */
public class GameStreamingResponse implements StreamObserver<GameState> {

    private CountDownLatch countDownLatch;
    private StreamObserver<Die> dieStreamObserver;

    public GameStreamingResponse(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onNext(GameState gameState) {
        List<Player> playersList = gameState.getPlayersList();
        playersList.forEach(player -> {
            System.out.println("player = " + player);
        });

        boolean isGameOver = playersList.stream().anyMatch(player -> player.getPosition() == 100);
        if (isGameOver) {
            System.out.println("game over!");
            this.dieStreamObserver.onCompleted();
        } else {
            this.roll();
        }

        System.out.println("--------------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {
        this.countDownLatch.countDown();
    }

    @Override
    public void onCompleted() {
        this.countDownLatch.countDown();
    }

    public void roll() {
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder().setValue(dieValue).build();
        this.dieStreamObserver.onNext(die);

    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver) {
        this.dieStreamObserver = dieStreamObserver;
    }
}
