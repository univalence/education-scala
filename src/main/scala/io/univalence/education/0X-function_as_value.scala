package io.univalence.education.internal

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.util.Random

import java.time.LocalDate

/**
 * =Function as value=
 * Like many other functional programming language, you can define
 * function as value.
 *
 * Function as value main benefit is:
 *   - The possibility to give a function in parameter
 *   - The possibility to return a function
 *   - The possibility to compose functions
 *
 * We will see these three in this lab.
 */
@main
def function_as_value(): Unit =
  section("The basis of function as value") {

    exercise("Declaring a function as a value") {
      val uppercaseAsValue: String => String = string => string.toUpperCase
      //                    ^^^^^^^^^^^^^^^^   ^^^^^^^^^^^^^^^^^^^^^^^^^^^^
      //                    |                  Body of the function
      //                    Parameter -> Return type

      // Find the equivalent of this function as value using def:
      def uppercaseAsFunction(string: String): String = string.toUpperCase

      check(uppercaseAsFunction("scala") == uppercaseAsValue("scala"))
    }

    exercise("Passing a function as a parameter") {
      // A function that apply the given `f` function to all the element of a list.
      def applyFunctionToEveryElements(list: List[Int])(f: Int => Int): List[Int] = list.map(f)

      val list = List(1, 2, 3)

      // Write the function `pow` using function as value.
      val pow: Int => Int = int => int * int

      check(applyFunctionToEveryElements(list)(pow) == List(1, 4, 9))
    }

    /**
     * A function can return another function. As an example, let's try
     * to create a function that will return a translator based on the
     * user language.
     */
    exercise("Returning a function") {
      // A function that apply the given `f` function to all the element of a list.
      enum Lang {
        case FR, EN
      }

      def translator(lang: Lang): String => String = {
        def translateFR(key: String): String =
          key match {
            case "hello" => "Bonjour le monde"
            case _       => "Inconnu"
          }

        def translateEN(key: String): String =
          key match {
            case "hello" => "Hello world"
            case _       => "Unknown"
          }

        lang match {
          case Lang.FR => translateFR
          case Lang.EN => translateEN
        }
      }

      val frTranslator = translator(Lang.FR)
      check(frTranslator("hello") == "Bonjour le monde")

      val enTranslator = translator(Lang.EN)
      check(enTranslator("hello") == "Hello world")
    }

    /**
     * Function as value allows us to compose functions chaining
     * functions with ease.
     */
    exercise("Composing functions") {

      /**
       * This is another way to write uppercase function using
       * {{{_.func}}} instead of {{{string => string.func}}}.
       */
      val uppercase: String => String = _.toUpperCase
      val chars: String => List[Char] = _.toList

      // Use `andThen` to chain the two functions.
      val both: String => List[Char] = uppercase andThen chars

      check(chars(uppercase("hello")) == both("hello"))
    }
  }
