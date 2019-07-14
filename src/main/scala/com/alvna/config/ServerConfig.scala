package com.alvna.config

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

/**
  * External configuration from application.conf
  **/
object ServerConfig extends ServerConfig

trait ServerConfig {
  lazy val config: Config = ConfigFactory.load()

  val httpConfig: Config = config.getConfig("http")

  val Url = httpConfig.getString("uri")
  val Port = httpConfig.getInt("port")

  val ServerUrl = s"$Url$Port"
}
