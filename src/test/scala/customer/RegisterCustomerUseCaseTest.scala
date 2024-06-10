package io.andrelucas
package customer


import customer.usecases.CustomerRepository

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatestplus.mockito.MockitoSugar

import scala.util.Failure

class RegisterCustomerUseCaseTest extends AsyncFlatSpecLike with MockitoSugar{
  val customerRepositoryMock: CustomerRepository = mock[CustomerRepository]

  it should "not register a customer with invalid email" in {
    val useCase = RegisterCustomerUseCase(customerRepositoryMock)
    val input = RegisterCustomerUseCase.Input("André Lucas", "andrelucas.com", "123456")

    val eventualOutput = useCase.execute(input)
    eventualOutput.value match
      case Some(Failure(exception)) =>
        assert(exception.getMessage == "Invalid email")
  }

  it should "not register a customer name is empty" in {
    val useCase = RegisterCustomerUseCase(customerRepositoryMock)
    val input = RegisterCustomerUseCase.Input("", "andrelucastic@gmail.com", "123456")

    useCase.execute(input).value match
      case Some(Failure(exception)) =>
        assert(exception.getMessage == "Name cannot be empty")
  }

  it should "not register a customer email is invalid" in {
    val useCase = RegisterCustomerUseCase(customerRepositoryMock)
    val input = RegisterCustomerUseCase.Input("André Lucas", "", "123456")

    useCase.execute(input).value match
      case Some(Failure(exception)) =>
        assert(exception.getMessage == "Email cannot be empty")

    val input2 = RegisterCustomerUseCase.Input("André Lucas", "andrelucastic.com", "123456")
    useCase.execute(input2).value match
      case Some(Failure(exception)) =>
        assert(exception.getMessage == "Invalid email")
  }

  it should "save a customer and return customerId" in {
    val capture = ArgumentCaptor.forClass(classOf[Customer])
    val customerRepositoryMock = mock[CustomerRepository]
    val useCase = RegisterCustomerUseCase(customerRepositoryMock)
    val input = RegisterCustomerUseCase.Input("André Lucas", "andrelucas@gmail.com", "123456")

    useCase.execute(input).map { output =>
      verify(customerRepositoryMock).save(capture.capture())
      assert(capture.getValue.name == "André Lucas")
      assert(capture.getValue.email == "andrelucas@gmail.com")
      assert(capture.getValue.password == "123456")

      assert(output.customerId != null)
    }
  }
}
