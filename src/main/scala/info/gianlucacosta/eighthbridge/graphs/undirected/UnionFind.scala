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

class UnionFind[V <: Vertex, L <: Link] private(
                                                 graph: UndirectedGraph[V, L],
                                                 val links: Set[L],
                                                 val hasCycles: Boolean,
                                                 rootByVertex: Map[V, V],
                                                 rankByVertex: Map[V, Int]
                                               ) {
  def this(graph: UndirectedGraph[V, L]) =
    this(
      graph =
        graph,
      links =
        Set[L](),
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

  def add(link: L): UnionFind[V, L] =
    if (hasCycles)
      new UnionFind[V, L](
        graph,
        links + link,
        hasCycles,
        rootByVertex,
        rankByVertex
      )
    else
      graph
        .getAnchors(link)
        .map(anchors => {
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

          new UnionFind[V, L](
            graph =
              graph,
            links =
              links + link,
            hasCycles =
              cyclesIntroduced,
            rootByVertex =
              updatedRootByVertex,
            rankByVertex =
              updatedRankByVertex
          )
        })
        .getOrElse(this)
}
