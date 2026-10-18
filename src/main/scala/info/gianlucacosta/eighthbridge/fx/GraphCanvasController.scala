package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.arcnodes.labeled.LabeledArcNode
import info.gianlucacosta.eighthbridge.fx.arcnodes.plain.PlainArcNode
import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled.LabeledVertexNode
import info.gianlucacosta.eighthbridge.fx.vertexnodes.plain.PlainVertexNode
import info.gianlucacosta.eighthbridge.theory.features.Named
import info.gianlucacosta.helios.fx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.Includes._

/**
  * Controller providing behavior for GraphCanvas
  */
abstract class GraphCanvasController {
  private var _graphCanvas: GraphCanvas = _

  protected[fx] final def graphCanvas: GraphCanvas =
    _graphCanvas

  protected[fx] final def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    require(_graphCanvas == null, "The controller must not be already associated with another GraphCanvas!")
    _graphCanvas = graphCanvas
  }

  protected final def graph: VisualGraph =
    graphCanvas.graph()

  //
  // NEW FUNCTIONS
  //
  def createBackgroundNode(): BackgroundNode =
    new DefaultBackgroundNode(graphCanvas)

  //TODO! Make this abstract!
  def createVertexNode(vertex: VisualVertex): VertexNode =
    new LabeledVertexNode(vertex)

  //TODO! Make this abstract!
  def createArcNode(arc: VisualArc): ArcNode =
    new PlainArcNode(arc)

  def minCanvasDimension: Dimension2D =
    new Dimension2D(
      800,
      600
    )

  def graphMargin: Int =
    20

  def canvasDimension: Dimension2D = {
    if (graphCanvas.vertexNodesByVertex.isEmpty)
      minCanvasDimension
    else {
      val maxRightEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.centerX() + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.centerY() + vertexNode.height() / 2
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

  def createVertex(center: Point2D): Option[VisualVertex]


  //
  // ANCIENT FUNCTIONS
  //

  //TODO! Turn this into a function of VisualArc, that the 2 specific trait will implement by returning their related constants!
  def renderDirected: Boolean



  def deleteSelection(): Option[VisualGraph] =
    None

 // def createVertex(center: Point2D): Option[VisualGraph] =
 //   None

  def createArc(sourceVertex: VisualVertex, targetVertex: VisualVertex): Option[VisualGraph] =
    None

  def editVertex(vertex: VisualVertex): Option[VisualGraph] =
    None

  def editArc(arc: VisualArc): Option[VisualGraph] =
    None

  def canDrawSelectionRectangle: Boolean =
    false

  def setVertexSelectedState(vertex: VisualVertex, selected: Boolean): Option[VisualGraph] =
    None

  def setArcSelectedState(arc: VisualArc, selected: Boolean): Option[VisualGraph] =
    None

  def setSelection(selectionVertexes: Set[VisualVertex], selectionArcs: Set[VisualArc]): Option[VisualGraph] =
    None

  //TODO! Delta should have a dedicated type!
  def dragSelection(delta: Point2D): Option[VisualGraph] =
    None

  def createArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[VisualGraph] =
    None

  def canDragArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    false

  def deleteArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[VisualGraph] =
    None

  def dragArcLabel(arc: VisualArc, originalCenter: Point2D, updatedCenter: Point2D): Option[VisualGraph] =
    None
}
