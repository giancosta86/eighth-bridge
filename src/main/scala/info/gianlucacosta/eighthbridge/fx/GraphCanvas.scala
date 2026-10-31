package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.features.TimestampedGraphDiff
import javafx.beans.Observable
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyObjectProperty}
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
                 ) extends Pane with DragContext {
  controller.graphCanvas =
    this

  styleClass.add("graph")

  val graph =
    new SimpleObjectProperty[VisualGraph](initialGraph)

  graph.onChange { (_, oldValue, newValue) =>
    val graphDiff =
      newValue.getDiffSince(oldValue)

    if (graphDiff.nonEmpty) {
      render(newValue, graphDiff)
    }
  }

  val zoomEnabled =
    new SimpleBooleanProperty(true)

  val minZoomScale =
    new SimpleDoubleProperty(0.2)

  val maxZoomScale =
    new SimpleDoubleProperty(Double.PositiveInfinity)

  val panEnabled =
    new SimpleBooleanProperty(true)

  def dimension: ReadOnlyObjectProperty[Dimension2D] =
    ReadOnlyProperties.createObject(
      () =>
        new Dimension2D(
          width(),
          height()
        ),

      width,
      height
    )

  clip = new Rectangle {
    width <==
      GraphCanvas.this.width

    height <==
      GraphCanvas.this.height
  }

  private val backgroundNode: BackgroundNode =
    controller.createBackgroundNode()

  children.add(backgroundNode)

  focusTraversable =
    true

  private val _panning =
    new SimpleBooleanProperty(false)

  def panning: ReadOnlyBooleanProperty =
    _panning

  private var _vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    Map()

  def vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    _vertexNodesByVertex

  private var _linkNodesByLink: Map[VisualLink, LinkNode] =
    Map()

  def linkNodesByLink: Map[VisualLink, LinkNode] =
    _linkNodesByLink

  render(
    initialGraph,
    new TimestampedGraphDiff[VisualVertex, VisualLink]()
  )

  private def render(updatedGraph: VisualGraph, graphDiff: TimestampedGraphDiff[VisualVertex, VisualLink]): Unit = {
    println(s"${new java.util.Date}: NOW RENDERING")

    val danglingVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      _vertexNodesByVertex.filterKeys(graphDiff.removedVertexes.contains)

    val danglingLinkNodesByLink: Map[VisualLink, LinkNode] =
      _linkNodesByLink.filterKeys(graphDiff.removedLinks.contains)

    _vertexNodesByVertex --=
      danglingVertexNodesByVertex.keySet

    _linkNodesByLink --=
      danglingLinkNodesByLink.keySet

    graphDiff.updatedVertexes.foreach(vertex => {
      val updatedVertex =
        updatedGraph.getVertexById(vertex.id).get

      val vertexNode =
        _vertexNodesByVertex(vertex)

      vertexNode.vertex() =
        updatedVertex.asInstanceOf[vertexNode.V]
    })

    graphDiff.updatedLinks.foreach(link => {
      val updatedLink =
        updatedGraph.getLinkById(link.id).get

      val linkNode =
        _linkNodesByLink(link)

      linkNode.link() =
        updatedLink.asInstanceOf[linkNode.L]
    })

    val addedVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      graphDiff.addedVertexes.map(vertex => {
          val vertexNode =
            controller.createVertexNode(vertex)

          vertex -> vertexNode
        })
        .toMap

    val addedLinkNodesByLink: Map[VisualLink, LinkNode] =
      graphDiff.addedLinks.map(link => {
          val linkNode =
            controller.createLinkNode(link)

          link -> linkNode
        })
        .toMap

    _vertexNodesByVertex ++=
      addedVertexNodesByVertex

    _linkNodesByLink ++=
      addedLinkNodesByLink

    addedVertexNodesByVertex.values.foreach(children.add(_))

    addedLinkNodesByLink.values.foreach(addedArcNode =>
      /*
       * The first child node is always the background node.
       * Then, new links can be added so that newer ones will stay on top.
       */
      children.add(updatedGraph.links.size, addedArcNode)
    )

    danglingVertexNodesByVertex.values.foreach(children.remove(_))

    danglingLinkNodesByLink.values.foreach(children.remove(_))

    val newDimension =
      controller.canvasDimension

    this.resize(
      newDimension.width,
      newDimension.height
    )
  }

  filterEvent(KeyEvent.KeyPressed) {
    (keyEvent: KeyEvent) => {
      keyEvent.code match {
        case KeyCode.Delete =>
          handleDelKeyPressed()

        case _ =>
      }
    }
  }

  private def handleDelKeyPressed(): Unit = {
    controller.deleteSelection()
  }

  filterEvent(ScrollEvent.Scroll) {
    (event: ScrollEvent) => {
      handleMouseScroll(event)
    }
  }

  private def handleMouseScroll(event: ScrollEvent): Unit = {
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
    }
  }

  override protected def handleDragStart(event: MouseEvent): Unit = {
    super.handleDragStart(event)

    if (event.isShiftDown && panEnabled()) {
      _panning() =
        true

      event.consume()
    }
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    if (_panning()) {
      event.consume()

      translateX() +=
        deltaFromLatest.x

      translateY() +=
        deltaFromLatest.y
    }
  }

  override protected def handleDragEnd(event: MouseEvent): Unit = {
    if (_panning()) {
      event.consume()

      _panning() =
        false
    }

    super.handleDragEnd(event)
  }
}
