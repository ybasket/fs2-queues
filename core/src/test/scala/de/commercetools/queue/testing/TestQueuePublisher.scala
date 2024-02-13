package de.commercetools.queue.testing

import cats.effect.IO
import de.commercetools.queue.QueuePublisher

import scala.concurrent.duration.FiniteDuration

class TestQueuePublisher[T](queue: TestQueue[T]) extends QueuePublisher[T] {

  override def publish(message: T, delay: Option[FiniteDuration]): IO[Unit] =
    queue.enqeueMessages(message :: Nil, delay)

  override def publish(messages: List[T], delay: Option[FiniteDuration]): IO[Unit] =
    queue.enqeueMessages(messages, delay)

}
