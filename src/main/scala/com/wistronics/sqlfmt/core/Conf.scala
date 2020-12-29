package com.wistronics.sqlfmt.core

import java.io.File

import com.typesafe.config.{Config, ConfigFactory}

import scala.io.Source
import scala.util.Try

object Conf {
  val default: Config = ConfigFactory.parseResources("com/wistronics/sqlfmt/default.conf")
  def apply(configPath: Option[String] = None): Config =
    configPath.fold(default) { path =>
      ConfigFactory
        .parseFile(new File(path))
        .withFallback(default)
    }

  def getKeywords(path:String)(implicit conf: Config): Set[String] =
    conf
      .getStringList(path)
      .toArray
      .map {
        case system: String => f"com/wistronics/sqlfmt/keywords/$system.keywords"
      }
      .foldLeft(Set[String]()) {
        case (result: Set[String], resource: String) =>
          result ++ Try(Source.fromResource(resource).getLines()).getOrElse(List()).toSet
      }
}
