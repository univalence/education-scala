package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

/**
 * =Welcome to Scala 🎉=
 *
 * Our Scala journey always start with a main function.
 *
 * In Scala 3, you can define a main function as follows.
 */
@main
def _01_main(): Unit = println("Hello world")

/**
 * NOTE
 *
 * [[Unit]] is an almost equivalent type to `void` in Java and C/C++. The
 * sole difference is that [[Unit]] as just one associated value: `()`.
 */

/**
 * ==STRING INTERPOLATION==
 *
 * String interpolation allows you to create string with placeholders.
 * To do so, you have to prefix the string with `s` and use `$` with a
 * variable name to represent a placeholder.
 *
 * Note#1: the placeholder might be an expression in this case, you have
 * to delimit the expression with curly braces `{}`. Eg. `s"one plus one
 * is ${1 + 1}"`.
 *
 * Note#2: there are other available prefixes, like `f` for simple
 * formatted string (eg. `f"height is $height%2.2f meters tall."`), or
 * `raw` for string with no escaping symbol (eg. `raw"success \o/"`
 */
@main
def _01_run2(): Unit =
  // TODO use string interpolation to display Mary
  val name1 = "John"
  val name2 = "Mary"
  println(s"Hello $name1 and [put second interpolation here]")

/** ==MULTILINE STRING== */
@main
def _01_run3(): Unit =
/**
 * TODO copy/paste the text below in the string to display. Ensure
 * that the line breaks are respected.
 */
/* Sister Suzie, brother John
 * Martin Luther, Phil and Don
 * Brother Michael, auntie Gin
 * Open the door and let 'em in
 */
  println("""[paste here]""")
