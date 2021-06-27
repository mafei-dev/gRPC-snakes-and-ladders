package com.mafei.server.streaming.server;

import com.mafei.model.BalanceDeductResponse;
import io.grpc.stub.StreamObserver;

/**
 * @Author mafei
 * @Created 6/26/2021 7:47 PM
 */
public class ResponseObserver implements StreamObserver<BalanceDeductResponse> {
    @Override
    public void onNext(BalanceDeductResponse balanceDeductResponse) {
        System.out.println("balanceDeductResponse = " + balanceDeductResponse);

    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("throwable = " + throwable.getMessage());

    }

    @Override
    public void onCompleted() {
        System.out.println("Done!");

    }
}
