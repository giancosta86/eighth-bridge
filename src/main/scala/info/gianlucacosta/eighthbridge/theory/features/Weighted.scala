package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{DirectedGraph, Arc, Vertex}

import scala.language.implicitConversions

/**
 * Object having a weight
 */
trait Weighted {
  def weight: Weight

  def setWeight(weight: Weight): this.type
}


object Weighted {
  //TODO! Consider deleting this!
  /*
  implicit class WeightedIterableExtensions[T <: Weighted](self: Iterable[T]) {

    val sortedByWeight: List[T] = {
      self
        .toList
        .sortBy(_.weight)
    }

    def getMinWeightOr(default: Weight): Weight =
      sortedByWeight
        .headOption
        .map(_.weight)
        .getOrElse(default)

    val minWeight: Weight =
      getMinWeightOr(Weight.PositiveInfinity)

    def getMaxWeightOr(default: Weight): Weight =
      sortedByWeight
        .lastOption
        .map(_.weight)
        .getOrElse(default)

    val maxWeight: Weight =
      getMaxWeightOr(Weight.NegativeInfinity)
  }

  implicit class IterableOfWeightedTupleExtensions[T](self: Iterable[(T, Weight)]) {
    val sortedByWeight: List[(T, Weight)] =
      self
        .toList
        .sortBy(_._2)
  }
  */
  implicit class WeightedDirectedGraphExtensions[V <: Vertex, A <: Arc with Weighted](self: DirectedGraph[V, A]) {
    def getMinArcBetween(source: V, target: V): Option[A] =
      self
        .getArcsBetween(source, target)
        .toList
        .sortBy(_.weight)
        .headOption

    def getMinEdgeBetween(oneVertex: V, anotherVertex: V): Option[A] =
      self
        .getEdgesBetween(oneVertex, anotherVertex)
        .toList
        .sortBy(_.weight)
        .headOption

    def getMinArcWeightBetween(source: V, target: V): Weight =
      self
        .getMinArcBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.PositiveInfinity)

    def getMinEdgeWeightBetween(oneVertex: V, anotherVertex: V): Weight =
      self
        .getMinEdgeBetween(oneVertex, anotherVertex)
        .map(_.weight)
        .getOrElse(Weight.PositiveInfinity)

    def getMaxArcBetween(source: V, target: V): Option[A] =
      self
        .getArcsBetween(source, target)
        .toList
        .sortBy(_.weight)
        .lastOption

    def getMaxEdgeBetween(oneVertex: V, anotherVertex: V): Option[A] =
      self
        .getEdgesBetween(oneVertex, anotherVertex)
        .toList
        .sortBy(_.weight)
        .lastOption

    def getMaxArcWeightBetween(source: V, target: V): Weight =
      self
        .getMaxArcBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.NegativeInfinity)

    def getMaxEdgeWeightBetween(oneVertex: V, anotherVertex: V): Weight =
      self
        .getMaxEdgeBetween(oneVertex, anotherVertex)
        .map(_.weight)
        .getOrElse(Weight.NegativeInfinity)
  }
}
