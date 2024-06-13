package io.andrelucas
package customer


import application.customer.Customer
import application.customer.repository.CustomerRepository
import application.customer.usecases.RegisterCustomerUseCase

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify

import scala.concurrent.{ExecutionContext, Future}

class RegisterCustomerUseCaseTest extends UnitTest {
  given ec: ExecutionContext = ExecutionContext.global

  val customerRepositoryMock: CustomerRepository = mock[CustomerRepository]
  val saveCustomer: Customer => Future[Unit] = (customer: Customer) => customerRepositoryMock.save(customer)
  val passwordGenerator: () => String = () => "123456"

  it should "not register a customer name is empty" in {
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("", "andrelucastic@gmail.com", saveCustomer, passwordGenerator)
    val output = useCase.execute(input)

    output.failed.map { exception =>
      assert(exception.getMessage == "Name cannot be empty")
    }
  }

  it should "not register a customer email is invalid" in {
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("André Lucas", "", saveCustomer, passwordGenerator)

    useCase.execute(input).failed.map(exception => assert(exception.getMessage == "Email cannot be empty"))

    val input2 = RegisterCustomerUseCase.Input("André Lucas", "andrelucastic.com", saveCustomer, passwordGenerator)
    useCase.execute(input2).failed.map(exception => assert(exception.getMessage == "Invalid email"))
  }

  it should "save a customer and return customerId" in {
    val capture = ArgumentCaptor.forClass(classOf[Customer])
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("André Lucas", "andrelucas@gmail.com", saveCustomer, passwordGenerator)

    useCase.execute(input).map { output =>
      verify(customerRepositoryMock).save(capture.capture())
      assert(capture.getValue.name == "André Lucas")
      assert(capture.getValue.email == "andrelucas@gmail.com")
      assert(capture.getValue.password == "123456")

      assert(output.customerId != null)
    }
  }
}
