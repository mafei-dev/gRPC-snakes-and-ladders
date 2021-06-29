package com.mafei.server;

import com.mafei.model.SampleRequest;
import com.mafei.model.SampleResponse;
import com.mafei.model.SampleServiceGrpc;
import io.grpc.stub.StreamObserver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author mafei
 * @Created 6/29/2021 11:26 PM
 */
public class SampleService extends SampleServiceGrpc.SampleServiceImplBase {
    @Override
    public void test(SampleRequest request, StreamObserver<SampleResponse> responseObserver) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS'Z'");
        responseObserver.onNext(SampleResponse.newBuilder().setDatetime(simpleDateFormat.format(date)).build());
        responseObserver.onCompleted();
    }
}
