package com.alvna

import com.alvna.config.ServerConfig
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, ListeningServer, Service}
import com.twitter.util.{Await,Future}
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

trait FinagleMockServer extends ServerConfig {
  private val logger : Logger = LogManager.getLogger(getClass)
  val server = Http.server
  var listener : ListeningServer = null

  private val service: Service[Request, Response] = {
    def apply(req: Request): Future[Response] =
      Future.value(Response())
  }

  def start() = {
    //Waits until the server resources are released
    logger.info("Starting server ... ")

    listener = server.serve(ServerUrl,service)
    Await.ready(listener)
  }

  def stop(): Unit ={
    //Waits until the server resources are released
    logger.info("Stopping server ...")
    listener.close().onSuccess(
      _ => logger.info("Server stopped successfully"))
      .onFailure( t => {
        logger.error("An error occurred stopping the server ...")
        logger.error(t.getMessage)
      })
  }
}
