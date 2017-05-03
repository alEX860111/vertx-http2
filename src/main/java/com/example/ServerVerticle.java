package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

final class ServerVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.createHttpServer().requestHandler(request -> {
      request.response().end("Hello world");
    }).listen(8080);
  }

}
