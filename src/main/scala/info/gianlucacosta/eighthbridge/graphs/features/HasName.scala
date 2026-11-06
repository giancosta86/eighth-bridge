package info.gianlucacosta.eighthbridge.graphs.features

import scala.language.implicitConversions

/**
 * Object having a name
 */
trait HasName {
  def name: String

  override def toString: String =
    name
}

object HasName {
  implicit class NamedPairExtensions[T <: HasName, U <: HasName](self: (T, U)) {
    val namePair: (String, String) =
      (self._1.name, self._2.name)

    val sortedNamePair: (String, String) =
      if (self._1.name <= self._2.name)
        (self._1.name, self._2.name)
      else
        (self._2.name, self._1.name)
  }
}