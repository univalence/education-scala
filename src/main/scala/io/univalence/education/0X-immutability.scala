package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.util.Random

import java.time.LocalDate

/**
 * =Immutability=
 * Immutability is one of the main rules that leads to functional
 * programming. It imposes that an initialized value should never be
 * replaced during its lifetime, forcing us to initialize another value,
 * if we want to apply a transformation.
 *
 * Immutability has several advantages over mutability:
 *   - It allows us to keep the intermediate states and to recover it
 *     later on.
 *   - It facilitates the code refactoring, since it lead us to
 *     referentially transparent code.
 *   - It makes concurrency code much easier to write, as nothing
 *     changes.
 *   - For classes, it allows us to chain operations making the code
 *     more readable (pattern: Fluent Interface).
 *
 * However, immutability also has disadvantages:
 *   - By creating a copy for each operations, it is less optimized than
 *     mutability.
 *   - It is easier to create memory leaks (however thanks to the Java
 *     garbage collector, this is not an issue for us).
 */
@main
def immutability(): Unit =
  section("The basis of immutability") {

    /**
     * Scala is a language that can be used to write both functional and
     * imperative code. Even if it is totally possible to write a
     * codebase that use both paradigms, we tend to always write fully
     * pure functional code.
     *
     * It means that some features of Scala will never (or almost never)
     * be used by developers. The keywords `var` and `while` are
     * examples to avoid. We prefer to use `val`, used to declare what
     * would `const` or `final` in other languages, and recursion
     * instead of `while`.
     *
     * Fun fact: When people make imperative programs using Scala, we
     * say that they write Scava programs (contraction of Scala and
     * Java).
     */
    exercise("Differentiate a value and a variable") {
      var variable = 10
      val value    = 10

      variable += 10
      // value += 10
      val newValue = value + 10

      check(variable != value)
      check(variable == newValue)
    }
  }

  /**
   * Immutability is interesting with classes since we can chain
   * operations. Let's try to transform a mutable class containing an
   * `int` into a immutable one.
   */
  exercise("Make a class immutable") {
    class Mutable(var int: Int) { self =>
      def add(amount: Int): Unit = self.int = self.int + amount
    }

    val mutable1 = Mutable(10)
    val mutable2 = mutable1
    mutable2.add(10)
    mutable2.add(20)

    check(mutable1.int == 40)
    check(mutable2.int == 40)

    /**
     * We add the keyword `case` here. A case class is a class with
     * useful functions.
     *
     * Another feature of case class is that it implies that all
     * parameters are value by default.
     */
    case class Immutable(int: Int) {
      def add(amount: Int): Immutable = Immutable(int + amount)
    }

    val immutable1 = Immutable(10)
    val immutable2 =
      immutable1
        .add(10)
        .add(20)

    check(immutable1 == Immutable(10))
    check(immutable2 == Immutable(40))
  }

  /**
   * This way of updating an existing case class don't scale. Imagine a
   * case class with many parameters. If you want to create a function
   * that only updates one of them, you will have to give the new
   * instance every parameter even if you don't need to change them.
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
       * increaseAge and you want to add another parameter to your case
       * class, you will have to update every function that create a new
       * Person accordingly.
       *
       * Thanks to Martin Odersky, case class has another cool function
       * that will help us, `copy`. This function allows us to only give
       * the changing parameters and it will create the correct Person
       * accordingly.
       */
      def increaseAge: Person =
        // Person(
        //   firstName,
        //   lastName,
        //   age + 1,
        //   height,
        //   birthdate,
        //   phone,
        //   job
        // )

        copy(age = age + 1)

      /** Use copy to apply a new job to the person. */
      def changeJob(newJob: String): Person = copy(job = Some(newJob))
    }

    val fiona = Person("Fiona", "Kerhs", 24, 183, LocalDate.of(1997, 12, 24), "+33632132145", Some("Data Engineer"))

    val grownUpFiona = fiona.increaseAge
    check(grownUpFiona.age == fiona.age + 1)

    val promotedFiona = fiona.changeJob("Tech lead")
    check(promotedFiona.job.contains("Tech lead"))
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
     * immutability has one big disadvantage, which is performance.
     * Generally speaking, this is not an issue for programmers.
     *
     * However, since everybody massively uses the Scala standard
     * library, its functions are implemented using imperative code.
     *
     * Even if their implementations are mutable, we still say that
     * these functions are immutable, because mutability is scoped
     * inside the function and is not noticeable outside. Functions are
     * still pure and referentially transparent. This is for performance
     * reason and not because it is not possible.
     *
     * To prove it, let's transform the list's foldLeft function body
     * only using immutability. FoldLeft is a very important list
     * transformation function, it allows us to generalize the concept
     * of aggregation.
     *
     * Examples: The sum of the elements of a list can be written as
     * follow:
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
     *   - On macOS, using `Command` + `Left click`
     *   - On other platform, using `Ctrl` + `Left click`
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
