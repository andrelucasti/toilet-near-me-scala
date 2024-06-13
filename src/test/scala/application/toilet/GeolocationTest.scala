package io.andrelucas
package application.toilet

class GeolocationTest extends UnitTest {

  it should "throw an exception when the latitude is invalid" in {
    val exception = intercept[GeolocationException] {
      Geolocation(91, 0)
    }
    exception.getMessage shouldBe "Invalid latitude"
  }

  it should "throw an exception when the longitude is invalid" in {
    val exception = intercept[GeolocationException] {
      Geolocation(0, 181)
    }
    exception.getMessage shouldBe "Invalid longitude"
  }

  it should "throw an exception when the latitude and longitude are invalid" in {
    val exception = intercept[GeolocationException] {
      Geolocation(91, 181)
    }
    exception.getMessage shouldBe "Invalid geolocation"
  }

  it should "create a geolocation" in {
    val geolocation = Geolocation(0, 0)
    geolocation.latitude shouldBe 0
    geolocation.longitude shouldBe 0
  }
}
