package com.wistronics.sqlfmt.core

import com.typesafe.config.Config
import com.wistronics.sqlfmt.utils.StringUtils._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val conf: Config = Conf()

    val sqls = Map("" -> "")
    sqls.map {
      case (fileName, sql) =>
        // fix comas, missing as keywords and `.`
        val (tokenized, tokens) = Tokenizer(sql)
        val sanitized = tokenized.sanitize
        val cased = Caser(sanitized, keywords = Caser.keywords, toLower = Caser.toLowerOption)

      //val cased =
    }

    // 2. serialize sql
    // 3. find selects
    // 4. format select
    //    format conditions
  }
}
