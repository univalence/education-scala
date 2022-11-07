package io.univalence.education

import io.univalence.education.internal.exercise_tools.*
import io.univalence.education.internal.implicits.*

import scala.annotation.tailrec
import scala.collection.SortedSet
import scala.util.{Failure, Success, Try}

@main
def _02_introduction(): Unit = {

  /**
   * =Variables=
   * Scala proposes 3 kinds of variable declaration. And there is one
   * more that can be assimilated to a variable declaration.
   *
   * NOTE: now on, every time you see `??`, `|>?`, or a `TODO`, it is a
   * placeholder for you to put your answer. Same if a test fails.
   */
  section("PART 1 - Variables") {

    /**
     * ==Simple (immutable) variable declaration: `val`==
     *
     * The first kind of variable declaration is the one recommended in
     * Scala. It declares an '''immutable''' variable (ie. you cannot
     * modify the variable value, once it has been initialized).
     *
     * This declaration is close to the declaration of constant in other
     * programming languages, or to `final` variables in Java.
     *
     * '''Immutability''' helps you to reason more easily about your
     * program, as it comes with no side effect, and therefore with less
     * surprises.
     *
     * So if you have to declare a variable think about using `val`
     * first.
     */
    exercise("Immutable variable (aka val)") {
      val a: Int = 42

      /**
       * You can use the '''type inference''' mechanism. It is a
       * mechanism where the compiler "guess" the type of your variables
       * from the type of their assigned expression.
       *
       * In the line below, you let Scala "guess" the type of your
       * variable according to the result of the expression. In the
       * example below, Scala will guess that the variable `b` is of
       * type `Int`.
       */
      val b = 8

      check(a + b == ??)

      // todo: create a value so that the test passes
      check(?? == "hello")
    }

    /**
     * ==Mutable variable declaration: `var`==
     *
     * Scala allows the declaration of mutable variable, with the `var`
     * keyword.
     *
     * BEWARE!!! Mutable variables used to be considered harmful by the
     * Scala community and even by the functional programming community.
     * Use it, if there is no other way or for optimization purpose
     * (even there, do not forget D. Knuth's advice: "Premature
     * optimization is the root of all evil."
     */
    exercise("Mutable variable (aka var)") {
      var a: Int = 42
      a = 3
      a += 8

      check(a == ??)
    }

    /**
     * Unlike Java, the scala compiler is able to infer types, meaning
     * it can guess most types without having manually write it.
     *
     * However it's considered good practice to state most types
     * explicitly (especially public functions)
     */
    exercise("Type inference") {
      val a = ??

      check(a.isInstanceOf[Int])

      /**
       * BEWARE!!!
       *
       * To let the type inference mechanism determine the type of your
       * variable is a good thing, when you have to assign almost simple
       * expressions. But with complex expressions, you take the risk to
       * get confused when determining the exact type of a variable.
       *
       * In case of complex, it is better to explicitly annotate the
       * type in the variable declaration. Also, the difference between
       * simple and complex here depends on the team your are working
       * in. It is obvious that `"hello"` or `1 + 1` are simples. But,
       * how do you or how does your team categorize `Map("key1" ->
       * 42)`?
       */
    }

    /**
     * ==lazy val==
     *
     * There is another kind of variable declaration: variable with a
     * lazy initialization. In this case, the variable is initialized
     * when its content is required, and not necessarily during the
     * declaration.
     */
    exercise("Lazy val") {
      var x = 0

      lazy val effect: Int = {
        x += 1
        42
      }

      check(x == ??)
      check(effect == ??)
      check(x == ??)
      check(effect == ??)
      check(x == ??)
    }

    /**
     * ==Parameterless function==
     *
     * This one is not really a variable declaration, but rather a
     * function declaration. Nevertheless, it can be seen as a dual of
     * the lazy val declaration (ie. when the variable is used many
     * times, the associated expression is not evaluated).
     *
     * Here with the parameterless function, we have a declaration close
     * to the lazy val declaration, as the associated value is computed
     * only when the function is called (and not before). But, every
     * time the function is called, the associated expression is
     * reevaluated.
     */
    exercise("Parameterless function") {
      var x = 1

      def g: Int = {
        x += 1
        42
      }

      check(x == 1)
      check(g == 42)
      check(x == 2)
      check(g == 42)
      check(x == 3)
    }

    /**
     * Scala comes with a String interpolation system, meaning that you
     * can reference variables inside a string.
     */
    exercise("String interpolation") {
      val br  = "ch"
      val ch  = "br"
      val str = s"Le ${br}as sur la ${ch}aise"

      check(str == ??)
    }

  }

  section("PART 2 - Collections") {

    /**
     * Scala collections inherit from the Traversable trait therefore
     * giving you the ability to use common useful functions such as:
     *   - length
     *   - isEmpty
     *   - contains
     *   - indexOf
     *
     * There are two catégories of collections in Scala:
     *   - immutable collections -> content cannot be modified
     *   - mutable collections also called buffers -> content can be
     *     modified
     *
     * The first ones are most often used in Scala (and functional
     * programming in general).
     */

    exercise("Create some basic lists") {

      /**
       * Lists are used when in need of an ordered collection. Scala
       * lists are linked lists.
       */
      val l1: List[Int] = |>?

      check(l1 == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil)
    }

    exercise("Mutate list") {

      /** ... But remember: Scala list are immutable! */

      val l1 = List(42)
      val l2 = |>?

      check(l1 == List(42))
      check(l2 == List(42, 43))
    }

    exercise("other useful collections") {

      /**
       * Seq is like list and should be used as a general purpose
       * collection.
       */
      val seqWithDuplicates: Seq[Int] = Seq(2, 3, 1, 2, 3, 1)

      /**
       * Set should be used when you want to remove duplicated items.
       * But those items are not indexed and are not ordered.
       */
      // TODO transform seqWithDuplicates into a Set
      val set: Set[Int] = |>?

      check(seqWithDuplicates.size > set.size)

      /**
       * [[Map]] are associative arrays (like a dictionary in Python).
       *
       * The [[SortedSet]] below is like a set, except its items are
       * ordered. This structure used to be implemented by using a
       * balanced binary tree (eg. AVL, red-black tree).
       *
       * The `zip` operation below associated two collections by
       * grouping in a tuple items with the same index.
       */
      val l                     = List("one", "two", "three")
      val sortedSet             = SortedSet.from(set)
      val map: Map[String, Int] = l.zip(sortedSet).toMap

      check(sortedSet == ??)
      check(map == ??)
    }

    exercise("Common collection operations") {

      val list            = List(1, 2, 3, 4, 5, 6)
      val head            = list.head
      val tail            = list.tail
      val exists: Boolean = list.exists(_ < 1)

      check(head == ??)
      check(tail == ??)
      check(exists == ??)
    }
  }

  section("PART 3 - Functions") {

    exercise("Declaration of a function") {

      /**
       * There are several ways to write the same function in Scala.
       *
       * The five declarations below represent the same function.
       */

      def plusOne(n: Int): Int = n + 1

      val addOne: Int => Int    = (n: Int) => n + 1
      val increment: Int => Int = n => n + 1
      val increase: Int => Int  = _ + 1
      object oneUp {
        def apply(n: Int): Int = n + 1
      }

      check(plusOne(42) == ??)
      check(addOne(42) == ??)
      check(increment(42) == ??)
      check(increase(42) == ??)

      /**
       * Yes, `oneUp` also declares a kind of function, knowing that
       * `oneUp.apply(42)` can be reduced into `oneUp(42)`.
       */
      check(oneUp(42) == ??)
      check(oneUp.apply(42) == ??)

      /**
       * You do not even have to declare the type of some anonymous
       * functions, when provided sufficient amount of contextual info.
       *
       * for instance, below, you do not have to declare the type of the
       * function inside `map`.
       */

      val incrementList: List[Int] => List[Int] = (list: List[Int]) => list.map(elt => elt + 1)

      check(incrementList(List(42, 24)) == ??)
    }

    exercise("Function with a parameter") {
      // TODO Create your own function
      val greeting: String => String = |>?

      check(greeting("general Kenobi") == "Hello there, general Kenobi")
    }

    exercise("function with default parameters") {

      /**
       * You can declare default parameters in Scala function simply by
       * adding `= value` after a parameters.
       *
       * Here is an example:
       */

      def incrementBy(increment: Int, baseNumber: Int = 0) = baseNumber + increment

      check(incrementBy(42) == ??)
      check(incrementBy(42, 8) == ??)
      check(incrementBy(baseNumber = 42, increment = 8) == ??)

      // TODO modify the function greeting, so it returns "Hello world" when it has no parameter.
      val defaultParam                  = "world"
      def greeting(str: String): String = s"Hello $str"
      
      check(?? == "Hello world")
    }

    exercise("Curryfication") {

      /**
       * In functional programming, functions can be specialized or
       * curryfied (in honor of the late Haskell Curry).
       *
       * Curryfication consists in converting a function that takes 2
       * parameters A and B into a function that takes the parameter A
       * and returns a function that takes the parameter B and returns
       * the final result. So it converts a function `(A, B) => C` to
       * the function `A => B => C`. The same goes for functions with 3,
       * 4, 5... parameters.
       *
       * ex:
       */

      // function that adds a number by another number
      def incrementBy(inc: Int)(n: Int) = n + inc

      // function that increments 2 by another number
      def incrementByTwo: Int => Int = incrementBy(2)

      check(incrementByTwo(8) == ??)

      /** Here is another way to write it */

      val addXtoN: Int => Int => Int = x => n => n + x

      /**
       * this kind of function composition is called right associative
       *
       * it means that a => b => c can be interpreted as a => (b => c)
       *
       * Here is an example of right associativity
       *
       * a + b + c + d === a + (b + c + d) === a + b + (c + d)
       *
       * In actual fact + is right and left associative as no operator
       * takes precedence over another but you get the idea.
       */

      check(addXtoN(2)(8) == ??)
    }

    exercise("Haskell says hi") {
      val greeting: String => String => String = h => w => h + w

      val specializedGreeting: String => String = greeting("Hello ")

      // TODO use specializedGreeting to complete this exercise
      check(?? == "Hello world")
    }

    exercise("Parameter name on function call") {

      /**
       * As seen before, in Scala, you do not have to mind parameter
       * order, as long as you specify their name. It comes in handy
       * when you have a lot a parameters (and you do not want to look
       * at the signature all the time) or if you want to specify only
       * some parameters (when copying a case class for example).
       */

      def functionWithManyParameters(a: Int, b: Int, c: Int, d: Int): Int = a + b + c + d

      check(functionWithManyParameters(c = 1, a = 2, d = 3, b = 4) == ??)

      case class User(firstName: String, lastName: String)

      val johnDoe = User("john", "doe")

      check(johnDoe.copy(firstName = "jane") == ??)
    }

    exercise("Call-by-value & call-by-name") {

      /**
       * Let us talk about two concepts named:
       *   - call-by-name
       *   - call-by-value
       *
       * So far, you have been using call-by-value parameters till
       * there. Here is a good enough definition found on the internet:
       *
       * "In Scala, when arguments pass through call-by-value function,
       * it computes the passed-in expression's or arguments value once
       * before calling the function"
       *
       * So, for instance:
       */

      def double1(evaluate: Boolean, message: String): String =
        if (evaluate)
          s"$message $message"
        else
          "nope"

      /** Reminder: you can pass functions as values in Scala. */
      var x1 = 0
      val messageWithAnEffect1 = {
        x1 += 1
        "hello"
      }

      check(double1(evaluate = false, messageWithAnEffect1) == ??)
      check(x1 == ??)

      check(double1(evaluate = true, messageWithAnEffect1) == ??)
      check(x1 == ??)

      /**
       * Now, let's use a by-name parameter!
       *
       * According to Scaladoc: By-name parameters are evaluated every
       * time they are used. They won’t be evaluated at all if they are
       * unused.
       *
       * You may declare by-name params by prepending a fat arrow to
       * your parameter type
       */

      def double2(evaluate: Boolean, message: => String): String =
        if (evaluate)
          s"$message $message"
        else
          "nope"

      var x2 = 0

      check(
        double2(
          evaluate = false, {
            x2 += 1
            "hello"
          }
        ) == ??
      )
      check(x2 == 1)

      check(
        double2(
          evaluate = true, {
            x2 += 1
            "hello"
          }
        ) == ??
      )
      check(x2 == ??)
    }

  }

  section("PART 4 - Control flow") {

    /** Scala if statements can be passed as a value. */

    exercise("if-else") {
      val x: Int            = 42
      val condition: String = if (x == 42) "forty two" else "some other number"

      check(condition == ??)
    }

    exercise("If else as a value") {
      // todo : use an if statement inside a string interpolation
      // reminder: string interpolation -> s"hello ${someVal}"

      val x: Int      = 42
      val condition   = if (x % 2 == 0) "even" else "odd"
      val str: String = s"x is an ${??} number"

      check(str == ??)
    }
  }

  section("PART 5 - Case class") {

    /**
     * Case classes are classes meant to hold values. Their equivalent
     * in other languages would be "data classes", "record", or "POJO".
     * Scala cases classes come with several perks:
     *   - the apply method allows you to create case classes without
     *     the "new" keyword.
     *   - all parameters are `public val`, therefore easy to access
     *     (though immutable ;) )
     *   - toString, hashcode and equals are implemented by default.
     *   - they can be used in pattern matching.
     */

    exercise("create a case class and an instance") {
      case class Pokemon(name: String, id: Int)

      val bulbasaur = Pokemon("Bulbasaur", 1)

      /** TODO create a new Pokemon named ditto with an id of 132. */
      val ditto: Pokemon = |>?

      /**
       * Ditto is a pokemon with a special attack called `Transform`
       * giving it the ability to change into an enemy.
       *
       * Let's use transform on Bulbasaur.
       */

      val transformedDitto = ditto.copy(name = "Bulbasaur")

      check(transformedDitto == ??)

      // TODO Let's finish the transformation by giving ditto bulbasaur's id

      val fullyTransformedDitto = |>?

      // the following test should pass, as case classes are compared by value.
      check(fullyTransformedDitto == bulbasaur)

      check(fullyTransformedDitto.toString == "Pokemon(Bulbasaur,1)")

    }

    exercise("create your own case class") {

      /**
       * TODO create a case class Student, its attributes will be:
       *   - name of type String
       *   - grades of type Seq[Int]
       *   - isHardWorking of type Boolean with a default value of
       *     `true`
       */

      |>?

      // TODO create an instance of Student in a way that passes the test

      // TODO uncomment those lines
      // val student = Student("jack", List(1,2,3))
      // check(student.isInstanceOf[Student])
      // check(student.isHardWorking == true)
    }

  }

  section("PART 6 - Functional data structures") {

    exercise("Function returning an Option") {

      /**
       * The type Option represents optional values Optional values can
       * be one of the following two types:
       *   - None: represnting the absence of value.
       *   - Some: representing the presence of a value.
       */

      val map = Map("a" -> 42, "c" -> 43)
      val a   = Some(42)
      val b   = None

      check(map.get("a") == ??)
      check(map.get("b") == ??)
    }

    exercise("Function returning a Try") {

      /**
       * The Try[A] type looks a bit like the Option type however the
       * value can be of the two following types:
       *   - Success(a): where a is of type A
       *   - Failure(err) where err is of type Throwable
       */

      val try1: Try[Double] = Try("42".toDouble)
      val try2: Try[Double] = Try("4 2".toDouble)

      val success = ??
      val failure = ??

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

      // "you take the red pill, you stay in wonderland, and I show you how deep the rabbit hole goes."
      // (`type` allows you to an alias to an existing type.)
      type RedPill  = String
      type BluePill = Int

      // Remember, i'm offering you the truth, nothing more.

      def morpheus(choice: Boolean): Either[BluePill, RedPill] = if (choice) Right("truth") else Left(1010110100)

      check(morpheus(true) == ??)
      check(morpheus(false) == ??)
    }

    exercise("For comprehension") {

      /**
       * A "for comprehension" is a way to sequentially transform
       * values.
       */

      val option1 = Some(42)
      val option2 = Some(8)

      val sumOption =
        for {
          fortyTwo <- option1
          eight    <- option2
        } yield fortyTwo + eight

      check(sumOption == ??)
    }
  }

  section("PART 7 - SBT") {
    exercise("add sbt dep") {

      /**
       * Chances are that you'll need some libraries at some point.
       * Let's install one among the best of them all: ZIO
       *
       * TODO --> First go and read this doc:
       * https://zio.dev/getting_started
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

  section("PART 8 - enums") {
    exercise("list all possibility of the solar system") {

      /**
       * Scala like many other languages allows you to write enums.
       * Enums are a way to describe a set of constant values
       */

      enum SolarSystemPlanet:
        case Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune

      // TODO list all possible planets of the solar system in a Set
      val enumValues: Set[SolarSystemPlanet] = |>?

      check(enumValues.size == 8)

      /**
       * Scala enums come with basic operations such as:
       *   - valueOf: gets an enum value by its name ->
       *     SolarSystemPlanet.valueOf("Jupiter")
       *   - values: returns every enum value in an array
       *   - ordinal: values of an enum are associated to a unique
       *     integer. This unique int can be accessed using ordinal like
       *     so -> SolarSystemPlanet.Mercury.ordinal
       *   - fromOrdinal: retrieve enum value from ordinal ->
       *     SolarSystemPlanet.fromOrdinal(0)
       */

      import SolarSystemPlanet.*

      check(SolarSystemPlanet.values == ??)
      check(SolarSystemPlanet.Earth.ordinal == ??)
      check(SolarSystemPlanet.fromOrdinal(0) == ??)
      check(SolarSystemPlanet.valueOf("Mars") == ??)

    }

    exercise("parameterized enums") {

      /** Scala 3 enums can come with parameters */

      enum ParamColor(val rgb: Int):
        case Red extends ParamColor(0xff0000)
        case Green extends ParamColor(0x00ff00)
        case Blue extends ParamColor(0x0000ff)
      end ParamColor

      val red: ParamColor = ParamColor.Red

      check(red.rgb == ??)
    }

    exercise("enums on steroids") {

      /** enums can have their own signature and fields */

      enum Squared(w: Int, l: Int):
        def area: Int = w * l

        case Square extends Squared(2, 2)
        case BigRectangle extends Squared(10, 20)
        case SmallRectangle extends Squared(2, 4)
      end Squared

      check(Squared.Square.area == ??)
    }

  }

  section("PART 9 - pattern matching as switch case") {

    /**
     * A first use of pattern matching consists in replacing a simple
     * series of if.
     */

    exercise("Simple pattern matching") {

      /**
       * Pattern matching (like switch...case) can improve the
       * readability of your source code.
       *
       * The structure starts with an expression (here, the variable
       * `expression`), the `match` keyworks, and series of `case`
       * clauses. The case clause is divided in 2 parts by the symbol
       * `=>` (also named ''fat arrow''). The left part is a pattern to
       * which the input expression is compared. The right part is an
       * expression to use is the left pattern matches.
       *
       * The line `case _` is used when none of the patterns above
       * match.
       */

      def letEmIn(selector: Int): String =
        selector match {
          case 1 => "Sister Suzie"
          case 2 => "Brother John"
          case 3 => "Martin Luther"
          case 4 => "Phil and Don"
          case 5 => "Brother Michael"
          case 6 => "Auntie Gin"
          case _ => "Open the door and let 'em in"
        }

      check(letEmIn(2) == ??)
      check(letEmIn(42) == ??)
    }

    exercise("Type matching") {

      /**
       * So, we can use pattern matching to check the type of an
       * expression. We use the symbol `_` in all cases to indicate that
       * we do not pay attention to the value. We only care about the
       * type.
       */
      def defaultValueForTypeOf(value: Any): Option[Any] =
        value match {
          case _: Int    => Some(0)
          case _: Double => Some(0.0)
          case _: String => Some("")
          case _         => None
        }

      check(defaultValueForTypeOf(1) == ??)
      check(defaultValueForTypeOf("hello") == ??)
      check(defaultValueForTypeOf(List(1, 2, 3)) == ??)

      // TODO: We can match multiple type at once: case _ @ (_: Int, _: Double)
    }

    section("Pattern matching and list") {
      exercise("Sum of a list") {
        def sum(l: List[Int]): Int =
          l match {
            case Nil       => 0
            case x :: tail => x + sum(tail)
          }

        check(sum(List.empty) == ??)
        check(sum(List(1)) == ??)
        check(sum(List(1, 2, 3, 4)) == ??)
      }

      exercise("Length of list") {
        def length[A](l: List[A]): Int = |>?

        check(length(List.empty[String]) == 0)
        check(length(List(10, 20, 40)) == 3)
        check(length(List(List.empty[Double])) == 1)
      }

      exercise("Sum of a list (tail recursive)") {
        // TODO uncomment the line below and ensure that there is no compilation error in the sum implementation
        // @tailrec
        def sum(l: List[Int]): Int = |>?

        check(sum(List.empty) == 0)
        check(sum(List(2)) == 2)
        check(sum(List(10, 20, 30, 40)) == 100)
      }
    }
  }

}
