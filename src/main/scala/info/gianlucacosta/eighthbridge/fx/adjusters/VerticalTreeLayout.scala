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
        graph
          .getSuccessors(vertex)

      val minAggregateSuccessorBand =
        if (successors.nonEmpty) {
          successors.foreach(successor =>
            registerTreeMetrics(
              level + 1,
              successor
            )
          )

          val jammedSuccessorBand =
            successors
              .toList
              .map(bandByVertex)
              .sum

          println(s"??????? SUCCESSORS FOR ${vertex}: ${successors}")

          println(s"======= JAMMED SUCCESSOR BAND FOR ${vertex}: ${jammedSuccessorBand}")

          val minGapSpace =
            minHorizontalGap * (successors.size - 1)

          jammedSuccessorBand + minGapSpace
        } else
          0

      val band =
        Math.max(
          width,
          minAggregateSuccessorBand
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

      println(s"%%%%%%%%% VERTEX NODE FOR ${vertex} @ ${level}: ${width} x ${height}")
      println (s"!!!!!!!!! BAND FOR VERTEX ${vertex}: ${band}")
      println()
    }

    graph
      .rootVertexes
      .foreach(rootVertex =>
        registerTreeMetrics(0, rootVertex)
      )

    println(s"*****> MAX HEIGHT BY LEVEL: ${maxHeightByLevel}")

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

    println(s"*****> VERTICAL CENTER BY LEVEL: ${verticalCenterByLevel}")

    var updatedGraph =
      graph

    def positionVertexes(
                          level: Level,
                          parentBandLeft: Double,
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

      vertexes.foldLeft(parentBandLeft)((cumulatedLeft, vertex) => {
        val vertexBandLeft =
          cumulatedLeft

        val vertexBand =
          bandByVertex(vertex)

        val horizontalCenter =
          vertexBandLeft + vertexBand / 2

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
            parentBandLeft =
              vertexBandLeft,
            parentBand =
              vertexBand,
            vertexes =
              sortedSuccessors
          )
        }

        horizontalCenter + vertexBand / 2 + actualHorizontalGap
      })
    }

    val sortedRootVertexes =
      rootSorter(
        graph.rootVertexes
      )

    val jammedRootBand =
      sortedRootVertexes
        .map(bandByVertex)
        .sum

    val rootSpace =
      minHorizontalGap * (sortedRootVertexes.size - 1)

    val aggregateRootBand =
      jammedRootBand + rootSpace

    println(s"*****> AGGREGATE ROOT BAND: ${aggregateRootBand}")



    require(sortedRootVertexes.toSet == graph.rootVertexes)

    positionVertexes(
      level =
        0,
      parentBandLeft =
        0,
      parentBand =
        aggregateRootBand,
      vertexes =
        sortedRootVertexes
    )

    updatedGraph
  }
}
