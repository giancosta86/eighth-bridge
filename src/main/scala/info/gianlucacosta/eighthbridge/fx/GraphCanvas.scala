package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.eighthbridge.theory.features.TimestampedGraphDiff
import javafx.beans.Observable
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.Node
import scalafx.scene.input.{KeyCode, KeyEvent, MouseEvent, ScrollEvent}
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Rectangle

/**
  * JavaFX component rendering a graph.
  *
  * By plugging a controller, the canvas can flexibly:
  * <ul>
  * <li>Render graph components using different JavaFX nodes</li>
  * <li>Support a given set of operations - for example: graph definition via user interaction</li>
  * </ul>
  *
  * @param controller   The controller telling the canvas how to render components and how to react to user input
  * @param initialGraph The initial graph shown by the canvas
  */
class GraphCanvas(
                   val controller: GraphCanvasController,
                   initialGraph: VisualGraph
                 ) extends Pane {
  controller.graphCanvas = this

  styleClass.add("graphCanvas")

  val graph =
    new SimpleObjectProperty[VisualGraph](initialGraph)

  graph.onChange { (_, oldValue, newValue) =>
    val graphDiff =
      newValue.getDiffSince(oldValue)

    render(newValue, graphDiff)
  }

  val zoomEnabled =
    new SimpleBooleanProperty(true)

  val minZoomScale =
    new SimpleDoubleProperty(0.2)

  val maxZoomScale =
    new SimpleDoubleProperty(Double.PositiveInfinity)

  val panEnabled =
    new SimpleBooleanProperty(true)


  private var _dimension: Dimension2D =
    new Dimension2D(
      width(),
      height()
    )

  //TODO! Is this really needed?
  def dimension: Dimension2D =
    _dimension

  width.addListener((observable: javafx.beans.Observable) => {
    _dimension =
      new Dimension2D(
        width(),
        height()
      )
  })

  height.addListener((observable: javafx.beans.Observable) => {
    _dimension =
      new Dimension2D(
        width(),
        height()
      )
  })


  clip = new Rectangle {
    width <==
      GraphCanvas.this.width

    height <==
      GraphCanvas.this.height
  }


  val backgroundNode: BackgroundNode = {
    val result =
      controller.createBackgroundNode()

    result.graphCanvas =
      this

    result
  }


  children.add(backgroundNode)

  focusTraversable =
    true

  private var dragAnchor: Point2D = _

  private var panning: Boolean =
    false


  private var _vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    Map()

  def vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    _vertexNodesByVertex

  private var _arcNodesByArc: Map[VisualArc, ArcNode] =
    Map()

  def arcNodesByArc: Map[VisualArc, ArcNode] =
    _arcNodesByArc

  render(
    initialGraph,
    new TimestampedGraphDiff[VisualVertex, VisualArc]()
  )

  private def render(newGraph: VisualGraph, graphDiff: TimestampedGraphDiff[VisualVertex, VisualArc]): Unit = {
    val addedVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      graphDiff.addedVertexes.map(_ -> {
          val vertexNode =
            controller.createVertexNode()

          vertexNode.graphCanvas =
            this

          vertexNode
        })
        .toMap

    val addedArcNodesByArc: Map[VisualArc, ArcNode] =
      graphDiff.addedArcs.map(_ -> {
          val arcNode =
            controller.createArcNode()

          arcNode.graphCanvas =
            this

          arcNode
        })
        .toMap

    val staleVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      vertexNodesByVertex.filterKeys(graphDiff.removedVertexes.contains)

    val staleArcNodesByArc: Map[VisualArc, ArcNode] =
      arcNodesByArc.filterKeys(graphDiff.removedArcs.contains)

    _vertexNodesByVertex =
      vertexNodesByVertex ++ addedVertexNodesByVertex -- staleVertexNodesByVertex.keySet

    _arcNodesByArc =
      arcNodesByArc ++ addedArcNodesByArc -- staleArcNodesByArc.keySet

    vertexNodesByVertex
      .foreach{ case (vertex, vertexNode) =>
        vertexNode.updateFxProps(vertex)
      }

    arcNodesByArc
      .foreach { case (arc, arcNode) =>
        arcNode.updateFxProps(arc)
      }



    addedVertexNodesByVertex.values.foreach(children.add(_))

    addedArcNodesByArc.values.foreach(children.add(_))

    staleVertexNodesByVertex.values.foreach(children.remove(_))

    staleArcNodesByArc.values.foreach(children.remove(_))

    val newDimension =
      controller.canvasDimension

    this.resize(
      newDimension.width,
      newDimension.height
    )
  }


  handleEvent(KeyEvent.KeyPressed) {
    (keyEvent: KeyEvent) => {
      keyEvent.code match {
        case KeyCode.Delete =>
          controller.deleteSelection()
            .foreach(newGraph =>
              graph() = newGraph
            )

        case _ =>
      }
    }
  }


  handleEvent(ScrollEvent.Scroll) {
    (event: ScrollEvent) => {
      if (zoomEnabled()) {
        event.consume()

        val oldScale =
          scaleX()

        val scaleFactor =
          math.pow(1.01, event.deltaY / 5)

        val newScale = math.max(
          minZoomScale(),

          math.min(
            maxZoomScale(),

            oldScale * scaleFactor
          )
        )

        scaleX() =
          newScale

        scaleY() =
          newScale

        val zoomFactor =
          (newScale / oldScale) - 1

        val canvasBounds =
          this.getBoundsInParent


        val deltaX =
          event.sceneX - (canvasBounds.width / 2 + canvasBounds.minX)

        val deltaY =
          event.sceneY - (canvasBounds.height / 2 + canvasBounds.minY)

        translateX() -=
          zoomFactor * deltaX

        translateY() -=
          zoomFactor * deltaY

        ()
      }
    }
  }


  filterEvent(MouseEvent.MousePressed) {
    (event: MouseEvent) => {
      if (event.isShiftDown && panEnabled()) {
        event.consume()

        dragAnchor =
          new Point2D(
            event.sceneX,
            event.sceneY
          )

        panning =
          true
      }
    }
  }


  filterEvent(MouseEvent.MouseDragged) {
    (event: MouseEvent) => {
      if (panning) {
        event.consume()

        val delta =
          new Point2D(
            event.sceneX - dragAnchor.x,
            event.sceneY - dragAnchor.y
          )

        translateX() +=
          delta.x

        translateY() +=
          delta.y

        dragAnchor =
          new Point2D(event.sceneX, event.sceneY)
      }
    }
  }


  filterEvent(MouseEvent.MouseReleased) {
    (event: MouseEvent) => {
      if (panning) {
        event.consume()

        dragAnchor =
          null

        panning =
          false
      }
    }
  }
}
