package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.{GraphCore, Link, Vertex}

final class UndirectedGraph[
  V <: Vertex,
  L <: Link
] private(
  vertexes: Set[V],
  linksByAnchors: Map[UndirectedAnchors[V], Set[L]]
) extends GraphCore[V, L, UndirectedAnchors[V]](
  vertexes,
  linksByAnchors
) {
  def this() = this(
    vertexes =
      Set[V](),
    linksByAnchors =
      Map[UndirectedAnchors[V], Set[L]]()
  )

  override protected def copy(
     updatedVertexes: Set[V],
     updatedLinksByAnchors: Map[UndirectedAnchors[V], Set[L]]
  ): this.type =
    new UndirectedGraph[V, L](
      vertexes =
        updatedVertexes,
      linksByAnchors =
        updatedLinksByAnchors
    )
      .asInstanceOf[this.type]

  override protected def createAnchors(first: V, second: V): UndirectedAnchors[V] =
    new UndirectedAnchors[V](first, second)

  override def addLink(oneVertex: V, anotherVertex: V, link: L): this.type =
    super.addLink(oneVertex, anotherVertex, link)

  @transient
  override lazy val isCyclic: Boolean = {
    var unionFind =
      new UnionFind[V, L](this)

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
