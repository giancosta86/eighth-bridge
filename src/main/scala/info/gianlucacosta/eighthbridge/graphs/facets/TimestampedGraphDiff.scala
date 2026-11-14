package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

case class TimestampedGraphDiff[V <: Vertex with HasTimestamp, L <: Link with HasTimestamp](
   updatedVertexes: Set[V],
   updatedLinks: Set[L],
   addedVertexes: Set[V],
   addedLinks: Set[L],
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
      "updatedVertexes" ->
        updatedVertexes.size,
      "updatedLinks" ->
        updatedLinks.size,
      "addedVertexes" ->
        addedVertexes.size,
      "addedLinks" ->
        addedLinks.size,
      "removedVertexes" ->
        removedVertexes.size,
      "removedLinks" ->
        removedLinks.size
    ).toString
}

object TimestampedGraphDiff {
  def forSingleGraph[
    V <: Vertex
      with HasTimestamp,
    L <: Link
      with HasTimestamp
  ](graph: Graph[V, L]):
  TimestampedGraphDiff[V, L] =
    TimestampedGraphDiff(
      updatedVertexes =
        Set[V](),
      updatedLinks =
        Set[L](),
      addedVertexes =
        graph.vertexes,
      addedLinks =
        graph.links,
      removedVertexes =
        Set[V](),
      removedLinks =
        Set[L]()
    )
}