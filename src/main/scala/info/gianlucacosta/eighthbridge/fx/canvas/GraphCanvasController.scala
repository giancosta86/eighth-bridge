package info.gianlucacosta.eighthbridge.fx.canvas

import scalafx.geometry.Dimension2D


/**
  * Controller providing behavior for GraphCanvas
  */
trait GraphCanvasController[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] {
  def createBackgroundNode(graphCanvas: GraphCanvas[V, L, G]): BackgroundNode[V, L, G]

  def createVertexNode(graphCanvas: GraphCanvas[V, L, G], vertex: V): VertexNode[V, L, G]

  def createLinkNode(graphCanvas: GraphCanvas[V, L, G], sourceVertex: V, targetVertex: V, link: L): LinkNode[V, L, G]

  def deleteSelection(graphCanvas: GraphCanvas[V, L, G], graph: G): Option[G]

  def getCanvasDimension(graphCanvas: GraphCanvas[V, L, G]): Dimension2D

  def renderDirected: Boolean
}
