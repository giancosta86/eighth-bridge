package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.VisualGraph

trait GraphAdjuster {
  def adjustGraph(graphCanvas: GraphCanvas): VisualGraph
}
