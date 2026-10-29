package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

case class TimestampedGraphDiff[V <: Vertex with Timestamped, A <: Link with Timestamped](
                                                                                           addedVertexes: Set[V] = Set[V](),
                                                                                           addedLinks: Set[A] = Set[A](),
                                                                                           updatedVertexes: Set[V] = Set[V](),
                                                                                           updatedLinks: Set[A] = Set[A](),
                                                                                           removedVertexes: Set[V] = Set[V](),
                                                                                           removedLinks: Set[A] = Set[A]()
                               ) {
  def nonEmpty: Boolean =
    updatedVertexes.nonEmpty ||
      updatedLinks.nonEmpty ||
      addedVertexes.nonEmpty ||
      addedLinks.nonEmpty ||
      removedVertexes.nonEmpty ||
      removedLinks.nonEmpty

  def isEmpty: Boolean =
    !nonEmpty
}