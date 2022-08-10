package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.util.Random

import java.time.LocalDate

/**
 * =Referential transparency=
 *
 * Referential transparency is one of the most important aspect of
 * functional programming. It means that at any moment, an expression
 * can be replaced by its value and vice versa.
 *
 * This aspect has several advantages:
 *   - It allows to to refactor the code easily
 *   - It allows to make the code predictable
 */
@main
def referential_transparency(): Unit =
  section("The basis of referential transparency") {

    exercise("Example of non referential transparency") {
      val random                     = Random.nextInt()
      val randoms                    = List(random, random, random)
      val randomsValuesByExpressions = List(Random.nextInt(), Random.nextInt(), Random.nextInt())

      // How many unique numbers are generated ?
      check(randoms.toSet.size == 1)

      // How many unique numbers are generated ?
      check(randomsValuesByExpressions.toSet.size == 3)
    }

    /**
     * This kind of result make the code unpredictable and increase the
     * chance of inserting a bug.
     *
     * It generally happens when you manipulate impure code (code with
     * side effects). You can reproduce the same issue using `println`
     * for example.
     *
     * To avoid this issue, you have to manipulate a function instead of
     * a value. Indeed, the value is evaluated one time then reused when
     * needed. However, a function is computed each time it is called.
     */
    exercise("Example of referential transparency") {
      def random                     = Random.nextInt()
      val randoms                    = List(random, random, random)
      val randomsValuesByExpressions = List(Random.nextInt(), Random.nextInt(), Random.nextInt())

      // How many unique numbers are generated ?
      check(randoms.toSet.size == 3)

      // How many unique numbers are generated ?
      check(randomsValuesByExpressions.toSet.size == 3)
    }
  }
