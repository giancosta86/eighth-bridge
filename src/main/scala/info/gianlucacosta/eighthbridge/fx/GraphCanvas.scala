package info.gianlucacosta.eighthbridge.fx

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
class GraphCanvas[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
](val controller: GraphCanvasController[V, L, G], initialGraph: G) extends Pane {
  //TODO! Perhaps, find another way to handle this null
  require(controller.graphCanvas == null, "The controller must not already belong to a graph!")
  controller.graphCanvas = this

  styleClass.add("graphCanvas")

  val graph =
    new SimpleObjectProperty[G](initialGraph)

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


  val backgroundNode: BackgroundNode[V, L, G] =
    controller.createBackgroundNode()

  children.add(backgroundNode)

  focusTraversable =
    true

  private var dragAnchor: Point2D = _

  private var panning: Boolean =
    false


  private var _vertexNodesByVertex: Map[V, VertexNode[V, L, G]] =
    Map()

  def vertexNodesByVertex: Map[V, VertexNode[V, L, G]] =
    _vertexNodesByVertex

  private var _linkNodesByLink: Map[L, LinkNode[V, L, G]] =
    Map()

  def linkNodesByLink: Map[L, LinkNode[V, L, G]] =
    _linkNodesByLink

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
          graph().vertexes.contains(vertexNode.vertexInGraph)
        )

    val keptVertexes =
      vertexNodesToKeep
        .map(_.vertexInGraph)
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
          vertexNode.vertexInGraph -> vertexNode
        )
        .toMap

    /*
     * Then, we need to repeat the same process to update
     * the pool of link nodes.
     */
    val originalLinkNodes =
      _linkNodesByLink
        .values
        .toSet

    val linkNodesBasedOnExistingLinks =
      originalLinkNodes
        .filter(linkNode =>
          graph().links.contains(linkNode.linkInGraph)
        )

    val keptLinks =
      linkNodesBasedOnExistingLinks.map(_.linkInGraph)

    val linkNodesAttachedToExistingVertexes =
      linkNodesBasedOnExistingLinks.filter(linkNode => {
        graph().getVertexPair(linkNode.linkInGraph).isDefined
      })

    val linkNodesToKeep = linkNodesAttachedToExistingVertexes

    val linkNodesToRemove = originalLinkNodes -- linkNodesToKeep

    val linksWithoutNode =
      graph().links -- keptLinks

    val brandNewLinkNodes =
      linksWithoutNode.map(link => {
        val linkNode = controller.createLinkNode(link)

        linkNode.updateFx()

        linkNode
      })

    val updatedLinkNodes = linkNodesToKeep ++ brandNewLinkNodes

    _linkNodesByLink =
      updatedLinkNodes.map(linkNode =>
          linkNode.linkInGraph -> linkNode
        ).
        toMap

    /*
     * Now, we need to add the new nodes to the canvas.
     */
    brandNewLinkNodes.foreach((node: Node) => children.add(node))

    brandNewVertexNodes.foreach((node: Node) => children.add(node))

    /*
     * Right after appending the new nodes, we need to remove stale nodes.
     */
    linkNodesToRemove
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
