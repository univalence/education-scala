package io.univalence.education.internal

object implicits {
  implicit class unimplementedMonad[A, F[_]](a: F[A]) {

    /** You should remove this function and write the answer instead. */
    def ??? : Nothing = throw new NotImplementedError
  }
}
