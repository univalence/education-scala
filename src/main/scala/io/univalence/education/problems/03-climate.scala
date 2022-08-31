package io.univalence.education.problems

import io.univalence.education.internal.exercise_tools.*

import scala.collection.MapView
import scala.io.Source
import scala.util.{Try, Using}

import java.io.FileInputStream
import java.time.LocalDate
import java.util.zip.GZIPInputStream

/**
 * In this exercise, we will analyse climate data across the world.
 *
 * It uses a dataset from 1995 to 2020 of temperatures measured across
 * the world.
 *
 * The dataset is CSV file with an header, that contains those columns:
 *   - Region
 *   - Country
 *   - State (for US only)
 *   - City
 *   - Month
 *   - Day
 *   - Year
 *   - Temperature (in Fahrenheit)
 */
@main
def _03_climate(): Unit = {
  import climate.*

  /**
   * This extension simply add a method on [[TemperatureRecord]] to get
   * the value in Celsius.
   */
  extension (t: TemperatureRecord) def celsius = (t.fahrenheit - 32.0) * 5.0 / 9.0

  /**
   * This extension add a method to get the average from a list of
   * values.
   */
  extension (l: List[Double]) def average: Double = l.sum / l.size.toDouble

  (for (data <- loadData("data/climate/city_temperature.csv.gz", TemperatureRecord))
    yield {
      exercise("Check the date range") {

        /**
         * With a combination of [[List.map]] and [[List.min]] or
         * [[List.max]], you can get such range.
         */
        val minDate: LocalDate = |>?
        val maxDate: LocalDate = |>?

        println(indent + s"Dataset date range: from $minDate to $maxDate")

        check(minDate == LocalDate.of(|>? : Int, |>? : Int, |>? : Int))
        check(maxDate == LocalDate.of(|>? : Int, |>? : Int, |>? : Int))
      }

      /**
       * The last year does not seem to be completed. In a view to avoid
       * errors in computation, we will simply drop this last year. This
       * can be done by using [[List.filter]] of [[List.filterNot]].
       *
       * TODO clean the dataset by removing the incomplete year.
       */
      val temperatures: List[TemperatureRecord] = |>?

      exercise("Get the average world temperature") {
        val average: Double = |>?

        println(indent + s"Average world temperature: $average")

        check(average == ??)
      }

      exercise("Evolution of world temperature across years") {

        /**
         * First, we group the temperature records by year. For this,
         * you will need the method [[List.groupBy]]. It takes a
         * function that extracts a key from a record and converts the
         * list in to a map of list. You have a map of list, because
         * many values in the list may have the same key.
         *
         * TODO get the temperature records by year
         */
        val temperaturesByYear: Map[Int, List[TemperatureRecord]] = |>?

        /**
         * By year, we want now the average temperature. You will need
         * to create a view to the Map with [[Map.view]], then to
         * transform only the values in Map with [[MapView.mapValues]].
         */
        val tempAverageByYear: Map[Int, Double] = |>?

        /**
         * Convert the Map into a List of pair year/average temperature.
         * Then sort ascending according to the year.
         */
        val tempAverageSortedByYear: List[(Int, Double)] = |>?

        println(indent + s"Average worldwide temperature by year: $tempAverageSortedByYear")

        /**
         * Now, we will compute the differences in terms of temperature
         * year after year. There, you will need [[List.zip]], that
         * combines two lists elements by elements to get a list pairs
         * of elements with the same index.
         *
         * We will apply zip operation on our previous list with itself,
         * by dropping one element. Here is an example of how to use zip
         * and drop on the same list.
         *
         * {{{
         *   l                => 1 2 3 4
         *   l.drop(1)        => 2 3 4
         *   l.zip(l.drop(1)) => (1,2) (2,3) (3,4)
         * }}}
         *
         * By using map operation, you will be able to compute the
         * difference in temperature between two successive years.
         */
        val tempEvolutionByYear: List[(Int, Double)] = |>?

        println(indent + s"Evolution of temperatures: $tempEvolutionByYear")

        /**
         * It is time to compute the average evolution in temperature
         * year after year.
         */
        val averageTempEvolution: Double = |>?

        println(indent + s"Average evolution of temperatures: $averageTempEvolution")

        check(averageTempEvolution == ??)
      }
    }).get
}

object climate:
  /** Interface for deserialization. */
  trait Deserializer[A]:
    def deserialize(line: String): Option[A]

  case class TemperatureRecord(
      region:     String,
      country:    String,
      state:      String,
      city:       String,
      date:       LocalDate,
      fahrenheit: Double
  )

  object TemperatureRecord extends Deserializer[TemperatureRecord]:
    override def deserialize(line: String): Option[TemperatureRecord] =
      try {
        val fields     = line.split(",")
        val fahrenheit = fields(7).toDouble

        if (fahrenheit <= -99) None
        else {
          val date = LocalDate.of(fields(6).toInt, fields(4).toInt, fields(5).toInt)
          Some(
            TemperatureRecord(
              region     = fields(0),
              country    = fields(1),
              state      = fields(2),
              city       = fields(3),
              date       = date,
              fahrenheit = fahrenheit
            )
          )
        }
      } catch {
        case e: Exception =>
          println(s"${e.getMessage} => with line $line")
          throw e
      }

  def loadData[A](filename: String, deserializer: Deserializer[A]): Try[List[A]] =
    Using(Source.fromInputStream(new GZIPInputStream(new FileInputStream(filename)))) { input =>
      val values =
        for {
          line  <- input.getLines().drop(1)
          value <- deserializer.deserialize(line)
        } yield value

      values.toList
    }
