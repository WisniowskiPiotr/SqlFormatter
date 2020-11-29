package com.wistronics.sqlfmt.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class FormatterSpec extends AnyFlatSpec with Matchers {

  implicit val indent: String = Formatter.getIndent(Conf.default)

  val testList: Seq[(String, String)] =
    Seq("basic_1.test.sql" -> "basic_1.result.sql")

  testList.map {
    case (k, v) =>
      (
        k,
        Source.fromResource(f"com/wistronics/sqlfmt/FormatterSpec/$k").mkString,
        Source.fromResource(f"com/wistronics/sqlfmt/FormatterSpec/$v").mkString
      )
  }.foreach {
    case (testFile: String, test: String, resultSql: String) =>
      "FormatterSpec.apply" should f"format sql properlt in $testFile" in {
        val res = Formatter(test)
        res shouldBe resultSql
      }
  }
}
