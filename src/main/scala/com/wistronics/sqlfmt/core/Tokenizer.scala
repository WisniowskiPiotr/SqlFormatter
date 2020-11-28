package com.wistronics.sqlfmt.core

import scala.annotation.tailrec

object Tokenizer {
  val sqlTokenBorders = Seq(("'", "'", "'"), ("`", "`", "`"), ("--", "\n", "--\n"))

  @tailrec
  private def iterateOverString(output: String, outputTokens: Map[String, String], input: String, block: String)(
    implicit tokenBorders: Seq[(String, String, String)]
  ): (String, Map[String, String]) =
    input.toSeq match {
      case Seq(c, tail @ _*) if tokenBorders.exists {
            case (k, _, _) if (block + c).length < k.length => k.startsWith(block + c)
            case (k, _, _) => (block + c).startsWith(k)
          } =>
        // key candidate needs next recursion
        iterateOverString(output = output, outputTokens = outputTokens, input = tail.toString, block = block + c)
      case Seq(c, tail @ _*) if tokenBorders.exists {
            case (k, _, _) if block.length < k.length => k.startsWith(block)
            case (k, _, _) => block.startsWith(k)
          } =>
        // key candidate invalidated
        iterateOverString(output = output + block + c, outputTokens = outputTokens, input = tail.toString, block = "")
      case Seq(c, tail @ _*) if tokenBorders.exists {
            case (k, v, _) => {
              val blockExt = block + c
              blockExt.startsWith(k) && blockExt.endsWith(v)
            }
          } =>
        // tokenize block
        val blockExt = block + c
        val tokenBorder = tokenBorders.find { case (k, v, _) => blockExt.startsWith(k) && blockExt.endsWith(v) }.map {
          case (prefix, suffix, replacement) => (prefix.replace("\n", replacement), suffix.replace("\n", replacement))
        }.get
        val token = f"${tokenBorder._1}token_${outputTokens.size}${tokenBorder._2}"
        iterateOverString(
          output = output + token,
          outputTokens = outputTokens + (token -> blockExt),
          input = tail.toString,
          block = ""
        )
      case _ => (output, outputTokens)
    }

  def excludeNotModifiableStrings(
    input: String
  )(implicit tokenBorders: Seq[(String, String, String)]): (String, Map[String, String]) =
    iterateOverString(output = "", outputTokens = Map(), input = input, block = "")
}
