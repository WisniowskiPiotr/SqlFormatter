package com.wistronics.sqlfmt.core

import com.typesafe.config.Config

object Main {
  def main(args: Array[String]): Unit = {
    implicit val conf: Config = Conf()

    // 1. tockenize string that should not be modified
    // 2. serialize sql
    // 3. find selects
    // 4. format select
    //    format conditions
  }
}
