package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

case class TimestampedGraphDiff[V <: Vertex with Timestamped, A <: Link with Timestamped](
                                                                                          addedVertexes: Set[V] = Set[V](),
                                                                                          addedArcs: Set[A] = Set[A](),
                                                                                          updatedVertexes: Set[V] = Set[V](),
                                                                                          updatedArcs: Set[A] = Set[A](),
                                                                                          removedVertexes: Set[V] = Set[V](),
                                                                                          removedArcs: Set[A] = Set[A]()
                               ) {
  def nonEmpty: Boolean =
    updatedVertexes.nonEmpty ||
      updatedArcs.nonEmpty ||
      addedVertexes.nonEmpty ||
      addedArcs.nonEmpty ||
      removedVertexes.nonEmpty ||
      removedArcs.nonEmpty
}