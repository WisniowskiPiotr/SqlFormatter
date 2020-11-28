package com.wistronics.sqlfmt.utils

import org.apache.commons.lang3.{ StringUtils => JStringUtils }

object StringUtils {

  implicit class StringExt(v: String) {
    private val ToUnderscoreRegex = "(?<!_|^)([A-Z]|[0-9]+)"
    private val FromUnderscoreRegex = "_([a-z\\d])".r
    private val RemoveNonPrintableRegex = "[^\\p{Graph}]+" // removes also double spaces in same run

    def sanitize: String = v.replaceAll(RemoveNonPrintableRegex, " ").trim

    def hasOnlyUpperLetters: Boolean = v.filter(_.isLetter).forall(_.isUpper)

    def toUnderscore: String =
      v.sanitize
        .split("[ /\\-_|~]")
        .map { word =>
          if (word.hasOnlyUpperLetters)
            word.toLowerCase
          else
            ToUnderscoreRegex.r.replaceAllIn(word, m => "_" + m.group(0)).toLowerCase
        }
        .mkString("_")

    def fromUnderscore: String = FromUnderscoreRegex.replaceAllIn(v, m => m.group(1).sanitize.toUpperCase)

    def substringsBetween(prefix: String, suffix: Option[String] = None): Seq[String] =
      JStringUtils.substringsBetween(v, prefix, suffix.getOrElse(prefix))
  }
}
