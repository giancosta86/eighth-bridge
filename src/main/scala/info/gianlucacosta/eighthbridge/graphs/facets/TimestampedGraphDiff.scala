package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

case class TimestampedGraphDiff[V <: Vertex with HasTimestamp, L <: Link with HasTimestamp](
                                                                                           addedVertexes: Set[V],
                                                                                           addedLinks: Set[L],
                                                                                           updatedVertexes: Set[V],
                                                                                           updatedLinks: Set[L],
                                                                                           removedVertexes: Set[V],
                                                                                           removedLinks: Set[L]
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

  override def toString: String =
    Map(
      "addedVertexes" ->
        addedVertexes.size,
      "addedLinks" ->
        addedLinks.size,
      "updatedVertexes" ->
        updatedVertexes.size,
      "updatedLinks" ->
        updatedLinks.size,
      "removedVertexes" ->
        removedVertexes.size,
      "removedLinks" ->
        removedLinks.size
    ).toString
}

object TimestampedGraphDiff {
  def forNewGraph[
    V <: Vertex with HasTimestamp,
    L <: Link with HasTimestamp
  ](graph: Graph[V, L]):
  TimestampedGraphDiff[V, L] =
    TimestampedGraphDiff(
      addedVertexes =
        graph.vertexes,
      addedLinks =
        graph.links,
      updatedVertexes =
        Set[V](),
      updatedLinks =
        Set[L](),
      removedVertexes =
        Set[V](),
      removedLinks =
        Set[L]()
    )
}