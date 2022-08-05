package io.univalence.education

import io.univalence.education.internal.exercise_tools.*
import io.univalence.education.internal.implicits.*

import scala.annotation.tailrec
import scala.util.Random

import java.time.LocalDate

/**
 * Scala comes with a standard library that implements commonly used
 * codes.
 *
 * One of the most important part of the standard library are their
 * immutable data structures. We can find them in the package
 * `scala.collection.immutable`.
 *
 * You will certainly use some of them during your Scala journey. Thus
 * it is interesting to practice a bit with them!
 */
@main
def standard_library(): Unit =
  /**
   * List[A] is by far the most important data structure available in
   * the Scala standard library. It represents a list of elements. It is
   * similar to a Linked List available in many other programming
   * languages.
   *
   * In Scala, this data structure comes with plenty of helping
   * functions which make your life easier.
   *
   * Let's see some of them.
   */
  section("Practicing with List[A]") {
    val numbers: List[Int] = List(1, 2, 3, 4, 5)

    exercise("Find the size of a list") {
      val numberOfNumbers: Int = numbers.length

      check(numberOfNumbers == 5)
    }

    exercise("Check if an element exists or not") {
      val contains3: Boolean = numbers.contains(3)
      val contains6: Boolean = numbers.contains(6)

      check(contains3)
      check(!contains6)
    }

    exercise("Append (at the end of the list) the number 6") {
      val numbersThen6: List[Int] = numbers :+ 6

      check(numbersThen6 == List(1, 2, 3, 4, 5, 6))
    }

    exercise("Prepend (at the beginning of the list) the number 0") {
      val numbersAfter0: List[Int] = 0 +: numbers

      check(numbersAfter0 == List(0, 1, 2, 3, 4, 5))
    }

    /**
     * The map function is one of the most used one, it allows us to
     * easily update all elements of a list applying the same function
     * to all of them.
     */
    exercise("Transform all elements of a list using `map`") {
      val numbersTimes2: List[Int] = numbers.map(_ * 2)

      check(numbersTimes2 == List(2, 4, 6, 8, 10))
    }

    /**
     * Tips: You can use modulo to assert if a number is even or odd.
     */
    exercise("Filter all odd elements of a list using `filter`") {
      val evenNumbers: List[Int] = numbers.filter(_ % 2 == 0)

      check(evenNumbers == List(2, 4))
    }
  }

  /**
   * In Scala, you should not use the keyword `null` to specify than a
   * value can be undefined. Often, it leads to unexpected error in your
   * code because it is invisible and unpredictable. TODO the creator of
   * null call it the billion dollars problem.
   *
   * Instead, in Scala, you should use Option[A]. It is a data structure
   * that can be represented in two ways.
   *
   * It can be either:
   *   - None meaning that there is no value associated with the option.
   *   - Some(a) meaning that there is a value called `a` associated
   *     with the option.
   *
   * The main advantages compare to null is the compiler forcing you to
   * handle the case when the may not have a value.
   *
   * An Option[A] contains a value of type A. As an example, an
   * Option[Int] may contain an Int ot Nothing.
   */
  section("Practicing with Option[A]") {
    exercise("Create an option with no value") {
      val none: Option[Int] = None

      check(none.isEmpty)
    }

    exercise("Create an option with a value of 10") {
      val some: Option[Int] = Some(10)

      check(some.isDefined)
    }

    exercise("Modify the value inside the Option") {
      val some: Option[Int]        = Some(10)
      val updatedSome: Option[Int] = some.map(_ + 10)

      check(updatedSome.contains(20))
    }
  }

  section("Practicing with Either[A, E]") {}

  section("Complementarities, similarities and monads") {}
