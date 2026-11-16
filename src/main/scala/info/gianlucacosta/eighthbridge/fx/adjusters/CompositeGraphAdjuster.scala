package info.gianlucacosta.eighthbridge.fx.adjusters

import info.gianlucacosta.eighthbridge.fx.{GraphAdjuster, GraphCanvas, VisualGraph}

class CompositeGraphAdjuster(adjusters: List[GraphAdjuster]) extends GraphAdjuster {
  override def adjustGraph(graphCanvas: GraphCanvas, graph: VisualGraph): VisualGraph = {
    adjusters
      .foldLeft(graph)((cumulatedGraph, adjuster) =>
        adjuster.adjustGraph(graphCanvas, cumulatedGraph)
      )
  }
}
