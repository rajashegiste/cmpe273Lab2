package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class PollServiceServer {

    private static final Logger logger = Logger.getLogger(PollServiceServer.class.getName());
    private int port = 50051;
    private ServerImpl server;

    private void start() throws Exception {
        server = NettyServerBuilder.forPort(port)
                .addService(PollServiceGrpc.bindService(new PollServiceImpl()))
                .build().start();
        logger.info("Server started, listening on : " + port );
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                PollServiceServer.this.stop();
                System.err.println("*** server shut down");
            }
        });

    }
    private void stop(){
        if(server != null){
            server.shutdown();
        }
    }
    public static void main(String[] args) throws Exception{
        final PollServiceServer server = new PollServiceServer();
        server.start();
    }

    private class PollServiceImpl implements PollServiceGrpc.PollService{
        @Override
        public void createPoll(PollRequest request, StreamObserver<PollResponse> responseObserver){

            final AtomicInteger counter = new AtomicInteger(2009234027);
            String poll_id = Integer.toHexString(counter.getAndIncrement());
	    String moderatorid= request.getModeratorId();
	    logger.info("Moderator Id : "+ moderatorid +" Generated");
            PollResponse reply = PollResponse.newBuilder().setId(poll_id).build();
            responseObserver.onValue(reply);
            responseObserver.onCompleted();

        }
    }

}


