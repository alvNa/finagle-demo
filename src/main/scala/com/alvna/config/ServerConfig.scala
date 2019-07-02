package com.alvna.config

import com.typesafe.config.ConfigFactory


/**
  * External configuration from application.conf
  **/
object ServerConfig extends ServerConfig

trait ServerConfig {

  type TypeSafeConfig = com.typesafe.config.Config
  lazy val config: TypeSafeConfig = ConfigFactory.load()

  val httpConfig: TypeSafeConfig = config.getConfig("http")

  val Url = httpConfig.getString("uri")
  val Port = httpConfig.getInt("port")

  val ServerUrl = s"$Url$Port"
}
