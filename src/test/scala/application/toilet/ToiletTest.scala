package io.andrelucas
package application.toilet

import application.commons.NameInvalidException

import io.andrelucas.application.toilet.Toilet.ToiletType

import scala.util.Either.LeftProjection

class ToiletTest extends UnitTest {

  it should "not create a toilet when description is empty" in {
    val toilet = Toilet.create("", 0, 0)

    toilet.toEither.left should be (LeftProjection(Left(NameInvalidException("Description cannot be empty"))))
  }

  it should "not create a toilet when geolocation is invalid" in {
    val toilet = Toilet.create("Gym Toilet", 90.1, 181)

    toilet.toEither.left should be (LeftProjection(Left(GeolocationException("Invalid geolocation"))))
  }

  it should "create a toilet when description and geolocation are valid" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180)
    val toiletId = toilet.toEither.right.get.id
    toilet.toEither.right.get should be (Toilet(toiletId, "Gym Toilet", Geolocation(90, 180), ToiletType.Free, 0))
  }


  it should "create a free toilet when toilet price is less or equal 0" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180, 0)
    val toiletId = toilet.toEither.right.get.id

    toilet.toEither.right.get should be (Toilet(toiletId, "Gym Toilet", Geolocation(90, 180), ToiletType.Free, 0))

    val toilet2 = Toilet.create("Gym Toilet", 90, 180, -100)
    val toiletId2 = toilet2.toEither.right.get.id

    toilet2.toEither.right.get should be(Toilet(toiletId2, "Gym Toilet", Geolocation(90, 180), ToiletType.Free, 0))
  }

  it should "create a paid toilet when has a price" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180, 100)
    val toiletId = toilet.toEither.right.get.id

    toilet.toEither.right.get should be (Toilet(toiletId, "Gym Toilet", Geolocation(90, 180), ToiletType.Paid, 100))
  }
}
