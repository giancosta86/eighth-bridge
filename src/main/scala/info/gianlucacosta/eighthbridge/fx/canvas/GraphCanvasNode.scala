package info.gianlucacosta.eighthbridge.fx.canvas

import scalafx.scene.Node

/**
  * Generic JavaFX node rendering a graph element
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
  def controller: GraphCanvasController[V, L, G] =
    graphCanvas.controller


  /**
    * The current graph within the graph canvas
    *
    * @return
    */
  def graph: G =
    graphCanvas.graph


  /**
    * Simple way to update the graph contained the graph canvas - thus triggering the rendering process.
    * When migrating from older versions of EighthBridge, use this in lieu of notifyGraphChanged()
    *
    * @param newGraph
    */
  def graph_=(newGraph: G): Unit =
    graphCanvas.graph = newGraph


  /**
    * Used by GraphCanvas to draw the node whenever rendering is performed
    */
  def render(): Unit
}
