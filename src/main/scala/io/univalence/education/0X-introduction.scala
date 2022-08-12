package io.univalence.education

import zio.*

import io.univalence.education.internal.exercise_tools.*
import io.univalence.education.internal.implicits.*

import scala.::
import scala.collection.SortedSet
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
      var x = 0
      lazy val effect = {
        x += 1
        42
      }
      check(x == ??)
      check(effect == 42)
      check(x == ??)
      check(effect == 42)
      check(x == ??)
    }

    /**
     * Scala comes with a String interpolation system, meaning that you
     * can add variables inside a string.
     */

    exercise("String interpolation", activated = true) {
      val gl  = "p"
      val p   = "gl"
      val str = s"{$gl}isser dans la ${p}iscine"
      check(str == "glisser dans la piscine")
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
      check(l1 == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil)
    }

    exercise("Mutate list", activated = true) {

      /** Reminder: Scala list are immutable */

      val l1 = List(42)
      val l2 = l1 :+ 43
      check(l1 == List(42))
      check(l2 == List(42, 43))
    }

    exercise("other useful collections", activated = true) {

      /** Seq should be used as a general purpose collection */
      val seqWithDuplicates: Seq[Int] = Seq(2, 3, 1, 2, 3, 1)

      /** Set should be used when */
      // todo : transform seqWithDuplicates into a Set
      val set: Set[Int] = seqWithDuplicates.toSet

      /** Map are associative arrays */
      val l                     = List("one", "two", "three")
      val sortedSet             = SortedSet.from(set)
      val map: Map[String, Int] = l.zip(sortedSet).toMap

      check(seqWithDuplicates.size > set.size)
      check(sortedSet == Set(1, 2, 3))
      check(map == Map("one" -> 1, "two" -> 2, "three" -> 3))
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
      var x = 0
      def effect = {
        x += 1
        42
      }
      // todo: run the effect above in order to make the test pass
      check(x == 1)
      check(effect == 42)
    }

    /**
     * There are several ways to write the same function in Scala
     *
     * Here are a few examples
     */

    def plusOne(n: Int)       = n + 1
    val addOne                = (n: Int) => n + 1
    val increment: Int => Int = n => n + 1
    val increase: Int => Int  = _ + 1

    /**
     * you don't even have to type some anonymous function when provided
     * sufficient amount of contextual info
     *
     * for instance:
     */

    val incrementList = (list: List[Int]) => list.map(elt => elt + 1)

    exercise("function with arg", activated = true) {
      val greeting: String => String = ???
      check(greeting("general Kenobi") == "Hello there, general Kenobi")
    }

    exercise("passing a function as argument", activated = true) {
      val a = List(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0)
      check(a == List(1, 3, 5))
    }

  }

  section("PART 4  - Control flow") {

    /** Scala if statements can be passed as a value. */

    val x: Int            = 42
    val condition: String = if (x == 42) "forty two" else "some other number"
    check(condition == "forty two")

    exercise("If else as a an expression", activated = true) {
      val x: Int = 42

      // todo : use an if statement inside a string interpolation
      // reminder: string interpolation -> s"hello ${someVal}"

      val condition: String = s"x is an ${if (x % 2 == 0) "even" else "odd"} number"
      check(condition == "x is an even number")
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

      /** todo: create a new Pokemon named ditto with an id of 132 */
      val ditto = Pokemon("Ditto", 132)

      /**
       * Ditto is a pokemon with a special attack called `Transform`
       * giving it the ability to change into an enemy
       *
       * Let's use transform on bulbasaur
       */

      val transformedDitto = ditto.copy(name = "Bulbasaur")

      // todo : Let's finish the transformation by giving ditto bulbasaur's id

      val fullyTransformedDitto = ???

      // the following test should pass as case classes are compared by value
      check(fullyTransformedDitto == bulbasaur)

      check(fullyTransformedDitto.toString == "Pokemon(Bulbasaur,1)")

    }

    exercise("create your own cae class", activated = true) {

      /**
       * todo : create a case class Student, its attributes will be:
       *   - name of type String
       *   - grades of type Seq[Int]
       *   - isHardWorking of type Boolean with a default value of
       *     `true`
       */

      ???

      // todo: create an instance of Student in a way that passes the test

      ???

      // val student = Student("jack", List(1,2,3))
      // check(student.isInstanceOf[Student])
      // check(student.isHardWorking == true)
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
       * Once again, here is a data structure that represents some kind
       * of duality. Either[L,R] can be of type:
       *   - Left(l) where the value is of type L
       *   - Right(r) where r is of type R
       */

      // you take the red pill, you stay in wonderland, and I show you how deep the rabbit hole goes

      type RedPill  = String
      type BluePill = Int

      // remember, i'm offering you the truth, nothing more

      def morpheus(choice: Boolean): Either[BluePill, RedPill] = if (choice) Right("truth") else Left(1010110100)
      check(morpheus(true) == Right("truth"))
    }

    exercise("For comprehesion", activated = true) {

      /**
       * a "for comprehension" is a way to sequentially transform values
       * (called closures)
       */

      val option1 = Some(42)
      val option2 = Some(8)

      val sumOption =
        for {
          fortyTwo <- option1
          eight    <- option2
        } yield fortyTwo + eight

    }
  }

  section("PART 8  - SBT") {
    exercise("add sbt dep", activated = true) {

      /**
       * Chances are that you'll need some libraries at some point.
       * Let's install one among the best of them all: ZIO
       *
       * First go and read this doc: https://zio.dev/getting_started
       *
       * You'll find `build.sbt` at the root of this project
       *
       * Reload the project by clicking on the reload button that should
       * appear at the top right corner of your screen
       *
       * At the top of this file, import zio like so: `import zio._`
       *
       * Then uncomment the following lines and see if the test passes
       */

      // val zioTest: UIO[Unit] = ZIO.unit
      // check(zioTest.isInstanceOf[UIO[Unit]])
    }
  }

  section("PART 9  - Pattern matching") {
    exercise("???", activated = true) {
      ??

    }
  }

}
