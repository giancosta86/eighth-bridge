package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex

package object adjusters {
  type Level =
    Int

  type RootSorter =
    (Set[VisualVertex]) => List[VisualVertex]

  val DefaultRootSorter: RootSorter =
    (rootVertexes) =>
      rootVertexes.toList

  type SuccessorSorter =
    (Level, VisualVertex, Set[VisualVertex]) => List[VisualVertex]

  val DefaultSuccessorSorter: SuccessorSorter =
    (level, parent, vertexes) =>
      vertexes.toList
}
