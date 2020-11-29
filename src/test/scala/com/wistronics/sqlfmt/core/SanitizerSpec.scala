package com.wistronics.sqlfmt.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source


class SanitizerSpec extends AnyFlatSpec with Matchers {

  val testList: Seq[(String, String)] =
    Seq("basic_1.test.sql" -> "basic_1.result.sql")

  testList.map {
    case (k, v) =>
      (
        k,
        Source.fromResource(f"com/wistronics/sqlfmt/SanitizerSpec/$k").mkString,
        Source.fromResource(f"com/wistronics/sqlfmt/SanitizerSpec/$v").mkString
      )
  }.foreach {
    case (testFile: String, test: String, resultSql: String) =>
      "Sanitizer.apply" should f"sanitize SQL in $testFile" in {
        val res = Sanitizer(test)
        res shouldBe resultSql
      }
  }
}
