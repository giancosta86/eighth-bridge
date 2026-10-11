package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualArc, VisualVertex}
import scalafx.scene.Node
import scalafx.Includes._

/**
  * Generic JavaFX node rendering a graph element into GraphCanvas.
  */
trait GraphCanvasNode extends Node {
  /**
    * The graph canvas owning this UI node
    *
    * @return
    */
  def graphCanvas: GraphCanvas


  /**
    * The controller of the owning graph canvas
    *
    * @return
    */
  final def controller: GraphCanvasController =
    graphCanvas.controller

  //TODO! Del this?
  /**
    * The current graph within the graph canvas
    *
    * @return
    */
  final def graph: AnyVisualGraph =
    graphCanvas.graph()

  //TODO! Del this!
  /**
    * Simple way to update the graph contained the graph canvas - thus triggering the rendering process.
    * When migrating from older versions of EighthBridge, use this in lieu of notifyGraphChanged()
    *
    * @param newGraph
    */
  /*final def graph_=(newGraph: G): Unit =
    graphCanvas.graph() = newGraph*/
}
