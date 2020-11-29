package com.wistronics.sqlfmt.core

import java.io.File

import com.typesafe.config.{ Config, ConfigFactory }

object Conf {
  val default: Config = ConfigFactory.parseResources("com/wistronics/sqlfmt/default.conf")
  def apply(configPath: Option[String] = None): Config =
    configPath.fold(default) { path =>
      ConfigFactory
        .parseFile(new File(path))
        .withFallback(default)
    }
}
