package info.gianlucacosta.eighthbridge.theory.features

import info.gianlucacosta.eighthbridge.theory.{Arc, Vertex}

case class TimestampedGraphDiff[V <: Vertex with Timestamped, A <: Arc with Timestamped](
                                                                                          addedVertexes: Set[V] = Set[V](),
                                                                                          addedArcs: Set[A] = Set[A](),
                                                                                          updatedVertexes: Set[V] = Set[V](),
                                                                                          updatedArcs: Set[A] = Set[A](),
                                                                                          removedVertexes: Set[V] = Set[V](),
                                                                                          removedArcs: Set[A] = Set[A]()
                               ) {
  def nonEmpty: Boolean =
    addedVertexes.nonEmpty ||
      addedArcs.nonEmpty ||
      updatedVertexes.nonEmpty ||
      updatedArcs.nonEmpty ||
      removedVertexes.nonEmpty ||
      removedArcs.nonEmpty
}