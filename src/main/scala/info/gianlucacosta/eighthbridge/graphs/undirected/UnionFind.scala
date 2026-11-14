package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

object UnionFind {
  private def findRootAndCompress[V <: Vertex](
                                                vertex: V, rootByVertex: Map[V, V]
                                              ): (V, Map[V, V]) = {
    val root =
      rootByVertex(vertex)

    if (root == vertex)
      (vertex, rootByVertex)
    else {
      val (actualRoot, compressedRootByVertex) =
        findRootAndCompress(root, rootByVertex)

      (
        actualRoot,

        compressedRootByVertex +
          (vertex -> actualRoot)
      )
    }
  }
}

class UnionFind[V <: Vertex, E <: Edge] private(
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
    graph
      .getAnchors(edge)
      .map(anchors => {
        if (hasCycles)
          new UnionFind[V, E](
            graph,
            edges + edge,
            hasCycles,
            rootByVertex,
            rankByVertex
          )
        else {
          val (vi, vj) =
            anchors.toPair

          val (iRoot, jRoot, compressedRootByVertex) = {
            val (iRoot, compressedRootByVertexAfterI) =
              UnionFind.findRootAndCompress(vi, rootByVertex)

            val (jRoot, compressedRootByVertexAfterJ) = {
              UnionFind.findRootAndCompress(vj, compressedRootByVertexAfterI)
            }

            (iRoot, jRoot, compressedRootByVertexAfterJ)
          }

          val cyclesIntroduced =
            iRoot == jRoot

          val (updatedRootByVertex, updatedRankByVertex): (Map[V, V], Map[V, Int]) =
            if (rankByVertex(iRoot) < rankByVertex(jRoot))
              (
                compressedRootByVertex +
                  (iRoot -> jRoot),

                rankByVertex
              )
            else if (rankByVertex(iRoot) > rankByVertex(jRoot))
              (
                compressedRootByVertex +
                  (jRoot -> iRoot),

                rankByVertex
              )
            else
              (
                compressedRootByVertex +
                  (jRoot -> iRoot),

                rankByVertex +
                  (iRoot ->
                    (rankByVertex(iRoot) + 1)
                    )
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
        }
      })
      .getOrElse(
        this
      )
  }
}
