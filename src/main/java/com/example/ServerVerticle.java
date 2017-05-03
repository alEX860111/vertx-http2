package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

final class ServerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.createHttpServer().requestHandler(request -> {
      request.response().end("Hello world");
    }).listen(8080, result -> {
      if (result.succeeded()) {
        LOGGER.info(this.getClass() + " initialized and listening on port " + result.result().actualPort() + ".");
        startFuture.complete();
      } else {
        LOGGER.error(result.cause().getMessage());
        startFuture.fail(result.cause());
      }
    });
  }

}
