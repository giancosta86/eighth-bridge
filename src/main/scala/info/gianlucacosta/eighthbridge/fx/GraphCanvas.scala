package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualArc, VisualVertex}
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
                   initialGraph: AnyVisualGraph
                 ) extends Pane {
  //TODO! Perhaps, find another way to handle this null
  require(controller.graphCanvas == null, "The controller must not already belong to a graph!")
  controller.graphCanvas = this

  styleClass.add("graphCanvas")

  val graph =
    new SimpleObjectProperty[AnyVisualGraph](initialGraph)

  graph.addListener((observable: Observable) => {
    //The canvas can only be updated by changing the graph
    render()
  })

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


  val backgroundNode: BackgroundNode =
    controller.createBackgroundNode()

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

  render()

  private def render(): Unit = {
    /*
     * First of all, we need to update the map of vertex nodes,
     * creating new vertex nodes as required.
     */
    val (vertexNodesToKeep, vertexNodesToRemove) =
      _vertexNodesByVertex
        .values
        .partition(vertexNode =>
          graph().vertexes.contains(vertexNode.vertex)
        )

    val keptVertexes =
      vertexNodesToKeep
        .map(_.vertex)
        .toSet

    val vertexesWithoutNode =
      graph().vertexes -- keptVertexes

    val brandNewVertexNodes =
      vertexesWithoutNode.map(vertex => {
        val vertexNode = controller.createVertexNode(vertex)

        vertexNode.updateFx()

        vertexNode.toFront()

        vertexNode
      })

    val updatedVertexNodes = vertexNodesToKeep ++ brandNewVertexNodes

    _vertexNodesByVertex =
      updatedVertexNodes.map(vertexNode =>
          vertexNode.vertex -> vertexNode
        )
        .toMap

    /*
     * Then, we need to repeat the same process to update
     * the pool of arc nodes.
     */
    val originalArcNodes =
      _arcNodesByArc
        .values
        .toSet

    val arcNodesBasedOnExistingArcs =
      originalArcNodes
        .filter(arcNode =>
          graph().arcs.contains(arcNode.arc)
        )

    val keptArcs =
      arcNodesBasedOnExistingArcs.map(_.arc)

    val arcNodesAttachedToExistingVertexes =
      arcNodesBasedOnExistingArcs.filter(arcNode => {
        graph().getVertexPair(arcNode.arc).isDefined
      })

    val arcNodesToKeep = arcNodesAttachedToExistingVertexes

    val arcNodesToRemove = originalArcNodes -- arcNodesToKeep

    val arcsWithoutNode =
      graph().arcs -- keptArcs

    val brandNewArcNodes =
      arcsWithoutNode.map(arc => {
        val arcNode = controller.createArcNode(arc)

        arcNode.updateFx()

        arcNode
      })

    val updatedArcNodes = arcNodesToKeep ++ brandNewArcNodes

    _arcNodesByArc =
      updatedArcNodes.map(arcNode =>
          arcNode.arc -> arcNode
        ).
        toMap

    /*
     * Now, we need to add the new nodes to the canvas.
     */
    brandNewArcNodes.foreach((node: Node) => children.add(node))

    brandNewVertexNodes.foreach((node: Node) => children.add(node))

    /*
     * Right after appending the new nodes, we need to remove stale nodes.
     */
    arcNodesToRemove
      .foreach(children.remove)

    vertexNodesToRemove
      .foreach(children.remove)


    /*
     * Finally, we are going to ask the controller to computer the new dimension.
     */
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
