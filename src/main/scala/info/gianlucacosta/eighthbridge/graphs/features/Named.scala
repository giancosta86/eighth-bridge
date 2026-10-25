package info.gianlucacosta.eighthbridge.graphs.features

import scala.language.implicitConversions

/**
 * Object having a name
 */
trait Named {
  def name: String

  def setName(name: String): this.type
}


object Named {
  implicit class NamedIterableExtensions[T <: Named](self: Iterable[T]) {
    val sortedByName: List[T] = {
      self
        .toList
        .sortBy(_.name)
    }
  }

  implicit class NamedPairExtensions[T <: Named, U <: Named](self: (T, U)) {
    val namePair: (String, String) =
      (self._1.name, self._2.name)

    val sortedNamePair: (String, String) =
      if (self._1.name <= self._2.name)
        (self._1.name, self._2.name)
      else
        (self._2.name, self._1.name)
  }
}