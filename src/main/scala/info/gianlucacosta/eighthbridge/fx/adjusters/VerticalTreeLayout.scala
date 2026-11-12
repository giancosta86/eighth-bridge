package info.gianlucacosta.eighthbridge.fx.adjusters

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphAdjuster}
import info.gianlucacosta.eighthbridge.graphs.facets.HasCenterSettable
import scalafx.Includes._

class VerticalTreeLayout(
                            minHorizontalGap: Double =
                              40,
                            verticalGap: Double =
                              60,
                            leftPadding: Double =
                              15,
                            topPadding: Double =
                              15,
                            rootSorter: RootSorter =
                              DefaultRootSorter,
                            successorSorter: SuccessorSorter =
                              DefaultSuccessorSorter
                          ) extends GraphAdjuster {
  override def adjustGraph(graphCanvas: GraphCanvas): VisualGraph = {
    val graph =
      graphCanvas.graph()

    if (graph.isEmpty) {
      return graph
    }

    var bandByVertex: Map[VisualVertex, Double] =
      Map[VisualVertex, Double]()

    var maxHeightByLevel: Map[Level, Double] =
      Map[Level, Double]()

    def registerTreeMetrics(level: Level, vertex: VisualVertex): Unit = {
      val vertexNode =
        graphCanvas.vertexNodesByVertex(vertex)

      val width =
        vertexNode.width()

      val successors =
        graph.getSuccessors(vertex)

      val aggregateSuccessorBand =
        if (successors.nonEmpty) {
          successors.foreach(successor =>
            registerTreeMetrics(
              level + 1,
              successor
            )
          )

          val jammedSuccessorBand =
            successors
              .map(bandByVertex)
              .sum

          val minGapSpace =
            minHorizontalGap * (successors.size - 1)

          jammedSuccessorBand + minGapSpace
        } else
          0

      val band =
        Math.max(
          width,
          aggregateSuccessorBand
        )

      require(
        !bandByVertex.contains(vertex),
        "The graph is not a tree!"
      )

      bandByVertex +=
        (vertex -> band)

      val height =
        vertexNode.height()

      if (height >= maxHeightByLevel.getOrElse(level, 0.0)) {
        maxHeightByLevel +=
          (level -> height)
      }
    }

    graph
      .rootVertexes
      .foreach(rootVertex =>
        registerTreeMetrics(0, rootVertex)
      )

    val levelCount =
      maxHeightByLevel.size

    val topByLevel: Map[Level, Double] =
      Range(1, levelCount)
        .foldLeft(List(0 -> 0.0))((cumulatedTuples, level) => {
          val previousTop =
            cumulatedTuples.head._2

          val previousHeight =
            maxHeightByLevel(level - 1)

          val currentTop =
            previousTop + previousHeight + verticalGap

          (level -> currentTop) :: cumulatedTuples
        })
        .toMap

    val verticalCenterByLevel: Map[Level, Double] =
      Range(0, levelCount)
        .map(level => {
          val verticalCenter =
            topByLevel(level) + maxHeightByLevel(level) / 2

          level -> verticalCenter
        })
        .toMap

    var updatedGraph =
      graph

    def positionVertexes(
                          level: Level,
                          parentLeft: Double,
                          parentBand: Double,
                          vertexes: List[VisualVertex]
                        ): Unit = {
      val verticalCenter =
        verticalCenterByLevel(level)

      val jammedBand: Double =
        vertexes.map(bandByVertex).sum

      val freeSpaceInParentBand =
        parentBand - jammedBand

      val actualHorizontalGap =
        freeSpaceInParentBand / (vertexes.size - 1)

      vertexes.foldLeft(parentLeft)((cumulatedLeft, vertex) => {
        val vertexLeft =
          cumulatedLeft

        val vertexBand =
          bandByVertex(vertex)

        val horizontalCenter =
          vertexLeft + vertexBand / 2

        val updatedVertex =
          vertex
            .asInstanceOf[VisualVertex with HasCenterSettable]
            .setCenter(
              x =
                horizontalCenter + leftPadding,
              y =
                verticalCenter + topPadding
            )

        updatedGraph =
          updatedGraph.updateVertex(updatedVertex)

        val successors =
          updatedGraph.getSuccessors(vertex)

        if (successors.nonEmpty) {
          val sortedSuccessors =
            successorSorter(level + 1, vertex, successors)

          require(sortedSuccessors.toSet == successors)

          positionVertexes(
            level =
              level + 1,
            parentLeft =
              vertexLeft,
            parentBand = vertexBand,
            vertexes =
              sortedSuccessors
          )
        }

        horizontalCenter + vertexBand / 2 + actualHorizontalGap
      })
    }

    val rootVertexes =
      graph.rootVertexes

    val jammedRootBand =
      rootVertexes
        .map(bandByVertex)
        .sum

    val rootSpace =
      minHorizontalGap * (rootVertexes.size - 1)

    val aggregateRootBand =
      jammedRootBand + rootSpace

    val sortedRootVertexes =
      rootSorter(rootVertexes)

    require(sortedRootVertexes.toSet == rootVertexes)

    positionVertexes(
      level =
        0,
      parentLeft =
        0,
      parentBand =
        aggregateRootBand,
      vertexes =
        sortedRootVertexes
    )

    updatedGraph
  }
}
