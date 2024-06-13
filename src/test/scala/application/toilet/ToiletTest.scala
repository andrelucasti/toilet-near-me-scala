package io.andrelucas
package application.toilet

import application.commons.NameInvalidException

import scala.util.Either.LeftProjection

class ToiletTest extends UnitTest {

  it should "not create a toilet when description is empty" in {
    val toilet = Toilet.create("", 0, 0, ToiletType.Free)

    toilet.toEither.left should be (LeftProjection(Left(NameInvalidException("Description cannot be empty"))))
  }

  it should "not create a toilet when geolocation is invalid" in {
    val toilet = Toilet.create("Gym Toilet", 90.1, 181, ToiletType.Free)

    toilet.toEither.left should be (LeftProjection(Left(GeolocationException("Invalid geolocation"))))
  }

  it should "create a toilet when description and geolocation are valid" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180, ToiletType.Free)

    toilet.toEither.right.get should be (Toilet("Gym Toilet", Geolocation(90, 180), ToiletType.Free, 0))
  }

  it should "not create a toilet when is paid and does not have a value" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180, ToiletType.Paid)

    toilet.toEither.left should be (LeftProjection(Left(ToiletInvalidException("Toilet paid must have a price"))))
  }

  it should "create a paid toilet when has a value" in {
    val toilet = Toilet.create("Gym Toilet", 90, 180, ToiletType.Paid, 100)

    toilet.toEither.right.get should be (Toilet("Gym Toilet", Geolocation(90, 180), ToiletType.Paid, 100))
  }
}
