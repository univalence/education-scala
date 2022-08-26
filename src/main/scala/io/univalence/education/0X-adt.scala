package io.univalence.education

import io.univalence.education.internal.exercise_tools.*

import scala.annotation.tailrec

/** =Algebraic Data Type (ADT)= */
@main
def adt(): Unit =
  section("ADT") {

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

    import Expression.*

    def eval(expression: Expression, variableValue: Double): Double =
      expression match
        case Variable          => ???
        case Constant(value)   => ???
        case Add(left, right)  => ???
        case Mult(left, right) => ???

    exercise("Evaluate an expression") {
      check(eval(Variable, 3) == 3.0)
      check(eval(Constant(2.0), 3) == 2.0)
      check(eval(Add(Constant(1), Mult(Variable, Constant(2))), 3) == 7.0)
    }

    /**
     * Complete this function in a view to get the size of an
     * expression, ie. the number of symbols that appear in the
     * expression.
     *
     * This will serve as an heuristic in order to estimate the
     * complexity of an expression.
     */
    def size(expression: Expression): Int = ???

    exercise("Size of an expression") {
      check(size(Variable) == 1)
      check(size(Constant(2)) == 1)
      check(size(Add(Variable, Variable)) == 3)
      check(size(Add(Variable, Constant(1))) == 3)
      check(size(Mult(Variable, Variable)) == 3)
      check(size(Mult(Variable, Constant(1))) == 3)
      check(size(Add(Constant(1), Mult(Variable, Constant(2)))) == 5)
    }

    exercise("Simplify an expression") {

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
