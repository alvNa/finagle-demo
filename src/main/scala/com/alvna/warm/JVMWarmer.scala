package com.alvna.warm

import com.alvna.server.FinagleMockServer
import com.twitter.util.Awaitable.CanAwait

class JVMWarmer(server : FinagleMockServer) {

  def warmUp(times: Int)(implicit permit : CanAwait): Unit ={
    server.listener.get.isReady
  }
}
