package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
import scalafx.scene.{Group, Node}
import scalafx.Includes._

/**
  * Generic JavaFX node rendering a graph element into GraphCanvas.
  */
abstract class GraphCanvasNode extends Group {
  private var _graphCanvas: GraphCanvas = _

  /**
    * The graph canvas owning this UI node
    */
  protected[fx] def graphCanvas: GraphCanvas =
    _graphCanvas

  private[fx] def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    _graphCanvas = graphCanvas
  }

  /**
    * The controller of the owning graph canvas
    *
    * @return
    */
  protected final def controller: GraphCanvasController =
    graphCanvas.controller

  /**
    * The current graph within the graph canvas
    *
    * @return
    */
  protected final def graph: VisualGraph =
    graphCanvas.graph()

  /**
    * Simple way to update the graph contained the graph canvas - thus triggering the rendering process.
    * When migrating from older versions of EighthBridge, use this in lieu of notifyGraphChanged()
    */
  protected final def graph_=(newGraph: VisualGraph): Unit =
    graphCanvas.graph() = newGraph
}
