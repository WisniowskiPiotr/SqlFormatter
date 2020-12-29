package com.wistronics.sqlfmt.core
import java.util.regex.Pattern

object Caser {
  def apply(sql: String, keywords: Set[String], toLower: Boolean): String =
    keywords.foldLeft(sql) {
      case (sqlString, keyword) =>
        val replacement = if (toLower) keyword.toLowerCase else keyword
        val regex = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE)
        regex.matcher(sqlString).replaceAll(replacement)
    }
}
