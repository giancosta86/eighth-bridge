package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.helios.fx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}

/**
  * Controller providing behavior for GraphCanvas
  */
trait GraphCanvasController[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] {
  def renderDirected: Boolean

  def createBackgroundNode(graphCanvas: GraphCanvas[V, L, G]): BackgroundNode[V, L, G] =
    new DefaultBackgroundNode(graphCanvas)

  def createVertexNode(graphCanvas: GraphCanvas[V, L, G], vertex: V): VertexNode[V, L, G] =
    new DefaultVertexNode(graphCanvas)

  def createLinkNode(graphCanvas: GraphCanvas[V, L, G], sourceVertex: V, targetVertex: V, link: L): LinkNode[V, L, G] =
    new DefaultLinkNode(graphCanvas, sourceVertex.id, targetVertex.id)

  def deleteSelection(graphCanvas: GraphCanvas[V, L, G], graph: G): Option[G]

  def createVertex(graph: G, center: Point2D): Option[G]

  def createLink(graph: G, sourceVertex: V, targetVertex: V): Option[G]


  def editVertex(graph: G, vertex: V): Option[G]

  def editLink(graph: G, link: L): Option[G]


  def canDrawSelectionRectangle: Boolean

  def setVertexSelectedState(graph: G, vertex: V, selected: Boolean): Option[G]

  def setLinkSelectedState(graph: G, link: L, selected: Boolean): Option[G]

  def setSelection(graph: G, selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G]


  def dragSelection(graphCanvas: GraphCanvas[V, L, G], delta: Point2D): Option[G] = {
    val graph =
      graphCanvas.graph

    Some(
      graph.replaceVertexes(
        graph.selectedVertexes.map(vertex => {
          val newCenter =
            (vertex.center + delta).clip(graphCanvas.dimension)

          vertex.visualCopy(center = newCenter)
        })
      )
    )
  }

  def createLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G]

  def canDragLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], oldInternalPoint: Point2D, newInternalPoint: Point2D): Boolean

  def deleteLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G]

  def dragLinkLabel(graph: G, link: L, oldCenter: Point2D, newCenter: Point2D): Option[G]

  def minCanvasDimension: Dimension2D =
    new Dimension2D(
      800,
      600
    )

  def graphMargin: Int = 20

  def getCanvasDimension(graphCanvas: GraphCanvas[V, L, G]): Dimension2D = {
    if (graphCanvas.vertexNodes.isEmpty)
      minCanvasDimension
    else {
      val maxRightEdge =
        graphCanvas
          .vertexNodes
          .values
          .map(vertexNode =>
            vertexNode.vertex.center.x + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodes
          .values
          .map(vertexNode =>
            vertexNode.vertex.center.y + vertexNode.height() / 2
          )
          .max


      new Dimension2D(
        math.max(
          maxRightEdge + graphMargin,
          minCanvasDimension.width
        ),

        math.max(
          maxBottomEdge + graphMargin,
          minCanvasDimension.height
        )
      )
    }
  }
}
