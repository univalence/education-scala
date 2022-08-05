package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.util.Random

import java.time.LocalDate

/**
 * =Immutability=
 * Immutability is one of the main rules that lead to a functional
 * program. It imposes that an initialized value should never be
 * replaced during its lifetime, forcing us to reinitialized another
 * value if we want to apply a transformation.
 *
 * Immutability has several advantages over mutability:
 *   - It allows us to keep the state in between and to recover it later
 *     on.
 *   - It facilitates the code refactoring since it lead us to
 *     referentially transparent code.
 *   - It makes concurrency code much easier to write.
 *   - For classes, it allows us to chain operation making the code more
 *     readable.
 *
 * However, immutability also has disadvantages:
 *   - By creating a copy for each operations, it is less optimized than
 *     mutability.
 *   - It is easier to create memory leaks (however thanks to the
 *     garbage collector in Java, this is not an issue for us).
 */
@main
def immutability(): Unit =
  section("The basis of immutability") {

    /**
     * Scala is a language that can be used to write both functional and
     * imperative program. Even if it is totally possible to write a
     * codebase that use both paradigms, we tend to always write fully
     * purely and functional programs.
     *
     * It means that some features of Scala will never (or almost never)
     * be used by developers. The keywords `var` and `while` are
     * examples. We prefer to use `val`, commonly named `const` in other
     * languages for `var` and recursion instead of `while`.
     *
     * Fun fact: When people make imperative programs using Scala, we
     * say that they write Scava programs (contraction of Scala and
     * Java).
     */
    exercise("Differentiate a value and a variable") {
      var variable = 10
      val value    = 10

      variable = 20
      // constant = 20
      val newValue = 20

      check(variable != value)
      check(variable == newValue)
    }
  }

  /**
   * Immutability is interesting with classes since we can chain
   * operations. Let's try to transform a mutable class containing an
   * int into a immutable one.
   */
  exercise("Make a class immutable") {
    class Mutable(var int: Int) {
      self =>
      def add(amount: Int): Unit = self.int = self.int + amount
    }

    val mutable = new Mutable(10)
    mutable.add(10)
    check(mutable.int == 20)

    /**
     * We add the keyword `case` here. It creates a `case class` instead
     * of a `class`. A case class is a class with several universal
     * function.
     *
     * As an example, it contains the function `apply` that allows us to
     * write:
     *   - val immutable = Immutable(10)
     *
     * Instead of:
     *   - val immutable = new Immutable(10)
     */
    case class Immutable(int: Int) {
      def add(amount: Int): Immutable = Immutable(int + amount)
    }

    val immutable = Immutable(10).add(10)
    check(immutable == Immutable(20))
  }

  /**
   * This way of updating an existing case class don't scale. Imagine a
   * case class with many parameters. If you want to create a function
   * that only update one of them, you will have to give to the new
   * instance all the parameters even if they didn't change.
   */
  exercise("Handle case class with many arguments") {
    case class Person(
        firstName: String,
        lastName:  String,
        age:       Int,
        length:    Int,
        birthdate: LocalDate,
        phone:     String,
        job:       Option[String]
    ) {

      /**
       * For example, the birthday function should increase the age by
       * one. However since we need to create a new Person due to
       * immutability, we have to give it all the parameters even the
       * unchanged ones.
       *
       * It has another issue. Imagine you have several functions like
       * birthday and you want to add another parameter to your case
       * class, you will have to update every functions that create a
       * new Person accordingly.
       *
       * Thanks to Martin Odersky, case class has another cool function
       * that will help us, `copy`. This function allows us to only give
       * the changing parameters and it will create the correct Person
       * accordingly.
       */
      def birthday: Person =
        // Person(
        //   firstName,
        //   lastName,
        //   age + 1,
        //   length,
        //   birthdate,
        //   phone,
        //   job
        // )

        copy(age = age + 1)

      /** Use copy to apply a new job to the person. */
      def promotion(newJob: String): Person = copy(job = Some(newJob))
    }

    val lea = Person("Fiona", "Kerhs", 24, 183, LocalDate.of(1997, 12, 24), "+33632132145", Some("Data Engineer"))

    check(lea.birthday.age == lea.age + 1)
    check(lea.promotion("Tech lead").job.contains("Tech lead"))
  }

  /**
   * It is almost always possible to replace a mutable value by an
   * immutable one.
   */
  section("Replace mutable variable by immutable value") {

    /**
     * A good way to train to immutability is to try to replace a
     * mutable counter by an immutable one.
     *
     * Tips: The `immutableF` function's definition may be not correct.
     */
    exercise("Use immutable value to make a counter") {
      Random.setSeed(1)

      var mutableCount = 0

      @tailrec
      def mutableF(): Unit = {
        mutableCount = mutableCount + 1
        if (Random.nextBoolean()) mutableF() else ()
      }

      mutableF()

      Random.setSeed(1)

      @tailrec
      def immutableF(count: Int): Int = if (Random.nextBoolean()) immutableF(count + 1) else count + 1

      val immutableCount = immutableF(0)

      check(mutableCount == immutableCount)
    }

    /**
     * At the beginning of this immutability journey, we said that
     * immutability has one big disadvantage, performance. Generally
     * speaking, this is not an issue for programmers.
     *
     * However, since everybody use massively the Scala standard
     * library, its functions are implemented using imperative code.
     *
     * Even if their implementations are mutable, we still say that
     * these functions are immutable because the mutability is scoped
     * inside the function. Functions are still pure and referential
     * transparent.
     *
     * It is for performance reason and not because it is not possible.
     *
     * To prove it, let's transform the list's foldLeft function body
     * using only immutability.
     *
     * FoldLeft is a very important list transformation, it allows us to
     * generalize the concept of accumulation.
     *
     * Examples:
     *
     * The sum of the elements of a list can be written as follow:
     *
     * {{{
     * val sum = List(1, 2, 3).foldLeft(0)(_ + _)
     * }}}
     *
     * The product of the elements of a list can be written as follow:
     *
     * {{{
     * val product = List(1, 2, 3).foldLeft(1)(_ * _)
     * }}}
     *
     * You can see the implementation of this function in IntelliJ:
     *   - In mac using `Command` + `Left click`
     *
     * This is the implementation of the foldLeft function in the
     * standard library:
     *
     * {{{
     * def foldLeft[B](z: B)(op: (B, A) => B): B = {
     *   var acc                 = z
     *   var these: LinearSeq[A] = coll
     *   while (!these.isEmpty) {
     *     acc   = op(acc, these.head)
     *     these = these.tail
     *   }
     *   acc
     * }
     * }}}
     */
    exercise("Make foldLeft operation of list immutable") {
      val list = List(1, 3, 4)

      def immutableFoldLeft[A, B](list: List[A])(default: B)(f: (A, B) => B): B =
        list match {
          case head :: next => f(head, immutableFoldLeft(next)(default)(f))
          case Nil          => default
        }

      check(list.foldLeft(0)(_ + _) == immutableFoldLeft(list)(0)(_ + _))
    }
  }
