package com.wistronics.sqlfmt.core

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.io.Source

class CaserSpec extends AnyFlatSpec with Matchers {

  val keywords: Set[String] = Caser.keywords(Conf.default)

  val testList: Seq[(String, String)] =
    Seq("basic_1.test.sql" -> "basic_1.result.sql", "basic_2.toLower.test.sql" -> "basic_2.toLower.result.sql")

  testList.map {
    case (k, v) =>
      (
        k,
        Source.fromResource(f"com/wistronics/sqlfmt/CaserSpec/$k").mkString,
        Source.fromResource(f"com/wistronics/sqlfmt/CaserSpec/$v").mkString
      )
  }.foreach {
    case (testFile: String, test: String, resultSql: String) =>
      val isToLower = testFile.contains("toLower")
      "Caser.apply" should f"convert keywords in $testFile to ${if (isToLower) "lowercase" else "uppercase"}" in {
        val res = Caser(test, keywords = keywords, toLower = testFile.contains("toLower"))
        res shouldBe resultSql
      }
  }
}
