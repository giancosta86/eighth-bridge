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
  def isEmpty: Boolean =
    updatedVertexes.isEmpty &&
      updatedLinks.isEmpty &&
      addedVertexes.isEmpty &&
      addedLinks.isEmpty &&
      removedVertexes.isEmpty &&
      removedLinks.isEmpty

  def nonEmpty: Boolean =
    !isEmpty
}