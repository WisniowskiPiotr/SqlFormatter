package com.wistronics.sqlfmt.core

import scala.annotation.tailrec
import scala.util.Try

object Tokenizer {
  val sqlTokenBorders: Seq[(String,String,String)] = Seq(("'", "'", "'"), ("`", "`", "`"), ("--", "\n", "--\n"))

  private def tokenizeCondition(blockExt:String,prefix:String,suffix:String):Boolean={
    blockExt.startsWith(prefix) && Try(blockExt.substring(prefix.length)).getOrElse("").endsWith(suffix)
  }

  @tailrec
  private def iterateOverString(output: String, outputTokens: Map[String, String], input: Seq[Char], block: String)(
    implicit tokenBorders: Seq[(String, String, String)]
  ): (String, Map[String, String]) = {
    input match {
      case Seq(c, tail @ _*) =>
        block + c match {
          // tokenize block
          case blockExt: String if tokenBorders.exists {
            case (k, v, _) => tokenizeCondition(blockExt,k,v)
          } =>
            val tokenBorder = tokenBorders.find { case (k, v, _) => tokenizeCondition(blockExt,k,v) }.map {
              case (prefix, suffix, replacement) => (prefix.replace("\n", replacement), suffix.replace("\n", replacement))
            }.get
            val token = f"${tokenBorder._1}token_${outputTokens.size}${tokenBorder._2}"
            iterateOverString(
              output = output + token,
              outputTokens = outputTokens + (token -> blockExt),
              input = tail,
              block = ""
            )
          // key candidate needs next recursion
          case blockExt: String if tokenBorders.exists {
            case (k, _, _) if blockExt.length < k.length => k.startsWith(blockExt)
            case (k, _, _) => blockExt.startsWith(k)
          } =>
            iterateOverString(output = output, outputTokens = outputTokens, input = tail, block = blockExt)
          // no block char
          case blockExt: String => iterateOverString(
            output = output + blockExt,
            outputTokens = outputTokens,
            input = tail,
            block = ""
          )
        }
      // end of string
      case _ => (output + block, outputTokens)
    }
  }

  def excludeNotModifiableStrings(
    input: String
  )(implicit tokenBorders: Seq[(String, String, String)]): (String, Map[String, String]) =
    iterateOverString(output = "", outputTokens = Map(), input = input, block = "")
}
