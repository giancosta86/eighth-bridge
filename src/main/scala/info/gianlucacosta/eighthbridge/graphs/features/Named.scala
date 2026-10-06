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
   * @param name The new name
   * @return The resulting new object
   */
  def setName(name: String): this.type
}


object Named {
  implicit class IterableOfNamedExtensions[T <: Named](iterable: Iterable[T]) {
    val sortedByName: List[T] = {
      iterable
        .toList
        .sortBy(_.name)
    }
  }

  implicit class PairOfNamedExtensions[T <: Named, U <: Named](pair: (T, U)) {
    val namePair: (String, String) =
      (pair._1.name, pair._2.name)
  }
}