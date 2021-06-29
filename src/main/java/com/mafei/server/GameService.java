package com.mafei.server;

import com.mafei.model.Die;
import com.mafei.model.GameServiceGrpc;
import com.mafei.model.GameState;
import com.mafei.model.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @Author mafei
 * @Created 6/27/2021 8:01 PM
 */
public class GameService extends GameServiceGrpc.GameServiceImplBase {
    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {
        System.out.println("GameService.roll");
        StreamObserver<Die> streamObserver = new StreamObserver<Die>() {
            Player client = Player.newBuilder().setName("client").build();
            Player server = Player.newBuilder().setName("server").build();

            @Override
            public void onNext(Die die) {
                System.out.println("die = " + die);
                this.client = this.getNewPlayerPosition(this.client, die.getValue());
                //100 is the count of squares on the board
                if (this.client.getPosition() != 100) {
                    this.server = this.getNewPlayerPosition(
                            this.server,
                            ThreadLocalRandom.current().nextInt(1, 7)
                    );
                }
                responseObserver.onNext(this.getGameState());
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("throwable.getMessage() = " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("done");
                responseObserver.onCompleted();
            }

            private GameState getGameState() {
                return GameState.newBuilder()
                        .addPlayers(this.server)
                        .build();
            }

            private Player getNewPlayerPosition(Player player, int dieValue) {
                int position = player.getPosition() + dieValue;
                position = GameData.getPosition(position);
                if (position <= 100) {
                    player = Player.newBuilder()
                            .setPosition(position)
                            .build();
                }
                return player;
            }
        };
        return streamObserver;
    }
}
