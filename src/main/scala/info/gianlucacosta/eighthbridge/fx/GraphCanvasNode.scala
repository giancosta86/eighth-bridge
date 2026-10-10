package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import scalafx.scene.Node
import scalafx.Includes._

/**
  * Generic JavaFX node rendering a graph element into GraphCanvas.
  */
trait GraphCanvasNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] extends Node {
  /**
    * The graph canvas owning this UI node
    *
    * @return
    */
  def graphCanvas: GraphCanvas[V, L, G]


  /**
    * The controller of the owning graph canvas
    *
    * @return
    */
  final def controller: GraphCanvasController[V, L, G] =
    graphCanvas.controller

  //TODO! Del this?
  /**
    * The current graph within the graph canvas
    *
    * @return
    */
  final def graph: G =
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
