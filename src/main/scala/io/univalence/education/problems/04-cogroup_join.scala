package io.univalence.education.problems

import io.univalence.education.internal.exercise_tools.*

import scala.collection.{immutable, View}
import scala.io.Source
import scala.util.{Try, Using}

import java.io.FileInputStream
import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.zip.GZIPInputStream

/**
 * =Cogroup=
 *
 * Cogroup is an operation that takes 2 datasets indexed by a key and
 * try to organize them in a single dataset, where elements with the
 * same key are in the same entry.
 *
 * This operation is a generalization of join operation, that is used in
 * SQL. Join and cogroup are very useful operation in data analytics.
 */
@main
def _04_cogroup_join(): Unit =
  section("Cogroup") {
    import cogroup_join.*
    import cogroup_join.CheckinId.*
    import cogroup_join.VenueId.*

    /**
     * Here dataset are represented using Map, where data are already
     * grouped according to a given key. This means, if in the dataset,
     * many records share the same key, they will be found within the
     * same key in a list.
     *
     * The key can be of any kind (string, int, UUID...).
     *
     * @param left
     *   left dataset.
     * @param right
     *   right dataset.
     * @tparam A
     *   type of data in the right dataset.
     * @tparam B
     *   type of data in the right dataset.
     * @tparam ID
     *   type of the key.
     * @return
     */
    def cogroup[A, B, ID](left: Map[ID, List[A]], right: Map[ID, List[B]]): Map[ID, (List[A], List[B])] = |>?

    exercise("Complete the implementation of cogroup, so the checks below pass") {
      check(
        cogroup(Map[String, List[Int]](), Map[String, List[Int]]())
          == Map.empty
      )

      check(
        cogroup(Map[String, List[Int]]("a" -> List(1)), Map[String, List[Int]]())
          == Map("a" -> (List(1), List.empty))
      )

      check(
        cogroup(Map("a" -> List(1)), Map("a" -> List(2)))
          == Map("a" -> (List(1), List(2)))
      )

      check(
        cogroup(Map("a" -> List(1)), Map("b" -> List(2)))
          == Map("a" -> (List(1), List.empty), "b" -> (List.empty, List(2)))
      )
    }

    exercise("Get the five most popular venues") {
      (for {
        checkins: Map[CheckinId, Checkin] <- loadData[Checkin, CheckinId]("data/foursquare/checkins.txt.gz", Checkin)
        venues: Map[VenueId, Venue]       <- loadData[Venue, VenueId]("data/foursquare/venues.txt.gz", Venue)
      } yield {
        // TODO group checkins by venue ID
        val checkinsByVenue: Map[VenueId, List[Checkin]] = |>?

        // TODO group venues by venue ID (you just have each venue in a list)
        val venuesById: Map[VenueId, List[Venue]] = |>?

        // cogroup checkins and venues with the same venue ID
        val checkinsAndVanues: Map[VenueId, (List[Checkin], List[Venue])] = cogroup(checkinsByVenue, venuesById)

        // TODO remove from the result all entry that has no venue
        val filteredCheckinsAndVanues: Map[VenueId, (List[Checkin], List[Venue])] = |>?

        // TODO use VenueCheckins structure (see below) to count the checkins by venue
        val venuesWithCheckinCount: List[VenueCheckins] = |>?

        // Descending sort occording to the checkin count and take the 5 first elements
        val result: List[VenueCheckins] =
          venuesWithCheckinCount
            .sortBy(-_.checkinCount)
            .take(5)

        check(
          result ==
            List(
              VenueCheckins(Venue(VenueId("439ec330f964a520102c1fe3"), 33.943894, -118.405023, "Airport", "US"), 5),
              VenueCheckins(Venue(VenueId("4412be02f964a520d6301fe3"), 44.854713, -93.24204, "Mall", "US"), 3),
              VenueCheckins(Venue(VenueId("41059b00f964a520850b1fe3"), 37.616424, -122.386279, "Airport", "US"), 3),
              VenueCheckins(Venue(VenueId("43a52546f964a520532c1fe3"), 40.645089, -73.784523, "Airport", "US"), 3),
              VenueCheckins(
                Venue(VenueId("41102700f964a520d60b1fe3"), 40.750614, -73.988564, "Department Store", "US"),
                3
              )
            )
        )
      }).get
    }
  }

object cogroup_join:

  /** Interface for deserialization. */
  trait Deserializer[A]:
    def deserialize(line: String): A

  /** Interface to exreact an ID from a case class instance. */
  trait GetId[A, B]:
    def idFrom(a: A): B

  object CheckinId:
    /**
     * By using `type` keyword, you can create an alias to an existing
     * type (like a synonym). With `type A = String`, `A` is totally
     * equivalent and interchangeable with the type String.
     *
     * By using `opaque type`, you can create a type that is only
     * similar to an existing type. With `opaque type A = String`,
     * elements of type `A` looks like String, but they cannot be mixed
     * with elements of type String: they are separated types. This is
     * true only in your code, but after the compilation the
     * distinctions between `A` and String are removed, and so are
     * conversion function between `A` and String. So, `opaque type`
     * declares new types with no cost at runtime.
     */
    opaque type CheckinId = String

    /** Conversion function from String to CheckinId. */
    def apply(s: String): CheckinId = s

    /** Conversion function from CheckinId to String. */
    extension (id: CheckinId) def toString: String = id

  object VenueId:
    opaque type VenueId = String

    def apply(s: String): VenueId                = s
    extension (id: VenueId) def toString: String = id

  import CheckinId.*
  import VenueId.*

  /** Checkin datatype */
  case class Checkin(id: CheckinId, venueId: VenueId, timestamp: LocalDateTime)

  object Checkin extends Deserializer[Checkin] with GetId[Checkin, CheckinId]:
    override def idFrom(checkin: Checkin): CheckinId = checkin.id

    override def deserialize(line: String): Checkin = {
      val field = line.split("\t")

      Checkin(id = CheckinId(field(0)), venueId = VenueId(field(1)), timestamp = foursquareTimestampToInstant(field(2)))
    }

  /** Venue datatype */
  case class Venue(id: VenueId, latitude: Double, longitude: Double, venueType: String, country: String)

  object Venue extends Deserializer[Venue] with GetId[Venue, VenueId]:
    override def idFrom(venue: Venue): VenueId = venue.id

    override def deserialize(line: String): Venue = {
      val field = line.split("\t")

      Venue(
        id        = VenueId(field(0)),
        latitude  = field(1).toDouble,
        longitude = field(2).toDouble,
        venueType = field(3),
        country   = field(4)
      )
    }

  /** Datatype to count the number of checkins by venue. */
  case class VenueCheckins(venue: Venue, checkinCount: Int)

  def foursquareTimestampToInstant(timestamp: String): LocalDateTime = {
    val formatter = DateTimeFormatter.ofPattern("EEE LLL dd HH:mm:ss Z yyyy")
    val dateTime  = LocalDateTime.parse(timestamp, formatter)

    dateTime
  }

  /**
   * The type expression `A & B` means that a value matches both the
   * type `A` and the type `B`.
   */
  def loadData[A, B](filename: String, deserializer: Deserializer[A] & GetId[A, B]): Try[Map[B, A]] =
    Using(Source.fromInputStream(new GZIPInputStream(new FileInputStream(filename)))) { input =>
      val values =
        for (line <- input.getLines())
          yield deserializer.deserialize(line)

      val valuesById =
        values
          .map(data => deserializer.idFrom(data) -> data)

      valuesById.toMap
    }
