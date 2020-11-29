package com.wistronics.sqlfmt.core

import com.wistronics.sqlfmt.utils.StringUtils._

object Sanitizer {
  def apply(sql: String): String =
    sql.replace(";", ";\n").sanitize
}
