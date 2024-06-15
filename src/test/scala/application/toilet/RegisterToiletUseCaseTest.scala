package io.andrelucas
package application.toilet

import application.customer.{CustomerId, CustomerNotFoundException}

import io.andrelucas.application.toilet.Toilet.ToiletType
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

class RegisterToiletUseCaseTest extends munit.FunSuite
  with Matchers {

  given ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global
  val bufferToilet: ListBuffer[Toilet] = scala.collection.mutable.ListBuffer.empty[Toilet]

  val toiletInMemory: Toilet => Future[Unit] = (toilet: Toilet) => Future[Unit] {
    println(s"Saving toilet ${toilet.id}")
    bufferToilet += toilet
  }

  override def beforeEach(context: BeforeEach): Unit = {
    bufferToilet.clear()
  }

  test("should not save a toilet when customer does not exist"){
    val mockCustomerExist = (_:CustomerId) => Future.successful(false)
    val customerId = CustomerId.generate.value
    val input = RegisterToiletUseCase.Input("Gym Toilet", 0, 0, customerId)

    val useCase = new RegisterToiletUseCase()

    munit.Assertions.interceptMessage[CustomerNotFoundException](s"Customer $customerId does not exist") {
      val eventualOutput = useCase.execute(input, mockCustomerExist, toiletInMemory)
      concurrent.Await.result(eventualOutput, concurrent.duration.Duration.Inf)
    }

    bufferToilet should have size 0
  }

  test("should not save a toilet when geolocation is invalid") {
    val mockCustomerExist = (_: CustomerId) => Future.successful(true)
    val customerId = CustomerId.generate.value
    val input = RegisterToiletUseCase.Input("Gym Toilet", 90.1, 181, customerId)

    val useCase = new RegisterToiletUseCase()

    val exception = munit.Assertions.intercept[GeolocationException] {
      val eventualOutput = useCase.execute(input, mockCustomerExist, toiletInMemory)
      concurrent.Await.result(eventualOutput, concurrent.duration.Duration.Inf)
    }

    bufferToilet should have size 0
  }

  test("should save a free toilet when customer exists and geolocation is valid") {
    val mockCustomerExist = (_: CustomerId) => Future.successful(true)
    val customerId = CustomerId.generate.value
    val input = RegisterToiletUseCase.Input("Gym Toilet", 90, 180, customerId)

    val useCase = new RegisterToiletUseCase()

    val eventualOutput = useCase.execute(input, mockCustomerExist, toiletInMemory)
    val output = concurrent.Await.result(eventualOutput, concurrent.duration.Duration.Inf)

    bufferToilet should have size 1
    bufferToilet.head should be (Toilet(ToiletId(output.toiletId), "Gym Toilet", Geolocation(90, 180), ToiletType.FREE, 0))
  }

  test("should save a paid toilet when has a toilet price, the customer exists and geolocation is valid"){
    val mockCustomerExist = (_: CustomerId) => Future.successful(true)
    val customerId = CustomerId.generate.value
    val input = RegisterToiletUseCase.Input("Gym Toilet", 90, 180, customerId, 100)

    val useCase = new RegisterToiletUseCase()

    val eventualOutput = useCase.execute(input, mockCustomerExist, toiletInMemory)
    val output = concurrent.Await.result(eventualOutput, concurrent.duration.Duration.Inf)

    bufferToilet should have size 1
    bufferToilet.head should be (Toilet(ToiletId(output.toiletId), "Gym Toilet", Geolocation(90, 180), ToiletType.PAID, 100))
  }
}
