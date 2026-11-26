package info.gianlucacosta.eighthbridge.core.facets

import info.gianlucacosta.eighthbridge.core.{Graph, Link, Vertex}
import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeight {
  def weight: Weight

  override def toString: String =
    Numbers.smartString(weight)
}

object HasWeight {
  implicit class WeightedIterableExtensions(self: Iterable[HasWeight]) {
    def totalWeight: Weight =
      self
        .map(_.weight)
        .sum
  }

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
