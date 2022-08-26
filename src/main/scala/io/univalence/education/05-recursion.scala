package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec
import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.{Failure, Success, Try}

import java.time.Duration
import java.util.concurrent.TimeUnit

/** =Recursion= */
@main
def _05_recursion(): Unit = {
  section("Recursive functions") {

    /**
     * So far, we know that functional programming does not allow
     * reassigning values.
     *
     * To understand why in depth please refer to the "referential
     * transparency" lab
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
      def fillList(startInclusive: Int, endInclusive: Int, list: List[Int] = List()): List[Int] =
        if (startInclusive == endInclusive) list :+ endInclusive
        else {
          val newList = list :+ startInclusive
          fillList(startInclusive + 1, endInclusive, newList)
        }

      check(fillList(0, 9) == ??)
      check(list == ??)
    }

  }

  section("Tailrec") {

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

    /**
     * There are cases where having an infinite is necessary. One of
     * those cases is when your application is a service.
     *
     * A service is an application that continuously waits for an input
     * or a request. When an input arrives, it processes it, returns a
     * response, and wait for another input. It is not supposed to stop.
     *
     * Example of services are Web applications like Twitter or
     * Facebook, or any kind of Web sites.
     *
     * Instead of using a `while` loop, in Scala, you also use a
     * tail-recursive function to represent the loop of a service.
     *
     * {{{
     *   @tailrec
     *   def serve[A, B](waitInput: () => A)(process: A => B)(sendResponse: B => Unit): Unit =
     *     val input = waitInput()
     *     val response = process(input)
     *     sendResponse(response)
     *
     *     serve(waitInput)(process)(sendResponse)
     * }}}
     */

  }

  section("Recursive data structures") {

    exercise("continually") {

      /**
       * One of the perks of pure functions is that they can easily be
       * made lazy, meaning that they won't be evaluated until needed.
       * Therefore it's easy to have structure that represent infinty.
       *
       * Iterator.continually is one of these methods that return a
       * never ending iterator over which you can play
       */

      val iterator = Iterator.continually(() => "All work and no play makes Jack a dull boy.")

      val extract: String = iterator.take(5).map(f => f()).mkString("\n")

      check(extract == ??)
    }

    exercise("from") {

      /**
       * maybe you're not interested in representing some of the
       * greatest Dory quotes. Then you might want to represent
       * infintite sequences of Ints
       */

      val evenInts                = Iterator.from(0, 2)
      val evenNumbersLowerThanTen = evenInts.take(5).toList

      check(evenNumbersLowerThanTen == ??)
    }

    exercise("fibonacci") {

      /**
       * Iterator comes with a function that allow you to constantly
       * apply a function to the previous result. It's time for a
       * classic, let's create a representation of fibonacci numbers
       */

      val fib = Iterator.iterate((0, 1))(t => (t._2, t._1 + t._2))
      val l2  = fib.take(5).map(t => t._2).mkString("0, ", ", ", ", etc.")

      check(l2 == ??)
    }

    exercise("retry", activated = false) {

      /**
       * A [[LazyList]] (aka Stream) is like a List, except that its
       * elements are computed lazily.
       *
       * Another key difference is that lazy lists memoize its values,
       * meaning that it can be traversed several times (whereas
       * Iterators can only be traversed once).
       *
       * Here, we will use a LazyList to implement a retry mechanism.
       *
       * Your application may depends on a remote service (access to an
       * account in a bank, order an object on Amazon, get the last
       * available tweets from Twitter). The problem is that while you
       * call a remote service, you experience a failure for
       * hard-to-know reasons. To ensure that the failure is not due to
       * a problem of latency on the network, or a temporary un
       * availability of the service, we have to retry the call to the
       * service, and you can retry many times.
       */

      /** This structure is used to store the logs of the service. */
      val output: ArrayBuffer[String] = ArrayBuffer.empty

      /**
       * This is an simulation of the call to a service.
       *
       * As you can see it returns a [[Try]]. This means that the call
       * may fail to return an Int.
       */
      def serviceCall(timeOfTheCall: Long): Try[Int] = {
        output.append(s"waiting for $timeOfTheCall milliseconds")
        Thread.sleep(timeOfTheCall)

        if (timeOfTheCall < 1_000)
          Failure(new Exception("not enough times spend"))
        else
          Success(42)
      }

      /**
       * This is the retry function. It takes a maximum number of
       * retries and the function representing the call to a service.
       *
       * This function returns the first retry of the call to the
       * service that succeeds or, if the maximum number of retries has
       * been reached, it returns the last obtained error.
       */
      def retry[A](retryCount: Int)(serviceCall: Long => Try[A]): Try[A] = {
        // we use a LazyList to virtually represent an infinite number of retries.
        // each retry as a number. It starts from 1.
        val callList: LazyList[(Int, Try[A])] =
          LazyList
            .from(1)
            .map { retryNumber =>
              // for the simulation, after each the time leave to the call increases exponentially.
              val timeOfTheCall = (math.pow(1.5, retryNumber) * 100).toLong

              val result: Try[A] = serviceCall(timeOfTheCall)
              output.append(result.toString)

              (retryNumber, result)
            }

        // at this level we limit the size of the LazyList with the function takeWhile
        val retriedCalls: LazyList[(Int, Try[A])] =
          callList.takeWhile { case (retryNumber, result) =>
            // retries continue while we did not reach the maximum number of retries
            // and while the call to the service has failed.
            retryNumber <= retryCount && result.isFailure
          }

        // we return the last available result of the successive calls
        retriedCalls.last._2
      }

      retry(2)(serviceCall)
        .foreach(result => output.append(result.toString))

      output.append("---")

      retry(6)(serviceCall)
        .foreach(result => output.append(result.toString))

      check(output.mkString("\n") == ??)

      // TODO explain why the program is not waiting after `---` ?
      // hint: the answer is in the definition
    }
  }
}
