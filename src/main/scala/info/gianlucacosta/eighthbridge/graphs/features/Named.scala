package info.gianlucacosta.eighthbridge.graphs.features

import scala.language.implicitConversions

/**
  * Object having a name
  */
trait Named {
  def name: String

  /**
    * Copies the current object, giving it a new name.
    *
    * If you implement this trait as a "case class", you can implement this method just by using the Scala-provided copy() method,
    * casting it via `.toInstanceOf[this.type]`.
    *
    * @param name The new name
    * @return The resulting new object
    */
  def nameCopy(name: String): this.type
}


object Named {
  implicit class IterableOfNamedExtensions[T <: Named](private val iterable: Iterable[T]) {
    val sortedByName: List[T] = {
      iterable
        .toList
        .sortBy(_.name)
    }
  }
}