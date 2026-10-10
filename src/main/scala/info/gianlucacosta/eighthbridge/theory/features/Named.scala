package info.gianlucacosta.eighthbridge.theory.features

import scala.language.implicitConversions

/**
 * Object having a name
 */
trait Named {
  def name: String
}


object Named {
  implicit class NamedIterableExtensions[T <: Named](iterable: Iterable[T]) {
    val sortedByName: List[T] = {
      iterable
        .toList
        .sortBy(_.name)
    }
  }

  implicit class NamedPairExtensions[T <: Named, U <: Named](pair: (T, U)) {
    val namePair: (String, String) =
      (pair._1.name, pair._2.name)

    val sortedNamePair: (String, String) =
      if (pair._1.name <= pair._2.name)
        (pair._1.name, pair._2.name)
      else
        (pair._2.name, pair._1.name)
  }
}