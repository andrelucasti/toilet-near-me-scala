package io.andrelucas

import java.util.UUID

trait IdGenerator:
  def generator: UUID = UUID.randomUUID()
