package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.{Graph, GraphCore, Link, Vertex}

import scala.language.implicitConversions

object UndirectedGraph {
  implicit def toUndirectedGraph[
    TGraphVertex <: Vertex,
    TGraphLink <: Link,
    TUndirectedGraphVertex <: TGraphVertex,
    TUndirectedGraphEdge <: TGraphLink with Edge
  ](self: Graph[TGraphVertex, TGraphLink]): UndirectedGraph[TUndirectedGraphVertex, TUndirectedGraphEdge] =
    self.asInstanceOf[UndirectedGraph[TUndirectedGraphVertex, TUndirectedGraphEdge]]

  implicit def toGraph[
    TGraphVertex <: Vertex,
    TGraphLink <: Link,
    TUndirectedGraphVertex <: TGraphVertex,
    TUndirectedGraphEdge <: TGraphLink with Edge
  ](self: UndirectedGraph[TUndirectedGraphVertex, TUndirectedGraphEdge]): Graph[TGraphVertex, TGraphLink] =
    self.asInstanceOf[Graph[TGraphVertex, TGraphLink]]

  implicit def toParent[
    TParentVertex <: Vertex,
    TParentEdge <: Edge,
    TDescendantVertex <: TParentVertex,
    TDescendantEdge <: TParentEdge
  ](descendant: UndirectedGraph[TDescendantVertex, TDescendantEdge]): UndirectedGraph[TParentVertex, TParentEdge] =
    descendant.asInstanceOf[UndirectedGraph[TParentVertex, TParentEdge]]
}

final class UndirectedGraph[
  V <: Vertex,
  E <: Edge
] private(
  vertexes: Set[V],
  linksByAnchors: Map[UndirectedAnchors[V], Set[E]]
) extends GraphCore[V, E, UndirectedAnchors[V]](
  vertexes,
  linksByAnchors
) {
  def this() = this(
    vertexes =
      Set[V](),
    linksByAnchors =
      Map[UndirectedAnchors[V], Set[E]]()
  )

  override protected def copy(
     updatedVertexes: Set[V],
     updatedLinksByAnchors: Map[UndirectedAnchors[V], Set[E]]
  ): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        updatedVertexes,
      linksByAnchors =
        updatedLinksByAnchors
    )
      .asInstanceOf[this.type]

  override protected def createAnchors(first: V, second: V): UndirectedAnchors[V] =
    new UndirectedAnchors[V](first, second)

  override def addLink(oneVertex: V, anotherVertex: V, linkToAdd: E): this.type =
    super.addLink(oneVertex, anotherVertex, linkToAdd)

  @transient
  override lazy val isCyclic: Boolean = {
    var unionFind =
      new UnionFind[V, E](this)

    links
      .view
      .takeWhile(_ =>
        !unionFind.hasCycles
      )
      .foreach(link =>
        unionFind =
          unionFind.add(link)
      )

    unionFind.hasCycles
  }
}
