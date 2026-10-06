package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.point2point.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Binding, Graph, Link, Vertex}

import scala.language.implicitConversions
import info.gianlucacosta.helios.mathutils.Numbers

/**
  * Object having a weight
  */
trait Weighted {
  def minWeight: Double

  def maxWeight: Double

  def weight: Double

  checkWeight()

  //TODO! Should the check really be performed here? Or in the controller instead?
  /**
    * Ensures the weight is in the range [minWeight; maxWeight], throwing an IllegalArgumentException in case of errors
    */
  private def checkWeight(): Unit = {
    require(
      minWeight <= weight && weight <= maxWeight,
      s"Weight must be in [${Numbers.smartString(minWeight)}; ${Numbers.smartString(maxWeight)}]"
    )
  }

  /**
    * Copies the current object, giving it a new weight.
    *
    * @param weight The new weight
    * @return The resulting new object
    */
  def setWeight(weight: Double): this.type
}


object Weighted {
  implicit class IterableOfWeightedExtensions[T <: Weighted](iterable: Iterable[T]) {
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

  implicit class WeightedGraphExtensions[V <: Vertex, L <: Link with Weighted, B <: Binding](graph: Graph[V, L, B]) {
    def getMinWeightBetween(vertexes: Set[V]): Double = {
      graph.getLinksBetween(vertexes)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }

    def getMinWeightBetween(vertexes: V*): Double =
      graph.getMinWeightBetween(vertexes.toSet)
  }

  implicit class WeightedDirectedGraphExtensions[V <: Vertex, L <: Link with Weighted](graph: DirectedGraph[V, L]) {
    def getMinArcWeightBetween(sourceVertex: V, targetVertex: V): Double = {
      graph.getArcsBetween(sourceVertex, targetVertex)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }

    def getMinArcWeightBetween(vertexPair: (V,V)): Double = {
      graph.getArcsBetween(vertexPair)
        .minWeight
        .getOrElse(Double.PositiveInfinity)
    }
  }
}


