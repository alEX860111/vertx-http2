package com.example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemKeyCertOptions;

final class ServerVerticle extends AbstractVerticle {

  private static final PemKeyCertOptions CERT_OPTIONS = new PemKeyCertOptions()
      .setKeyPath("server-key.pem")
      .setCertPath("server-cert.pem");

  private static final HttpServerOptions SERVER_OPTIONS = new HttpServerOptions()
      .setLogActivity(true)
      .setUseAlpn(true)
      .setSsl(true)
      .setPemKeyCertOptions(CERT_OPTIONS);

  private static final Logger LOGGER = LoggerFactory.getLogger(ServerVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    final HttpServer server = vertx.createHttpServer(SERVER_OPTIONS);

    server.requestHandler(req -> {
      final String path = req.path();
      final HttpServerResponse resp = req.response();

      if ("/".equals(path)) {

        resp.push(HttpMethod.GET, "/script.js", asyncResult -> {
          if (asyncResult.succeeded()) {
            LOGGER.info("sending push");
            final HttpServerResponse pushedResp = asyncResult.result();
            pushedResp.sendFile("script.js");
          } else {
            LOGGER.error(asyncResult.cause().getMessage());
          }
        });

        resp.sendFile("index.html");
      } else if ("/script.js".equals(path)) {
        resp.sendFile("script.js");
      } else {
        LOGGER.info("Not found " + path);
        resp.setStatusCode(404).end();
      }
    });

    server.listen(8443, "localhost", asyncResult -> {
      if (asyncResult.succeeded()) {
        LOGGER.info(this.getClass() + " initialized and listening on port " + asyncResult.result().actualPort() + ".");
        startFuture.complete();
      } else {
        LOGGER.error(asyncResult.cause().getMessage());
        startFuture.fail(asyncResult.cause());
      }
    });

  }

}
