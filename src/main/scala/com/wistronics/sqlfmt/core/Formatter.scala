package com.wistronics.sqlfmt.core

import com.typesafe.config.Config
import com.wistronics.sqlfmt.utils.StringUtils._

import scala.annotation.tailrec

object Formatter {
  def getIndent(implicit conf: Config): String = conf.getString("sql.indent")

  val selectKeywords: Set[String] = Set("FROM","WHERE","GROUP","HAVING","ORDER","LIMIT")

  def apply(sql: String)(implicit indent:String): String =
    sql.split(";").map(statement => selectFormat("",statement.split(" "),"")._1).mkString(";\n")

  @tailrec
  def selectFormat(output:String, words:Seq[String], currentIndent:String)(implicit indent:String):(String,Seq[String],String) = {
    words match {
      case Seq(token, tail @ _*) if token.startsWith("--") => selectFormat(f"$output$currentIndent$token\n", tail, currentIndent)
      case Seq(token, tail @ _*) if token.startsWith("--") => selectFormat(f"$output$currentIndent$token\n", tail, currentIndent)
      case Seq("SELECT", tail @ _*) => queryValuesFormatter(f"$output${currentIndent}SELECT ALL\n", tail, currentIndent.indent)
      case Seq("FROM", schemaTable, "AS", alias, tail @ _*) => selectFormat(f"$output${currentIndent}FROM $schemaTable AS $alias\n", tail, currentIndent)
      case Seq("FROM", schemaTable, alias, tail @ _*) if !selectKeywords.contains(alias) => selectFormat(f"$output${currentIndent}FROM $schemaTable AS $alias\n", tail, currentIndent)
      case Seq("FROM", schemaTable, tail @ _*) => selectFormat(f"$output${currentIndent}FROM $schemaTable\n", tail, currentIndent)
      case Seq("WHERE", tail @ _*) => whereConditionsFormatter(f"$output\n${currentIndent}WHERE ", tail, currentIndent.indent)
      case Seq("GROUP", "BY", tail @ _*) => groupByFormat(f"$output\n${currentIndent}GROUP BY\n", tail, currentIndent.indent)
      case Seq("HAVING", tail @ _*) => havingFormatter(f"$output\n${currentIndent}WHERE ", tail, currentIndent.indent)
      case Seq("ORDER", "BY", tail @ _*) => orderByFormat(f"$output\n${currentIndent}ORDER BY\n", tail, currentIndent.indent)
      case Seq("LIMIT", limit, tail @ _*) => selectFormat(f"$output\n${currentIndent}LIMIT $limit", tail, currentIndent.undent)
      //case otherWords => selectFormat(output, tail, currentIndent.undent)
    }
  }

  @tailrec
  def queryValuesFormatter(output:String, words:Seq[String], currentIndent:String)(implicit indent:String,conf: Config):(String,Seq[String],String) = {
    words match {
      case Seq("FROM", _*) => selectFormat(output, words, currentIndent.undent)
      case Seq(value, tail @ _*) if value.startsWith("--") =>queryValuesFormatter(f"$output$currentIndent$value\n",tail,currentIndent)
      case Seq(value, "AS", alias, tail @ _*) => queryValuesFormatter(f"$output$currentIndent$value AS $alias\n",tail,currentIndent)
      case Seq(value, alias, tail @ _*) => queryValuesFormatter(f"$output$currentIndent$value AS $alias\n",tail,currentIndent)
      case Seq(value, tail @ _*) =>queryValuesFormatter(f"$output$currentIndent$value\n",tail,currentIndent)
      case _ =>selectFormat(output,Seq(),currentIndent.undent)
    }
  }

  def whereConditionsFormatter(output:String, words:Seq[String], currentIndent:String)(implicit indent:String,conf: Config):(String,Seq[String],String) = {
    words match {
      case Seq("AND", value, tail @ _*) => whereConditionsFormatter(f"$output\n${currentIndent}AND $value ",tail,currentIndent)
      case Seq( value, tail @ _*) if !Caser.keywords.contains(value) => whereConditionsFormatter(f"$output$value ",tail,currentIndent)
      case Seq( value, tail @ _*) if !Caser.keywords.contains(value) => whereConditionsFormatter(f"$output$value ",tail,currentIndent)
    }
  }


  @tailrec
  def conditionsFormatter(output:String, words:Seq[String], currentIndent:String)(implicit indent:String,conf: Config):String = {
    words match {
      case Seq("FROM", _*) => conditionsFormatter(output, words, currentIndent.undent)
      case Seq(value, "AS", alias, tail @ _*) if alias.endsWith(",") => queryValuesFormatter(f"$output $value AS $alias,\n", tail, currentIndent)
      case Seq(value, tail @ _*) if value.endsWith(",") => queryValuesFormatter(f"$output\n$currentIndent$value\n", tail, currentIndent)
      case Seq(value, tail @ _*) => queryValuesFormatter(f"$output $value", tail, currentIndent)
      case _ => output
    }
  }

  @tailrec
  def conditionsFormatter(output:String, words:Seq[String], currentIndent:String)(implicit indent:String):String = {
    words match {
      case Seq("FROM", _*) => conditionsFormatter(output, words, currentIndent.undent)
      case Seq(value, "AS", alias, tail @ _*) if alias.endsWith(",") => queryValuesFormatter(f"$output $value AS $alias,\n", tail, currentIndent)
      case Seq(value, tail @ _*) if value.endsWith(",") => queryValuesFormatter(f"$output\n$currentIndent$value\n", tail, currentIndent)
      case Seq(value, tail @ _*) => queryValuesFormatter(f"$output $value", tail, currentIndent)
      case _ => output
    }
  }
/*
  @tailrec
  def caseWhenFormatter(output:String, words:Seq[String], currentIndent:String)(implicit indent:String):String = {
    words match {
      case Seq("CASE", "WHEN", tail @ _*) => caseWhenFormatter(f"$output\n${currentIndent}CASE\n${currentIndent.indent}WHEN ", tail, currentIndent.indent)
      case Seq("THEN", tail @ _*) => caseWhenFormatter(f"$output\n${currentIndent.indent}THEN ", tail, currentIndent)
    }
  }*/

}
