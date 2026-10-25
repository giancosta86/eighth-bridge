package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

import scala.language.implicitConversions

/**
 * Object having a weight
 */
trait Weighted {
  def weight: Weight

  def setWeight(weight: Weight): this.type
}


object Weighted {
  implicit class WeightedDirectedGraphExtensions[V <: Vertex, A <: Link with Weighted](self: DirectedGraph[V, A]) {
    def getMinArcBetween(source: V, target: V): Option[A] =
      self
        .getArcsBetween(source, target)
        .toList
        .sortBy(_.weight)
        .headOption

    def getMinArcWeightBetween(source: V, target: V): Weight =
      self
        .getMinArcBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.PositiveInfinity)

    def getMaxArcBetween(source: V, target: V): Option[A] =
      self
        .getArcsBetween(source, target)
        .toList
        .sortBy(_.weight)
        .lastOption

    def getMaxArcWeightBetween(source: V, target: V): Weight =
      self
        .getMaxArcBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.NegativeInfinity)
  }


  implicit class WeightedUndirectedGraphExtensions[V <: Vertex, E <: Link with Weighted](self: UndirectedGraph[V, E]) {
    def getMinEdgeBetween(oneVertex: V, anotherVertex: V): Option[E] =
      self
        .getEdgesBetween(oneVertex, anotherVertex)
        .toList
        .sortBy(_.weight)
        .headOption

    def getMinEdgeWeightBetween(oneVertex: V, anotherVertex: V): Weight =
      self
        .getMinEdgeBetween(oneVertex, anotherVertex)
        .map(_.weight)
        .getOrElse(Weight.PositiveInfinity)

    def getMaxEdgeBetween(oneVertex: V, anotherVertex: V): Option[E] =
      self
        .getEdgesBetween(oneVertex, anotherVertex)
        .toList
        .sortBy(_.weight)
        .lastOption

    def getMaxEdgeWeightBetween(oneVertex: V, anotherVertex: V): Weight =
      self
        .getMaxEdgeBetween(oneVertex, anotherVertex)
        .map(_.weight)
        .getOrElse(Weight.NegativeInfinity)
  }
}
