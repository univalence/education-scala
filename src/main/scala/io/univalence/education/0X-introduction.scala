package io.univalence.education

import io.univalence.education.internal.exercise_tools.*
import io.univalence.education.internal.implicits.*

import scala.::
import scala.util.{Failure, Success, Try}

@main
def _00_introduction(): Unit = {
  section("PART 1 - Variables") {

    exercise("Immutable variable (aka val)", activated = true) {
      // todo: swap the ??? expression with an appropriate value so that the test passes
      val a: Int = 42
      check(a == 42)

      // todo: create a value so that the test passes
      check(??? == "hello")
    }

    /**
     * Unlike Java, the scala compiler is able to infer types, meaning
     * it can guess most types without having manually write it.
     *
     * However it's considered good practice to state most types
     * explicitly (especially public functions)
     */
    exercise("Type inference", activated = true) {
      // todo: https://media.giphy.com/media/104ueR8J1OPM2s/giphy-downsized-large.gif
      val a = 1
      check(a.isInstanceOf[Int])
    }

    exercise("Lazy val", activated = true) {
      lazy val uselessHeavyStuff = (1 to 1_000_000).filter(_ % 42 == 0).sum
      val a                      = 42
      check(a == 42)
    }

    /**
     * Scala comes with a String interpolation system, meaning that you
     * can add variables inside a string.
     */

    exercise("String interpolation", activated = true) {
      val c   = "n"
      val n   = "c"
      val str = s"le cuisinier se${c}oue les ${n}ouilles"
      check(str == "le cuisinier secoue les nouilles")
    }
  }

  section("PART 2 - Collections") {

    /**
     * Scala collections all inherit from the Traversable trait
     * therefore giving you the ability to use common useful functions
     * such as :
     *
     *   - length
     *   - isEmpty
     *   - contains
     *   - indexOf
     *
     * there are two catégories of collections in Scala
     *   - mutable collections also called buffers -> content can be
     *     modified
     *   - immutable collections -> content connot be modified
     *
     * the latter are most often used in Scala (and functional
     * programming in general)
     */

    exercise("FGI", activated = true) {

      /**
       * Go and have a look at "scala traversable type hierarchy" on
       * your favorite search engine in order to get a feel for scala
       * collections
       */
    }

    exercise("Create some basic lists", activated = true) {

      /**
       * lists are used when in need of an ordered collection. Scala
       * lists are linked lists
       */

      val l1 = List(1, 2, 3, 4, 5, 6)
      check(l1 == List(1) :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil)
    }

    exercise("Mutate list", activated = true) {

      /** Reminder: Scala list are immutable */

      val l1 = List(42)
      val l2 = l1 :+ 43
      check(l2 == List(42, 43))
    }

    exercise("other useful collections", activated = true) {

      /** Seq should be used as a general purpose collection */
      val seq = Seq(42, 43)

      /** Set should be used when */
      val set = Set(1, 1, 2, 2, 3, 3)

      /** Map are associative arrays */
      val map = Map(1 -> "one", 2 -> "two", 3 -> "three", 4 -> "viva l'algérie")

      check(seq == ??)
    }

    exercise("common collection operations", activated = true) {

      val list            = List(1, 2, 3, 4, 5, 6)
      val head            = list.head
      val tail            = list.tail
      val exists: Boolean = list.exists(_ < 1)

      check(head == 1)
      check(tail == List(2, 3, 4, 5, 6))
      check(!exists)
    }
  }

  section("PART 3 - Functions") {

    exercise("function without arguments", activated = true) {
      def sophism: Unit = println("the rooster crows, the sun rises therefore the sun is risen by the rooster")
      // todo:  run the function above
    }

    exercise("function with arg", activated = true) {
      val greeting = (name: String) => s"Hello there, $name"
      check(?? == "Hello there, general Kenobi")
    }

    exercise("anonymous function", activated = true) {

      /**
       * The function notation above "() => ???" is called anonymous
       * function. This concept is useful when passing a function as an
       * argument
       */

      val a = List(1, 2, 3, 4, 5, 6).map((elt: Int) => elt + 1)
      check(a == List(2, 3, 4, 5, 6, 7))
    }

    exercise("passing a function as argument", activated = true) {
      val a = List(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0)
      check(a == List(1, 3, 5))
    }

  }

  section("PART 4  - Control flow") {

    exercise("If else as a an expression", activated = true) {
      val x: Int             = 42
      val condition: Boolean = if (x == 42) true else false
      check(condition)
    }
  }

  section("PART 5 - Case class") {

    /**
     * Case classes are classes meant to hold values, their equivalent
     * in other languages would be "data classes" or "record" Scala
     * Cases classes come with several perks:
     *
     *   - the apply method allow you to create case classes without the
     *     "new" keyword
     *   - all parameters are val therefore easy to access (though
     *     immutable ;) )
     *   - toString, hashcode and equals are implemented by default
     */

    exercise("create a case class and an instance", activated = true) {
      case class Pokemon(name: String, id: Int)
      val bulbasaur = Pokemon("Bulbasaur", 1)
      // todo create a ditto that looks like a bulbasaur
      val ditto = Pokemon("Bulbasaur", 1)

      check(bulbasaur.id == 1)
      check(ditto == bulbasaur) // equality by value
    }
  }

  section("PART 6 - Functional data structures") {

    exercise("Function returning an Option", activated = true) {

      /**
       * The type Option represents optional values Optional values can
       * be one of the following two types:
       *   - None: represnting the absence of value.
       *   - Some: representing the presence of a value.
       */

      val a   = Some(42)
      val b   = None
      val map = Map("a" -> 42, "c" -> 43)

      check(map.get("a") == a)
      check(map.get("b") == b)

    }

    exercise("Function returning an Try", activated = true) {

      /**
       * The Try[A] type looks a bit like the Option type however the
       * value can be of the two following types:
       *   - Success(a): where a is of type A
       *   - Failure(err) where err is of type Throwable
       */

      val try1: Try[Double] = Try("42".toDouble)
      val try2: Try[Double] = Try("4 2".toDouble)

      val success = Success(42)
      val failure = Failure(NumberFormatException())

      check(try1 == success)
      check(try2 == failure)
    }

    exercise("Either") {

      /**
       * Once again here is a data structure that represents some kind
       * of duality. Either[L,R] can be of type:
       *   - Left(l) where the value is of type L
       *   - Right(r) where r is of type R
       */

      // you take the red pill, you stay in wonderland, and I show you how deep the rabbit hole goes

      type RedPill  = String
      type BluePill = Int

      def morpheus(choice: Boolean): Either[BluePill, RedPill] = if (choice) Right("truth") else Left(1010110100)
      check(morpheus(true) == Right("truth"))
    }

    exercise("For comprehesion", activated = true) {
      check(?? == ??)
    }

  }

  section("PART 7 - Collection transformations") {
    exercise("map") {}

    exercise("flatmap") {}

    exercise("filter") {}

    exercise("collect") {}

    exercise("sortBy") {}

    exercise("zipWithIndex") {}

    exercise("foreach") {}

    exercise("mkString") {}

    exercise("fold") {}

  }

  section("PART 8  - SBT") {
    exercise("add sbt dep", activated = true) {
      check(?? == ??)
    }
  }

}
