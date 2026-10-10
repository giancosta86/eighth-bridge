package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{DirectedGraph, Link, Vertex}

import scala.language.implicitConversions

/**
 * Object having a weight
 */
trait Weighted {
  def weight: Double
}


object Weighted {
  implicit class WeightedIterableExtensions[T <: Weighted](iterable: Iterable[T]) {
    val sortedByWeight: List[T] = {
      iterable
        .toList
        .sortBy(_.weight)
    }

    def getMinWeightOr(default: Double): Double =
      sortedByWeight
        .headOption
        .map(_.weight)
        .getOrElse(default)

    val minWeight: Double =
      getMinWeightOr(Double.PositiveInfinity)

    def getMaxWeightOr(default: Double): Double =
      sortedByWeight
        .lastOption
        .map(_.weight)
        .getOrElse(default)

    val maxWeight: Double =
      getMaxWeightOr(Double.NegativeInfinity)
  }
}
