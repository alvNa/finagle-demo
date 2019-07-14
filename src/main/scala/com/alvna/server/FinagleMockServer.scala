package com.alvna.server

import com.alvna.config.ServerConfig
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, ListeningServer, Service}
import com.twitter.util.{Awaitable, Duration, Future}
import org.apache.logging.log4j.LogManager

trait FinagleMockServer extends ServerConfig {
  private val logger = LogManager.getLogger(getClass)
  val TimeOut = Duration.fromSeconds(10)

  private lazy val service = createService()
  private lazy val server = createBaseHttpServer()
  var listener : Option[ListeningServer]

  /**
    * Non blocking method. Return a Future of boolean indicating where
    * the service has started up.
    * */
  def start()(implicit permit : Awaitable.CanAwait): Future[Boolean] = {
    if (listener != None){
      logger.info("Server has currently started.")
    }
    else{
      logger.info("Starting server ... ")
      listener = Some(server.serve(ServerUrl,service))
    }

    Future.apply(listener.get.isReady)
  }

  def stop(): Future[Unit] = {
    if (listener == None){
      logger.info("Server hasn't started yet.")
      Future.Done
    }
    else{
      //Waits until the server resources are released
      logger.info("Stopping server ...")
      listener.get.close()
        .onSuccess(_ => logger.info("Server stopped successfully."))
        .onFailure( t => {
          logger.error("An error occurred stopping the server ...")
          logger.error(t.getMessage)
        })
    }
  }


  private def createBaseHttpServer(): Http.Server = {
    Http.server.withHttp2
  }

  private def createService(): Service[Request, Response] = {
    new Service[Request, Response] {
      def apply(req: Request): Future[Response] = Future.value(Response())
    }
  }

//  import java.util.concurrent.atomic.AtomicBoolean
//
//  import com.twitter.util.Future
//
//  val done = new AtomicBoolean(false)
//
//  def callThatReturnsFuture()(implicit permitable : Awaitable.CanAwait): Future[Boolean] = {
//    Future(listener.isReady)
//  }
//
//
//  def loop()(implicit permitable : Awaitable.CanAwait ): Future[Boolean] = {
//    if (done.get) {
//      Future.True
//    } else {
//      callThatReturnsFuture().before {
//        loop()
//      }
//    }
//  }
}
