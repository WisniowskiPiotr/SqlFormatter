package com.wistronics.sqlfmt.core
import java.util.regex.Pattern

import com.typesafe.config.Config

import scala.io.Source
import scala.util.Try

object Caser {

  def keywords(implicit conf: Config): Set[String] =
    conf
      .getStringList("sql.keywords.system")
      .toArray
      .map {
        case system: String => f"com/wistronics/sqlfmt/keywords/$system.keywords"
      }
      .foldLeft(Set[String]()) {
        case (result: Set[String], resource: String) =>
          result ++ Try(Source.fromResource(resource).getLines()).getOrElse(List()).toSet
      }

  def toLowerOption(implicit conf: Config): Boolean = conf.getBoolean("sel.keywords.toLower")

  def apply(sql: String, keywords: Set[String], toLower: Boolean): String =
    keywords.foldLeft(sql) {
      case (sqlString, keyword) =>
        val replacement = if (toLower) keyword.toLowerCase else keyword
        val regex = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE)
        regex.matcher(sqlString).replaceAll(replacement)
    }
}
