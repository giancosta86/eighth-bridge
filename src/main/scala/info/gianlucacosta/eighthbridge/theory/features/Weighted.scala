package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{DirectedGraph, Arc, Vertex}

import scala.language.implicitConversions

/**
 * Object having a weight
 */
trait Weighted {
  def weight: Double

  def setWeight(weight: Double): this.type
}


object Weighted {
  implicit class WeightedIterableExtensions[T <: Weighted](self: Iterable[T]) {
    val sortedByWeight: List[T] = {
      self
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

  implicit class IterableOfWeightedTupleExtensions[T](self: Iterable[(T, Double)]) {
    val sortedByWeight: List[(T, Double)] =
      self
        .toList
        .sortBy(_._2)
  }
}
