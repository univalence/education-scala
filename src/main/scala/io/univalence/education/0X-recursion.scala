package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success, Try}

import java.time.Duration
import java.util.concurrent.TimeUnit

@main
def recursion(): Unit = {
  section("PART X - recursive functions") {

    /**
     * So far, we know that functional programming does not allow
     * reassigning values.
     *
     * To understand why in depth please refer to the "advanced
     * concepts" lab
     *
     * That being said, how can we loop over a variable ? how do we
     * create while loops ?
     *
     * Well, we don't. Instead FP uses recursive function (meaning
     * functions that call themselves). These function allow our code to
     * stay as pure as a mormon
     *
     * Let us start with a wee example shall we ?
     */

    exercise("Fill a list with ints") {

      /**
       * the goal of this exercise is to create a function that fills a
       * list with ints. Using imperative code we'do something like
       * this:
       */

      var list: ListBuffer[Int] = ListBuffer()
      for (i <- 0 to 9)
        list.append(i)

      /** However here is how we'd do the same thing using FP */
      def fillList(endInclusive: Int, list: List[Int] = List(), startInclusive: Int = 0): List[Int] =
        startInclusive match {
          case endInclusive => list :+ endInclusive
          case iteration =>
            val newList = list :+ iteration
            fillList(endInclusive, newList, iteration + 1)
        }

      val validation = List(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

      check(fillList(9) == validation)
      check(list == validation)
    }

    exercise("") {
      check(true)
    }
  }

  section("PART X - tailrec") {

    /**
     * Recursive functions allow us to write pure and elegant recursive
     * functions. However it comes with a couple of issues
     *   - for starters, recursive functions are slower than iterative
     *     functions.
     *   - but most importantly, they require quiet a bit of memory.
     *
     * Indeed, a recursive function can easily blow the stack, therefore
     * summoning the infamous StackOverflow !
     *
     * Just in case you are not familiar with the JVM call stack, here
     * is a quick overlook of what it is:
     *
     *   - each JVM thread has a call stack
     *   - each stack stores som stack frames
     *   - a stack frame is called every time a method is called (I bet
     *     you get where i'm going)
     *   - each stack frame can have a different size given the method
     *     parameters and algorithm
     *   - stack frames are useful to keep the state of the function
     *
     * In short, multiple recursive calls will "stack" some stack frames
     * up until it all blows up
     *
     * However, and because recursive functions are some important in
     * FP, functional languges usually implement a tail call
     * optimization.
     *
     * Methods that are able to use tail call optimizations are called
     * tail recursive
     *
     * Basically, tail call optimization allows the JVM not to add a new
     * stack frame everytime a recursive call is made to the condition
     * that the recursive call is the LAST statement executed in the
     * function.
     *
     * The point is, because there are no statements after the recursive
     * call, preserving the state and stack frame of the parent function
     * is not useful
     *
     * Here comes an example:
     */

    @tailrec
    def timesUp(current: Int = 10): Unit =
      current match {
        case 0 => println("Time's up !")
        case i =>
          println(i)
          Thread.sleep(i * 1000)
          timesUp(current - 1)
      }

    exercise("make it tailrec") {

      // todo: uncomment the tailrec annotation and modify the function so it's tail recursive.
      // hint: add an accumulator as argument

      // @tailrec
      def sum(l: List[Int]): Int =
        l match {
          case Nil       => 0
          case x :: tail => x + sum(tail)
        }

      check(sum(List.empty) == ??)
      check(sum(List(1)) == ??)
      check(sum(List(1, 2, 3, 4)) == ??)

    }
  }

  section("PART X - recursive dat structures") {

    exercise("cuntinually") {

      /**
       * One of the perks of pure functions is that they can easily be
       * made lazy, meaning that they won't be evaluated until needed.
       * Therefore it's easy to have structure that represent infinty.
       *
       * Iterator.continually is one of these methods that return a
       * never ending iterator over which you can play
       */

      val doriterator     = Iterator.continually(() => "Hi, I'm Dory. I suffer from short-term remembory loss.")
      val extract: String = doriterator.take(5).map(f => f()).mkString("\n")

      check(
        extract ==
          """
            |Hi, I'm Dory. I suffer from short-term remembory loss.
            |Hi, I'm Dory. I suffer from short-term remembory loss.
            |Hi, I'm Dory. I suffer from short-term remembory loss.
            |Hi, I'm Dory. I suffer from short-term remembory loss.
            |Hi, I'm Dory. I suffer from short-term remembory loss.
            |""".stripMargin
      )

    }

    exercise("from") {

      /**
       * maybe you're not interested in representing some of the
       * greatest Dory quotes. Then you might want to represent
       * infintite sequences of Ints
       */

      val evenInts                = Iterator.from(0, 2)
      val evenNumbersLowerThanTen = evenInts.take(5).toList
      check(evenNumbersLowerThanTen == List(0, 2, 4, 6, 8))

    }

    exercise("fibonacci") {

      /**
       * Iterator comes with a function that allow you to constantly
       * apply a function to the previous result. It's time for a
       * classic, let's create a representation of fibonacci numbers
       */

      val fib = Iterator.iterate((0, 1))(t => (t._2, t._1 + t._2))
      val l2  = fib.take(5).map(t => t._2).mkString("0, ", ", ", ", etc.")

      check(l2 == "0, 1, 1, 2, 3, 5 etc.")
    }

    exercise("retry") {

      /**
       * A LazyList (fka Stream) is like a List, except that its
       * elements are computed lazily.
       *
       * Another key difference is that lazy lists memoizes its values,
       * meaning that it can be traversed several times (whereas
       * Iterators can only be traversed once)
       *
       * Let's say we'd like to create a function that retries something
       * exponentially until it works (if this was applied to API calls
       * it would be called "exponential backoff")
       */

      def apiCall(timeout: Long): Try[Int] = {
        println(s"waiting for ${timeout} milliseconds")
        Thread.sleep(timeout)
        if (timeout < 1_000)
          Failure(new Exception("not enough times spend"))
        else
          Success(42)
      }

      val retry: LazyList[Try[Int]] = LazyList.from(1).map(n => apiCall(math.pow(1.5, n).*(100).toLong))

      retry.take(2).foreach(println)
      println("---")
      retry.take(6).foreach(println)

      val out =
        """
          |waiting for 150 milliseconds
          |Failure(java.lang.Exception: not enough times spend)
          |waiting for 225 milliseconds
          |Failure(java.lang.Exception: not enough times spend)
          |---
          |Failure(java.lang.Exception: not enough times spend)
          |Failure(java.lang.Exception: not enough times spend)
          |waiting for 337 milliseconds
          |Failure(java.lang.Exception: not enough times spend)
          |waiting for 506 milliseconds
          |Failure(java.lang.Exception: not enough times spend)
          |waiting for 759 milliseconds
          |Failure(java.lang.Exception: not enough times spend)
          |waiting for 1139 milliseconds
          |Success(42)
          |""".stripMargin

          // todo: explain why the program is not waiting after `---` ?
          // hint: the answer is in the definition

      check(true)

    }
  }
}
