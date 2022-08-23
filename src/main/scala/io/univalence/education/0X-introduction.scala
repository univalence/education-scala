package io.univalence.education

//import zio.*

import io.univalence.education.internal.exercise_tools.*
import io.univalence.education.internal.implicits.*

import scala.::
import scala.annotation.tailrec
import scala.collection.SortedSet
import scala.util.{Failure, Success, Try}

@main
def _00_introduction(): Unit = {
  section("PART 1 - Variables") {

    exercise("Mutable variable") {

      /** You can create mutable variables */
      var a: Int = 42
      a += 8

      check(a == ??)

    }

    exercise("Immutable variable (aka val)", activated = true) {
      // todo: swap the ??? expression with an appropriate value so that the test passes
      val a: Int = 42
      val b: Int = 8
      check(a + b == ??)

      // todo: create a value so that the test passes
      check(?? == "hello")
    }

    /**
     * Unlike Java, the scala compiler is able to infer types, meaning
     * it can guess most types without having manually write it.
     *
     * However it's considered good practice to state most types
     * explicitly (especially public functions)
     */
    exercise("Type inference", activated = true) {
      val a = ???
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

      val l1 = ???
      check(l1 == 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil)
    }

    exercise("Mutate list", activated = true) {

      /** Reminder: Scala list are immutable */

      val l1 = List(42)
      val l2 = ???
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
      check(sortedSet == ??)
      check(map == ??)
    }

    exercise("common collection operations", activated = true) {

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

    /**
     * You can decalre default parameers in scala function simply by
     * adding `= value` after a parameters.
     *
     * Here is an example:
     */

    def incrementBy(increment: Int, baseNumber: Int = 0) = baseNumber + increment
    val incrementedValue                                 = incrementBy(42)    // == 42
    val yetAnotherIncrementedValue                       = incrementBy(42, 8) // == 50

    exercise("function with default parameters", activated = true) {

      // todo write a function with a default value

      val defaultParam                  = "world"
      def greeting(str: String): String = s"Hello $str"
      check(?? == "Hello world")
    }

    /**
     * In fuctional programming, functions can be specialized or
     * curryfied (in honor of the late Haskell Curry).
     *
     * It means that you can specify one or some of the arguments and
     * return a function with the rest of the arguments
     *
     * ex:
     */

    // function that adds a number by another number
    def incrementNby(inc: Int)(n: Int) = n + inc
    // function that increments 2 by another number
    def incrementNByTwo: Int => Int = incrementNby(2)
    val inc: Int                    = incrementNByTwo(8) // == 10

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
     * In actual fact + is right and left asociative as no operator
     * takes precedence over another but you get the idea.
     */

    val TwoPlusEight = addXtoN(2)(8) // 10

    exercise("Haskell says hi") {
      val greeting: String => String => String  = h => w => h + w
      val specializedGreeting: String => String = greeting("Hello ")
      check(?? == "Hello world")
    }

    /**
     * In scala, you don't have to mind paramter order as long as you
     * specify their name. It comes in handy when you have a lot a
     * paramters (and dont want to look at the signature all the time)
     * or if you want to specify only some parmeters (when copying a
     * case class for example)
     */

    def functionWithManyParameters(a: Int, b: Int, c: Int, d: Int) = ???
    val something = functionWithManyParameters(c = 1, a = 2, d = 3, b = 4)

    case class User(firstName: String, lastName: String)
    val johnDoe      = User("john", "doe")
    val changeGender = johnDoe.copy(firstName = "jane")

    exercise("naming parameters when calling a function") {
      def yetAnotherGreetingFunction(hello: String, world: String) = s"$hello $world"
      check(yetAnotherGreetingFunction(world = "world", hello = "hello") == ??)
    }

    exercise("passing a function as argument", activated = true) {
      val a = List(1, 2, 3, 4, 5, 6).filter(_ % 2 == 0)
      check(a == List(1, 3, 5))
    }

    /**
     * Let us talk about two concepts named:
     *   - call by name
     *   - call by value
     *
     * So far you've been using call-by-value parameters. here is a good
     * enough definition found on the internet:
     *
     * "In Scala when arguments pass through call-by-value function it
     * compute the passed-in expression's or arguments value once before
     * calling the function"
     *
     * so, for instance:
     */

    def double(evaluate: Boolean, message: String): String =
      if (evaluate)
        s"$message $message"
      else
        "nope"

    /** reminder: you can pass functions as values in scala. */

    val messageWithAnEffect = {
      println("Called !")
      "hello"
    }

    double(evaluate = false, messageWithAnEffect)
    // prints : Called !    <- message was evaluated in the parameters before being passed to the function
    // returns: "nope"
    double(evaluate = true, messageWithAnEffect)
    // prints  : Called !    <- Same here
    // returns : "hello hello"

    /**
     * Now, let's use a by-name parameter !
     *
     * According to Scaladoc : By-name parameters are evaluated every
     * time they are used. They won’t be evaluated at all if they are
     * unused.
     *
     * You may declare by-name params by prepending a fat arrow to your
     * parameter type
     */

    def double2(evaluate: Boolean, message: => String): String =
      if (evaluate)
        s"$message $message"
      else
        "nope"

    double2(evaluate = false, messageWithAnEffect)
    // "nope"
    double2(evaluate = true, messageWithAnEffect)
    // prints : Called !    <- ⌍
    // prints : Called !    <- |  message is evaluated twice in the expression
    // "hello hello"

    exercise("call by name ") {

      // todo: modify double3 in such a way that it only executes message once

      def double3(evaluate: Boolean, message: => String): String =
        if (evaluate)
          s"$message $message"
        else "nope"

      check(double3(evaluate = false, messageWithAnEffect) == "hello hello")
    }

  }

  section("PART 4  - Control flow") {

    /** Scala if statements can be passed as a value. */

    exercise("if else") {
      val x: Int            = 42
      val condition: String = if (x == 42) "forty two" else "some other number"
      check(condition == ??)
    }

    exercise("If else as a value", activated = true) {
      // todo : use an if statement inside a string interpolation
      // reminder: string interpolation -> s"hello ${someVal}"

      val x: Int      = 42
      val condition   = if (x % 2 == 0) "even" else "odd"
      val str: String = s"x is an ${???} number"

      check(str == ??)
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

      val map = Map("a" -> 42, "c" -> 43)
      val a   = Some(42)
      val b   = None

      check(map.get("a") == ??)
      check(map.get("b") == ??)

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

      // you take the red pill, you stay in wonderland, and I show you how deep the rabbit hole goes

      type RedPill  = String
      type BluePill = Int

      // remember, i'm offering you the truth, nothing more

      def morpheus(choice: Boolean): Either[BluePill, RedPill] = if (choice) Right("truth") else Left(1010110100)
      check(morpheus(true) == ??)
    }

    exercise("For comprehension", activated = true) {

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

      check(sumOption == ??)

    }
  }

  section("PART 7  - SBT") {
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

  section("PART 8 - enums") {
    exercise("list all possibility of the solar system", activated = true) {

      /**
       * Scala like many other languages allows you to write enums.
       * Enums are a way to describe a set of constant values
       */

      enum SolarSystemPlanet:
        case Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune

      // todo: list all possibility of the solar system
      val enumValues: Set[SolarSystemPlanet] = ???
      check(enumValues.size == 9)

      /**
       * scala enums come with basic operations such as :
       *
       *   - valueOf: gets an enum value by its name ->
       *     SolarSystemPlanet.valueOf("Jupiter")
       *
       *   - values: returns every enum value in an array
       *
       *   - ordinal: values of an enum are associated to a unique
       *     integer. This unique int can be accessed using ordinal liek
       *     so -> SolarSystemPlanet.Mercury.ordinal
       *
       *   - fromOrdinal: retrieve enum value from ordinal ->
       *     SolarSystemPlanet.fromOrdinal(0)
       */

      check(SolarSystemPlanet.values == ??)
      check(SolarSystemPlanet.Earth.ordinal == ??)
      check(SolarSystemPlanet.fromOrdinal(0) == ??)
      check(SolarSystemPlanet.valueOf("Mars") == ??)

    }

    exercise("parameterized enums", activated = true) {

      /** Scala 3 enums can come with parameters */

      enum ParamColor(val rgb: Int):
        case Red extends ParamColor(0xff0000)
        case Green extends ParamColor(0x00ff00)
        case Blue extends ParamColor(0x0000ff)
      end ParamColor

      val red = ParamColor.Red
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

    exercise("Simple pattern matching", activated = true) {

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

    exercise("Type matching", activated = true) {

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
      exercise("Sum of a list", activated = true) {
        def sum(l: List[Int]): Int =
          l match {
            case Nil       => 0
            case x :: tail => x + sum(tail)
          }

        check(sum(List.empty) == ??)
        check(sum(List(1)) == ??)
        check(sum(List(1, 2, 3, 4)) == ??)
      }

      exercise("Length of list", activated = false) {
        def length[A](l: List[A]): Int = ???

        check(length(List.empty[String]) == 0)
        check(length(List(10, 20, 40)) == 3)
        check(length(List(List.empty[Double])) == 1)
      }

      exercise("Sum of a list (tail recursive)", activated = false) {
        //        @tailrec // to uncomment
        def sum(l: List[Int]): Int = ???

        check(sum(List.empty) == 0)
        check(sum(List(2)) == 2)
        check(sum(List(10, 20, 30, 40)) == 100)
      }
    }
  }

  section("PART 10 - ADT") {

    /**
     * Pattern matching fits perfectly with the enumeration type.
     *
     * As a big advantage, the use of pattern matching and enumeration
     * type will drastically simplify your code, by providing a really
     * intuitive notation.
     */

    enum Expression:
      case Variable
      case Constant(value: Double)
      case Add(left: Expression, right: Expression)
      case Mult(left: Expression, right: Expression)

    val Zero = Expression.Constant(0.0)
    val One  = Expression.Constant(1.0)

    import Expression._

    exercisePart("Evaluate an expression")
    def eval(expression: Expression, variableValue: Double): Double =
      expression match {
        case Add(left, right)  => ???
        case Mult(left, right) => ???
        case Constant(value)   => ???
        case Variable          => ???
      }

    check(eval(Variable, 3) == 3.0)
    check(eval(Constant(2.0), 3) == 2.0)
    check(eval(Add(Constant(1), Mult(Variable, Constant(2))), 3) == 7.0)

    exercisePart("Size of an expression")

    /**
     * Complete this function in a view to get the size of an
     * expression, ie. the number of symbols that appear in the
     * expression.
     *
     * This will serve as an heuristic in order to estimate the
     * complexity of an expression.
     */
    def size(expression: Expression): Int = ???

    check(size(Variable) == 1)
    check(size(Constant(2)) == 1)
    check(size(Add(Variable, Variable)) == 3)
    check(size(Add(Variable, Constant(1))) == 3)
    check(size(Mult(Variable, Variable)) == 3)
    check(size(Mult(Variable, Constant(1))) == 3)
    check(size(Add(Constant(1), Mult(Variable, Constant(2)))) == 5)

    exercise("Simplify an expression", activated = false) {

      /**
       * In this exercise, you are asked to create a function that
       * simplifies an expression.
       *
       * To do so, you will use pattern matching to write down your
       * simplication rules (eg. `X + 0` can be simplified into `X` or
       * `0 * X` can be simplified into `0`). You will need your
       * function to be recursive, in a view to explore all the parts of
       * your expression.
       *
       * Also, just one pass (ie. one recursive call) might not be
       * enough to propagate your rules on the whole expression. For
       * example, even a recursive call will not simplify the expression
       * `Mult(Variable, Mult(Variable, Zero))` into `Zero`. At best,
       * you will get `Mult(Variable, Zero)`. So, in this exercise, you
       * will need the function [[fixedPoint]] below, that will apply
       * your simplification function until, no more simplification
       * happens.
       */

      def simplify(expression: Expression): Expression = ???

      /**
       * Find the fixed point of a function.
       *
       * This function will find the value of `x` such that `f(x) == x`.
       * A `limit` is given in a view to avoid infinite loops.
       *
       * @param f
       *   function on which to find the fix point.
       * @param x0
       *   the initial value to begin with to find the fixed point.
       * @param limit
       *   the maximal number of iterations.
       * @return
       *   a fixed point value or `None` if no fixed point has been
       *   found.
       */

      @tailrec
      def fixedPoint[A](f: A => A)(x0: A, limit: Int = 100): Option[A] =
        if (limit == 0) None
        else {
          val x = f(x0)
          if (x == x0) Some(x0)
          else fixedPoint(f)(x, limit - 1)
        }

      check(simplify(Variable) == Variable)
      check(simplify(Add(Zero, Variable)) == Variable)
      check(simplify(Add(Variable, Zero)) == Variable)
      check(simplify(Mult(Zero, Variable)) == Zero)
      check(simplify(Mult(Variable, Zero)) == Zero)
      check(simplify(Mult(One, Variable)) == Variable)
      check(simplify(Mult(Variable, One)) == Variable)
      check(simplify(Mult(One, Add(Zero, Mult(Variable, One)))) == Variable)
      check(simplify(Mult(Add(One, Mult(Variable, One)), Zero)) == Zero)
      check(simplify(Add(Add(One, One), One)) == Constant(3.0))
    }
  }

}
