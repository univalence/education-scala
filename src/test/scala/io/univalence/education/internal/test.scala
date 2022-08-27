/*
 * Copyright 2022 Univalence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.univalence.education.internal

import io.univalence.education.internal.exercise_tools.*

@main
def test2(): Unit = {
  section("Introduction") {
    exercise("This exercise succeeds") {
      check(1 + 1 == 2)
    }

    exercise("This exercise fails") {
      check(1 + 1 == 3)
    }

    exercise("You have to solve ?? in this exercise by yourself") {
      check {
        val a = 1
        val b = 2

        a + b == ??
      }
    }

    exercise("An exercice can have many checks") {
      check(1 + 1 == 11)
      check(1 + 1 == 2)
    }
  }

  section("Special cases") {
    exercise("This exercise has an exception") {
      check(throw new IllegalArgumentException("oops"))
    }

    exercise("You have to implement a function when you see |>?") {
      def f(n: Int): Int = |>?

      check(f(12) == 34)
    }

    exercise("You have to find the type of a function when you see !?") {
      def f(n: Int): !? = |>?

      check(f(12) == ??)
    }

    exercise("exercise 5", activated = false) {
      |>?
    }
  }
}
