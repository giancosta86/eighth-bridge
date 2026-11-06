package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

import scala.annotation.tailrec

object Tree {
  type Level =
    Int

  type RootSorter[V <: Vertex] =
    (Set[V]) => List[V]

  type SuccessorSorter[V <: Vertex] =
    (Level, V, Set[V]) => List[V]

  implicit class DirectedGraphExtensions[V <: Vertex, A <: Link](self: DirectedGraph[V, A]) {
    def getVertexesByLevel(
                          rootSorter: RootSorter[V],
                          successorSorter: SuccessorSorter[V]
                          ): Option[Map[Level, List[V]]] = {

      if (self.isCyclic)
        None
      else {
        val sortedRoots =
          rootSorter(
            self.rootVertexes
          )

        val rootMap: Map[Level, List[V]] =
          Map(0 -> sortedRoots)

        Some(
          getVertexesByLevel(
            currentLevel =
              0,
            cumulatedMap =
              rootMap,
            successorSorter =
              successorSorter
          )
        )
      }
    }

    @tailrec
    private def getVertexesByLevel(
                                    currentLevel: Level,
                                    cumulatedMap: Map[Level, List[V]],
                                    successorSorter: SuccessorSorter[V]
                                  ): Map[Level, List[V]] = {
      val currentSortedVertexes: List[V] =
        cumulatedMap(currentLevel)

      val sortedNextVertexes: List[V] =
        currentSortedVertexes.foldLeft(List[V]())(
          (cumulatedNextVertexes, currentVertex) => {
            val sortedSuccessors: List[V] =
              successorSorter(
                currentLevel,
                currentVertex,
                self.getSuccessors(currentVertex)
              )

            cumulatedNextVertexes ++ sortedSuccessors
          })

      val nextLevel =
        currentLevel + 1

      if (sortedNextVertexes.nonEmpty) {
        getVertexesByLevel(
          currentLevel =
            nextLevel,
          cumulatedMap =
            cumulatedMap +
              (nextLevel -> sortedNextVertexes),
          successorSorter =
            successorSorter
        )
      } else
        cumulatedMap
    }
  }
}
