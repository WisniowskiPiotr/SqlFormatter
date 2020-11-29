package com.wistronics.sqlfmt.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

import com.wistronics.sqlfmt.utils.StringUtils._

class TokenizerSpec extends AnyFlatSpec with Matchers {

  def testTokenGenerator(blockExt: String, tokenBorder: (String, String)): String =
    f"${tokenBorder._1}token_${blockExt.sha1.substring(0, 4)}${tokenBorder._2}"

  val testList: Seq[(String, String)] = Seq("basic_1.test.sql" -> "basic_1.result.sql")

  testList.map {
    case (k, v) =>
      (
        k,
        Source.fromResource(f"com/wistronics/sqlfmt/TokenizerSpec/$k").mkString,
        Source.fromResource(f"com/wistronics/sqlfmt/TokenizerSpec/$v").mkString
      )
  }.foreach {
    case (testFile: String, test: String, resultSql: String) =>
      "Tokenizer.excludeNotModifiableStrings" should f"tokenize $testFile" in {
        val res = Tokenizer(test)(tokenBorders = Tokenizer.sqlTokenBorders, tokenGenerator = testTokenGenerator)
        res._1 shouldBe resultSql
      }
  }
}
