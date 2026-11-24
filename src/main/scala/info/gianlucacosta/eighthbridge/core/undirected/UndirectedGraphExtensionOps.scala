package info.gianlucacosta.eighthbridge.core.undirected

import info.gianlucacosta.eighthbridge.core.{Graph, Vertex}

trait UndirectedGraphExtensionOps[V <: Vertex, E <: Edge] {
  val self: Graph[V, E]

  def isCyclic: Boolean = {
    var unionFind =
      new UnionFind[V, E](self)

    self.links
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

  def isComplete: Boolean =
    self.vertexes
      .forall(
        self.getNeighbors(_).size == self.vertexes.size - 1
      )
}
