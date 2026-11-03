package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

object UnionFind {
  private def findRootAndCompress[V <: Vertex](vertex: V, rootByVertex: Map[V, V]): (V, Map[V,V]) = {
    val root =
      rootByVertex(vertex)

    if (root == vertex)
      (vertex, rootByVertex)
    else {
      val (actualRoot, updatedRootByVertex) =
        findRootAndCompress(root, rootByVertex)

      (
        actualRoot,

        updatedRootByVertex +
          (vertex -> actualRoot)
      )
    }
  }
}

class UnionFind[V <: Vertex, E <: Link] private (
                                                       graph: UndirectedGraph[V, E],
                                                       val edges: Set[E],
                                                       val hasCycles: Boolean,
                                                       rootByVertex: Map[V, V],
                                                       rankByVertex: Map[V, Int]
                                               ) {
  def this(graph: UndirectedGraph[V, E]) =
    this(
      graph =
        graph,
      edges =
        Set[E](),
      hasCycles =
        false,
      rootByVertex =
        graph
          .vertexes
          .map(vertex =>
            vertex -> vertex
          )
          .toMap,
      rankByVertex =
        graph
          .vertexes
          .map(_ -> 0)
          .toMap
    )



  def add(edge: E): UnionFind[V, E] = {
    if (hasCycles && graph.links.contains(edge))
      new UnionFind[V, E](
        graph,
        edges + edge,
        hasCycles,
        rootByVertex,
        rankByVertex
      )
    else
      graph
        .getAnchorSet(edge)
        .map(anchorSet => {
          val (vi, vj): (V, V) =
            anchorSet

          val (iRoot, updatedRootByVertexAfterI) =
            UnionFind.findRootAndCompress(vi, rootByVertex)

          val (jRoot, updatedRootByVertexAfterJ) =
            UnionFind.findRootAndCompress(vj, updatedRootByVertexAfterI)

          val cyclesIntroduced =
            iRoot == jRoot

          val (updatedRootByVertex, updatedRankByVertex): (Map[V, V], Map[V, Int]) =
            if (rankByVertex(iRoot) < rankByVertex(jRoot))
              (
                updatedRootByVertexAfterJ +
                  (iRoot -> jRoot),

                rankByVertex
              )
            else if (rankByVertex(iRoot) > rankByVertex(jRoot))
              (
                updatedRootByVertexAfterJ +
                  (jRoot -> iRoot),

                rankByVertex
              )
            else
              (
                updatedRootByVertexAfterJ +
                  (jRoot -> iRoot),

                rankByVertex +
                  (iRoot -> (rankByVertex(iRoot) + 1))
              )

          new UnionFind[V, E](
            graph =
              graph,
            edges =
              edges + edge,
            hasCycles =
              cyclesIntroduced,
            rootByVertex =
              updatedRootByVertex,
            rankByVertex =
              updatedRankByVertex
          )

          this
        })
        .getOrElse(this)
  }
}
