package com.example;

import io.vertx.core.Vertx;

public final class Main {

  public static void main(final String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ServerVerticle());
  }

}
