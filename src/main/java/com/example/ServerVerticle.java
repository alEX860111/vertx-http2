package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemKeyCertOptions;

final class ServerVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    final HttpServerOptions options = new HttpServerOptions().setLogActivity(true).setUseAlpn(true).setSsl(true)
        .setPemKeyCertOptions(new PemKeyCertOptions().setKeyPath("server-key.pem").setCertPath("server-cert.pem"));

    vertx.createHttpServer(options).requestHandler(request -> {
      request.response().end("Hello world");
    }).listen(8443, "localhost", result -> {
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
