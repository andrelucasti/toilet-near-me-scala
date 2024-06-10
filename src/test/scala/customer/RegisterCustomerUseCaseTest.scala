package io.andrelucas
package customer


import customer.usecases.CustomerRepository

import org.mockito.ArgumentCaptor
import org.mockito.Mockito.verify
import org.scalatest.flatspec.AsyncFlatSpecLike
import org.scalatestplus.mockito.MockitoSugar

class RegisterCustomerUseCaseTest extends AsyncFlatSpecLike with MockitoSugar {
  val customerRepositoryMock: CustomerRepository = mock[CustomerRepository]
  val mockSaveCustomer = (customer: Customer) => customerRepositoryMock.save(customer)

  it should "not register a customer with invalid email" in {
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("André Lucas", "andrelucas.com", "123456")

    val eventualOutput = useCase.execute(input, mockSaveCustomer)
    eventualOutput.failed.map{ exception =>
      assert(exception.getMessage == "Invalid email")
    }
  }

  it should "not register a customer name is empty" in {
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("", "andrelucastic@gmail.com", "123456")
    val output = useCase.execute(input, mockSaveCustomer)

    output.failed.map { exception =>
      assert(exception.getMessage == "Name cannot be empty")
    }
  }

  it should "not register a customer email is invalid" in {
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("André Lucas", "", "123456")

    useCase.execute(input, mockSaveCustomer).failed.map(exception => assert(exception.getMessage == "Email cannot be empty"))

    val input2 = RegisterCustomerUseCase.Input("André Lucas", "andrelucastic.com", "123456")
    useCase.execute(input2, mockSaveCustomer).failed.map(exception => assert(exception.getMessage == "Invalid email"))
  }

  it should "save a customer and return customerId" in {
    val capture = ArgumentCaptor.forClass(classOf[Customer])
    val useCase = RegisterCustomerUseCase()
    val input = RegisterCustomerUseCase.Input("André Lucas", "andrelucas@gmail.com", "123456")

    useCase.execute(input, mockSaveCustomer).map { output =>
      verify(customerRepositoryMock).save(capture.capture())
      assert(capture.getValue.name == "André Lucas")
      assert(capture.getValue.email == "andrelucas@gmail.com")
      assert(capture.getValue.password == "123456")

      assert(output.customerId != null)
    }
  }
}
