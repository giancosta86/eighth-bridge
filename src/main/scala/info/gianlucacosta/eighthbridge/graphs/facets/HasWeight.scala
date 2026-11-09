package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

trait HasWeight {
  def minWeight: Weight
  def weight: Weight
  def maxWeight: Weight
}

//TODO!
/*
private def formatLabel(weight: Weight, maxWeight: Weight): String =
  if (maxWeight < Weight.PositiveInfinity)
    s"(${Numbers.smartString(weight)}; ${Numbers.smartString(maxWeight)}"
  else
    Numbers.smartString(weight)
*/

object HasWeight {
  implicit class WeightedGraphExtensions[V <: Vertex, A <: Link with HasWeight](self: Graph[V, A]) {
    def getMinLinkBetween(source: V, target: V): Option[A] =
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

    def getMaxLinkBetween(source: V, target: V): Option[A] =
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
