package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.facets.TimestampedGraphDiff
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ReadOnlyObjectProperty
import scalafx.geometry.Dimension2D
import scalafx.scene.Group
import scalafx.scene.input.{KeyCode, KeyEvent}
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
  */
class GraphCanvas extends Pane with PanContext with ZoomContext {
  styleClass.add("graph")

  val controller =
    new SimpleObjectProperty[GraphCanvasController](
      new GraphCanvasController {
        graphCanvas =
          GraphCanvas.this
      }
    )

  controller.onInvalidate {
    controller().graphCanvas =
      GraphCanvas.this

    fullRender()
  }

  private val backgroundNode =
    new SimpleObjectProperty[BackgroundNode](
      controller().createBackgroundNode()
    )

  backgroundNode <==
    Bindings.createObjectBinding[BackgroundNode](
      () =>
        controller().createBackgroundNode(),

      controller
    )

  private val backgroundSlot =
    new Group {
      children.setAll(backgroundNode())
    }

  children.add(backgroundSlot)

  backgroundNode.onInvalidate {
    backgroundSlot.children.setAll(backgroundNode())
    ()
  }

  val graph =
    new SimpleObjectProperty[VisualGraph](
      new DirectedGraph[VisualVertex, VisualLink]
    )

  graph.onChange { (_, oldValue, newValue) =>
    println(s"${new java.util.Date}: THE GRAPH PROPERTY HAS CHANGED")

    val graphDiff =
      newValue.getDiffSince(oldValue)

    println(s"The GraphDiff is: ${graphDiff}")

    if (graphDiff.nonEmpty) {
      render(newValue, graphDiff)
    }
  }

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

  focusTraversable =
    true

  private var _vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    Map()

  def vertexNodesByVertex: Map[VisualVertex, VertexNode] =
    _vertexNodesByVertex

  private var _linkNodesByLink: Map[VisualLink, LinkNode] =
    Map()

  def linkNodesByLink: Map[VisualLink, LinkNode] =
    _linkNodesByLink

  private val linkNodes =
    new Group
  children.add(linkNodes)

  private val vertexNodes =
    new Group
  children.add(vertexNodes)

  fullRender()

  private def fullRender(): Unit = {
    val currentGraph =
      graph()

    render(
      currentGraph,
      TimestampedGraphDiff.forSingleGraph(currentGraph)
    )
  }

  protected def render(updatedGraph: VisualGraph, graphDiff: TimestampedGraphDiff[VisualVertex, VisualLink]): Unit = {
    println(s"${new java.util.Date}: NOW RENDERING")

    val danglingLinkNodesByLink: Map[VisualLink, LinkNode] =
      _linkNodesByLink.filterKeys(graphDiff.removedLinks.contains)

    _linkNodesByLink --=
      danglingLinkNodesByLink.keySet


    val danglingVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      _vertexNodesByVertex.filterKeys(graphDiff.removedVertexes.contains)

    _vertexNodesByVertex --=
      danglingVertexNodesByVertex.keySet


    val addedVertexNodesByVertex: Map[VisualVertex, VertexNode] =
      graphDiff.addedVertexes.map(vertex => {
          val vertexNode =
            controller().createVertexNode(vertex)

          vertex -> vertexNode
        })
        .toMap

    _vertexNodesByVertex ++=
      addedVertexNodesByVertex


    val addedLinkNodesByLink: Map[VisualLink, LinkNode] =
      graphDiff.addedLinks.map(link => {
          val linkNode =
            controller().createLinkNode(link)

          link -> linkNode
        })
        .toMap

    _linkNodesByLink ++=
      addedLinkNodesByLink


    graphDiff.updatedVertexes.foreach(updatedVertex => {
      val vertexNode =
        _vertexNodesByVertex(updatedVertex)

      vertexNode.vertex() =
        updatedVertex.asInstanceOf[vertexNode.V]
    })

    graphDiff.updatedLinks.foreach(updatedLink => {
      val linkNode =
        _linkNodesByLink(updatedLink)

      linkNode.link() =
        updatedLink.asInstanceOf[linkNode.L]
    })


    addedVertexNodesByVertex
      .values
      .foreach(
        vertexNodes.children.add(_)
      )

    addedLinkNodesByLink
      .values
      .foreach(
        linkNodes.children.add(_)
      )


    danglingLinkNodesByLink
      .values
      .foreach(
        linkNodes.children.remove(_)
      )

    danglingVertexNodesByVertex
      .values
      .foreach(
        vertexNodes.children.remove(_)
      )


    val newDimension =
      controller().canvasDimension

    this.resize(
      newDimension.width,
      newDimension.height
    )
  }

  def adjust(graphAdjuster: () => VisualGraph): Unit = {
    println(s"---> REQUESTING AN ADJUSTMENT! First root center: ${graph().rootVertexes.head.center}")

    Platform.runLater {
      val adjustedGraph =
        graphAdjuster()

      println(s"---> ADJUSTMENT PERFORMED! First root center: ${adjustedGraph.rootVertexes.head.center}")

      graph() =
        adjustedGraph
    }
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
    controller().deleteSelection()
  }
}
