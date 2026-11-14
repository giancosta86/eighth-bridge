package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}
import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeight {
  def weight: Weight

  override def toString: String =
    Numbers.smartString(weight)
}

object HasWeight {
  implicit class WeightedGraphExtensions[V <: Vertex, L <: Link with HasWeight](self: Graph[V, L]) {
    def getMinLinkBetween(source: V, target: V): Option[L] =
      self
        .getLinksBetween(source, target)
        .toList
        .sortBy(_.weight)
        .headOption

    def getMinWeightBetween(source: V, target: V): Weight =
      self
        .getMinLinkBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.PositiveInfinity)

    def getMaxLinkBetween(source: V, target: V): Option[L] =
      self
        .getLinksBetween(source, target)
        .toList
        .sortBy(_.weight)
        .lastOption

    def getMaxWeightBetween(source: V, target: V): Weight =
      self
        .getMaxLinkBetween(source, target)
        .map(_.weight)
        .getOrElse(Weight.NegativeInfinity)
  }
}
