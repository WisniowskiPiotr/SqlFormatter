package com.wistronics.sqlfmt.core

import com.typesafe.config.Config
import com.wistronics.sqlfmt.utils.StringUtils._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val conf: Config = Conf() // TODO: read config path from args.
    // TODO: also read config values from args as priority over the ones from files.

    val sqls = Map("" -> "") // TODO: read all files specified by arg parameter in a dir
    sqls.foreach{
      case (fileName, sql) =>
        // TODO: future: fix comas, missing AS keywords and `.`
        val (tokenized, tokens) = Tokenizer(sql) // TODO: parametrize token borders in config
        val sanitized = tokenized.sanitize
        val cased = Caser(sanitized, keywords = Conf.getKeywords("sql.keywords.system"), toLower = conf.getBoolean("sql.keywords.to_lower"))
        val formatted = Formatter(cased)(conf.getString("sql.indent"))
      // replace tokens in formatted sql
      // save formatted files wih overwrite or fail if they are different than tokenized in case of check run
    }
  }
}
