/*
 * Copyright 2022 Univalence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.univalence.education.internal

import scala.quoted.*
import scala.reflect.ClassTag
import scala.reflect.ClassTag.Nothing

object exercise_tools {

  final private[internal] case class FileContext(file: String, path: String, line: Int, context: Seq[PartContext])
  final private[internal] case class CheckContext(expression: String, fileContext: FileContext)

  private[internal] enum CheckResultType {
    case SUCCESS, FAILURE, ERROR, TODO
  }

  private[internal] enum CheckResult {
    case Success(context: CheckContext)
    case Failure(context: CheckContext)
    case Error(context: CheckContext, exception: Throwable)
    case Todo(context: CheckContext)

    def expression: String =
      this match {
        case Success(context)  => context.expression
        case Failure(context)  => context.expression
        case Error(context, _) => context.expression
        case Todo(context)     => context.expression
      }

    def getType: CheckResultType =
      this match {
        case Success(_)  => CheckResultType.SUCCESS
        case Failure(_)  => CheckResultType.FAILURE
        case Error(_, _) => CheckResultType.ERROR
        case Todo(_)     => CheckResultType.TODO

      }
  }

  final private[internal] case class PartContext(title: String)

  private var activatedContexts: Seq[PartContext] = Seq.empty

  /** value place holder */
  final inline def ?? : Any = Nothing

  /** function body place holder */
  final inline def |>? : Nothing = throw new NotImplementedError

  /** type place holder */
  final type !? = Nothing

  /** Check a boolean expression and display the result. */
  inline def check(inline expression: Boolean): Unit = ${ checkImpl('expression) }

  private def checkImpl(expression: Expr[Boolean])(using Quotes): Expr[Unit] = {
    val result = checkResultOfImpl(expression)

    '{
      displayCheckResult($result)
    }
  }

  private[internal] inline def checkResultOf(inline expression: Boolean): CheckResult =
    ${ checkResultOfImpl('expression) }

  private def checkResultOfImpl(expression: Expr[Boolean])(using Quotes): Expr[CheckResult] = {
    import quotes.reflect.*

    val fileContext = captureFileContext(expression)

    val term = expression.asTerm.underlyingArgument

    val position   = term.pos
    val sourceCode = position.sourceCode.getOrElse("")
    val exprBody   = Expr(sourceCode.replace("\n", "\n\t"))

    '{
      val context = CheckContext($exprBody, $fileContext)
      evaluate($expression, context)
    }
  }

  private def captureFileContext[A](content: Expr[A])(using Quotes): Expr[FileContext] = {
    import quotes.reflect
    import quotes.reflect.*

    val sourceFilename = Expr(reflect.SourceFile.current.name)
    val position       = content.asTerm.underlyingArgument.pos
    val path           = Expr(position.sourceFile.path)
    val line           = Expr(position.startLine + 1)

    '{ FileContext(file = $sourceFilename, path = $path, line = $line, context = activatedContexts) }
  }

  /**
   * Create an exercise.
   *
   * @param title
   *   simple label
   * @param activated
   *   indicate if the code of the exercise is activated
   * @param content
   *   code of the exercise
   */
  inline def exercise(title: String, inline activated: Boolean = true)(content: => Unit): Unit =
    inline if (activated) {
      activatedContexts = activatedContexts :+ PartContext(title)
      part(activatedContexts.map(_.title).mkString(" > "))

      try content
      catch {
        case PartException(l, c) =>
          throw PartException(s"$title > $l", c)
        case e: Exception =>
          throw PartException(title, e)
        case e: NotImplementedError =>
          val trace    = e.getStackTrace.toList.filter(_.getClassName.startsWith("io.univalence")).head
          val filename = trace.getFileName
          val line     = trace.getLineNumber
          println(
            indent + s">>> ${Console.CYAN}TODO an implementation is missing.${Console.RESET} ($filename:$line)"
          )
      } finally activatedContexts = activatedContexts.init
    } else {

      part((activatedContexts.map(_.title) :+ s"$title ${Console.RED}(TO ACTIVATE)").mkString(" > "))
    }

  
  inline def indent: String = "\t" * activatedContexts.size
  
  inline def section(label: String)(f: => Unit): Unit = exercise(label)(f)

  final case class PartException(label: String, cause: Throwable)
      extends RuntimeException(s"""Exception caught in part "$label"""", cause)

  inline def exercisePart(title: String): Unit = part((activatedContexts.map(_.title) :+ title).mkString(" > "))

  /** Display a part with a file name. */
  inline def part(inline title: String): Unit = ${ partImpl('title) }

  private def partImpl(title: Expr[String])(using Quotes): Expr[Unit] = {
    val fileContext = captureFileContext(title)

    '{
      println(s"${Console.BOLD}${Console.YELLOW}${$fileContext.file}:${$fileContext.line} - ${$title}${Console.RESET}")
    }
  }

  private def evaluate(expression: => Boolean, checkContext: CheckContext): CheckResult =
    scala.util.Try(expression) match {
      case scala.util.Success(true)  => CheckResult.Success(checkContext)
      case scala.util.Success(false) => CheckResult.Failure(checkContext)
      case scala.util.Failure(exception) =>
        exception match {
          case _: NotImplementedError => CheckResult.Todo(checkContext)
          case _                      => CheckResult.Error(checkContext, exception)
        }
    }

  private def displayCheckResult(result: CheckResult): Unit = {
    val resultDisplay =
      result match {
        case CheckResult.Success(context) =>
          s"${Console.GREEN}${result.expression} OK (line:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Failure(context) =>
          s"${Console.YELLOW}${result.expression} FAILED (${context.fileContext.path}:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Todo(context) =>
          s"${Console.CYAN}TODO missing implementation detected while calling${Console.RESET} ${result.expression} (${context.fileContext.path}:${context.fileContext.line})"
        case CheckResult.Error(context, e) =>
          s"${Console.RED}${result.expression} ERROR (${context.fileContext.path}:${context.fileContext.line}: ${e.getClass.getCanonicalName}: ${e.getMessage})${Console.RESET}"
      }

    println(indent + s">>> $resultDisplay")
  }

}
