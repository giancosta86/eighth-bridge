package info.gianlucacosta.eighthbridge.graphs.point2point.specific

import scala.language.implicitConversions

/**
  * Object having a name
  */
trait Named[T <: Named[T]] {
  this: T =>
  def name: String

  /**
    * Copies the current object, giving it a new name.
    *
    * If you implement this trait as a "case class", you can implement this method just by using the Scala-provided copy() method.
    *
    * @param name The new name
    * @return The resulting new object
    */
  def nameCopy(name: String): T
}

object Named {
  implicit def toIterableNamedExtensions[T <: Named[T]](iterable: Iterable[T]) =
    IterableNamedExtensions(iterable)
}

case class IterableNamedExtensions[T <: Named[T]](private val iterable: Iterable[T]) {
  def sortedByName: List[T] = {
    iterable
      .toList
      .sortBy(_.name)
  }
}
