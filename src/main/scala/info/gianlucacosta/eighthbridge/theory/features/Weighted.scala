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

    val minWeight: Option[Double] =
      sortedByWeight
        .headOption
        .map(_.weight)

    val maxWeight: Option[Double] =
      sortedByWeight
        .lastOption
        .map(_.weight)
  }

  implicit class WeightedDirectedGraphExtensions[V <: Vertex, L <: Link with Weighted](graph: DirectedGraph[V, L]) {
    def getMinEdgeWeightBetween(oneVertex: V, anotherVertex: V): Double = {
      graph.getEdgesBetween(oneVertex, anotherVertex)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }

    def getMinArcWeightBetween(sourceVertex: V, targetVertex: V): Double = {
      graph.getArcsBetween(sourceVertex, targetVertex)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }

    def getMinArcWeightBetween(vertexPair: (V, V)): Double = {
      graph.getArcsBetween(vertexPair)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }
  }
}
