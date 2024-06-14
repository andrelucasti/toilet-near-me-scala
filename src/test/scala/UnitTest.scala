package io.andrelucas

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.mockito.MockitoSugar

import scala.concurrent.ExecutionContext

class UnitTest extends AnyFlatSpecLike with Matchers with MockitoSugar {
  given ec: ExecutionContext = ExecutionContext.global

}
