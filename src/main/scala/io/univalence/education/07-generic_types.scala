package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec

/**
 * =Generic type=
 *
 * With FP, generic types (or parametric types) are powerful tools for
 * developers, especially because they come with almost standardize
 * operations.
 *
 * With have already some of those types in the previous files, like
 * [[List]], [[Option]], [[Map]]... In this file, we will create or
 * recreate some of those types and take some time to think about their
 * operations.
 *
 * ==IMPORTANT==
 * Manipulating generic types implies an higher level in terms of
 * reasoning.
 *
 * If you are feeling lost in those exercises, just focus your mind on
 * the types you have to deal with. There is a really strong probability
 * that if the type of your expression is correct, then your
 * implementation is correct.
 *
 * So, only check the elements that the code context provides to solve
 * your exercises by using elements which types match each other.
 */

@main
def _07_generic_types(): Unit = {

  /**
   * A box is just a generic container for exactly one value.
   *
   * @param value
   *   the value inside the box.
   * @tparam A
   *   the type of the value.
   */
  case class Box[+A](value: A)

  section("Living in a box") {

    exercise("map a box") {

      /**
       * With the extension syntax, we add the `map` function to the Box
       * type. This addition is only effective in the current exercise.
       * It is not available outside.
       *
       * The `map` operation aims to modify the value inside the box.
       */
      extension [A](box: Box[A]) def map[B](f: A => B): Box[B] = |>?

      check(Box(42).map(_ * 2) == Box(84))
      check(Box("42").map(_.toInt) == Box(42))
    }

    exercise("flatMap a box") {

      /**
       * The `flatMap` operation is almost similar to `map`, except that
       * it can be used to chain operations using the value inside boxes.
       */
      extension [A](box: Box[A]) def flatMap[B](f: A => Box[B]): Box[B] = |>?

      check(Box(12).flatMap(a => Box(13).flatMap(b => Box(a + b))) == Box(25))

      /**
       * If you have defined the operations `map` and `flatMap`, you can
       * use the Box type inside a for-comprehension.
       */

      // TODO write map again by using flatMap
      extension [A](box: Box[A]) def map[B](f: A => B): Box[B] = |>?

      /**
       * The for-comprehension below is equivalent to
       *
       * {{{
       *   Box("Jon").flatMap(a => Box("Mary").map(b => (a, b)))
       * }}}
       */
      check(
        (for {
          a <- Box("Jon")
          b <- Box("Mary")
        } yield (a, b)) == Box(("Jon", "Mary"))
      )
    }

  }

  /**
   * We will now recreate a List type, as a linked list.
   *
   * It is an enum composed of:
   *   - [[NothingMore]]: the end of the list
   *   - [[Cell]]: a unit container in the list
   *
   * Note: pattern matching might help you in the exercises below.
   */
  enum MyList[+A]:
    case NothingMore
    case Cell(head: A, tail: MyList[A])

  /**
   * Below are some utility functions to help you with [[MyList]] type.
   */
  object MyList:
    import MyList.*

    /**
     * Create an empty list.
     *
     * @tparam A
     *   type of the empty list.
     */
    def empty[A]: MyList[A] = NothingMore

    /**
     * Helps you to create a list in a more readable way.
     *
     * With this function, we have this equivalence:
     * {{{
     *   MyList(1, 2, 3) == Cell(1, Cell(2, Cell(3, NothingMore)))
     * }}}
     *
     * Reminder: all explicit call to a function named `apply` can be
     * omitted.
     *
     * @param values
     *   this parameter is a vararg, meaning that `values` matches a
     *   series of parameter. `values` is seen as an `Array[A]`.
     */
    def apply[A](values: A*): MyList[A] = values.foldRight(empty[A])((value, l) => Cell(value, l))

  section("My list") {
    import MyList.*

    exercise("map") {

      /**
       * There, `map` transforms the element of the list one by one. But
       * it should not change the size of the list.
       */
      extension [A](l: MyList[A]) def map[B](f: A => B): MyList[B] = |>?

      check(MyList(1, 2, 3).map(_ + 1) == MyList(2, 3, 4))
    }

    exercise("flatMap") {

      /**
       * There, `flatMap` is like `map`, ie. transforms the element of
       * the list one by one. But it can change the size of the list
       * according to `f`.
       *
       * Notice that when you combine flatMap operations on lists, they
       * act as a cartesian product between those lists.
       */
      extension [A](l: MyList[A]) def flatMap[B](f: A => MyList[B]): MyList[B] = |>?

      check(
        MyList(1, 2).flatMap(a => MyList(4, 5).flatMap(b => MyList((a, b)))) == MyList((1, 4), (1, 5), (2, 4), (2, 5))
      )

      // TODO write map again by using flatMap
      extension [A](l: MyList[A]) def map[B](f: A => B): MyList[B] = |>?

      check(
        (for {
          greeting <- MyList("Good morning", "Good afternoon")
          name     <- MyList("Jon", "Mary")
        } yield s"$greeting $name!")
          ==
            MyList("Good morning Jon", "Good morning Mary", "Good afternoon Jon", "Good afternoon Mary")
      )

      check(MyList(1, 2, 3).flatMap(a => MyList.empty[Int].map(b => a + b)) == ??)
    }

    exercise("filter") {
      extension [A](l: MyList[A]) def filter(f: A => Boolean): MyList[A] = |>?

      check(MyList(1, 2, 3, 4, 5).filter(_ % 2 == 0) == MyList(2, 4))
    }

    exercise("fold") {
      extension [A](l: MyList[A]) def foldLeft[B](init: B)(f: (B, A) => B): B = |>?

      check(MyList(1, 2, 3).foldLeft(0)(_ + _) == 6)
      check(MyList("a", "b", "c").foldLeft(MyList.empty[String])((l, value) => Cell(value, l)) == MyList("c", "b", "a"))
    }
  }

  /**
   * Till there, we have seen covariant generic types, ie. they accept
   * items of a type or of its subtypes (eg. a list of animals can
   * contains dogs and cats).
   *
   * Now we will see a contravariant generic types, ie. they accept
   * items of a type or of its super-types. This has consequences on
   * their operations.
   *
   * The type [[Printer]] below converts values of a given type into a
   * string. This type is contravariant (see the sign `-` in the type
   * declaration).
   */
  case class Printer[-A]() {
    def print(value: A): String = value.toString
  }

  section("Contravariant type") {

    exercise("contramap") {

      /**
       * Contravariant generic types have a contravariant version of the
       * operation `map`.
       *
       * If you look closely to signature of the function `contramap`
       * below, you will see that it is very similar `map` operation
       * seen before. But it has a small difference that might sound
       * unintuitive: the signature of the parameter `f` sounds
       * reversed. Why is that?
       *
       * You want to print integer values, but you only have a printer
       * of string values. To have your printer of integers, you only
       * have to convert those integers into a string and then use the
       * printer of strings. `contramap` allows you to do this.
       *
       * TODO implement contramap
       */
      extension [A](p: Printer[A]) def contramap[B](f: B => A): Printer[B] = |>?

      val stringPrinter: Printer[String] = Printer()
      val intPrinter: Printer[Int]       = stringPrinter.contramap(_.toString)

      check(intPrinter.print(42) == "42")

      /**
       * Here is another example, where we only want to display the name
       * of a user.
       */

      case class PersonName(firstName: String, lastName: String)
      case class User(id: Int, login: String, name: PersonName)

      val personNamePrinter: Printer[PersonName] =
        stringPrinter.contramap(person => s"${person.firstName} ${person.lastName}")

      // TODO use personNamePrinter and contramap to create a printer that display the name of a user
      val userNamePrinter: Printer[User] = |>?

      val user = User(id = 1, login = "jon", name = PersonName(firstName = "Jon", lastName = "Doe"))
      check(userNamePrinter.print(user) == "Jon Doe")
    }

  }
}
