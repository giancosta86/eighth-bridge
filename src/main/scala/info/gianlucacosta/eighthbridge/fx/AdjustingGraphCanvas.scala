package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.facets.TimestampedGraphDiff
import scalafx.Includes._
import scalafx.application.Platform

abstract class AdjustingGraphCanvas(
                            controller: GraphCanvasController,
                            initialGraph: VisualGraph
                           ) extends GraphCanvas(controller, initialGraph) {

  private var adjustmentRequested =
    false

  override protected def render(updatedGraph: VisualGraph, graphDiff: TimestampedGraphDiff[VisualVertex, VisualLink]): Unit = {
    super.render(updatedGraph, graphDiff)

    if (!adjustmentRequested) {
      println("%%%% REQUESTING ADJUSTMENT!")

      Platform.runLater {
        val adjustedGraph =
          adjustGraph(updatedGraph)

        graph() =
          adjustedGraph
      }

      adjustmentRequested =
        true
    }
  }

  protected def adjustGraph(graph: VisualGraph): VisualGraph
}
