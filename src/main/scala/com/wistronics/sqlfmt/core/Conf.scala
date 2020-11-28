package com.wistronics.sqlfmt.core

import java.io.File

import com.typesafe.config.{ Config, ConfigFactory }

object Conf {
  def apply(configPath: Option[String] = None): Config = {
    val default = ConfigFactory.parseResources("com/wistronics/sqlfmt/default.conf")
    configPath.fold(default) { path =>
      ConfigFactory
        .parseFile(new File(path))
        .withFallback(default)
    }
  }
}
