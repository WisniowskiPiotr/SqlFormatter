package com.wistronics.sqlfmt.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class TokenizerSpec extends AnyFlatSpec with Matchers {

  val resourceList: Seq[(String, String)] = Seq(
    "basic_1.test.sql" -> "basic_1.result.sql"
  )

  resourceList.map { case (k, v) => (k, Source.fromResource(f"com/wistronics/sqlfmt/TokenizerSpec/$k").mkString, Source.fromResource(f"com/wistronics/sqlfmt/TokenizerSpec/$v").mkString) }.foreach {
    case (testFile: String, test: String, resultSql: String) =>
      "Tokenizer.excludeNotModifiableStrings" should f"tokenize $testFile" in {
        val res = Tokenizer.excludeNotModifiableStrings(test)(Tokenizer.sqlTokenBorders)
        res._1 shouldBe resultSql
      }
  }
}
