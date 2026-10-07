package info.gianlucacosta.eighthbridge.fx

import scalafx.Includes._
import info.gianlucacosta.helios.fx.Includes._
import javafx.beans.property.SimpleDoubleProperty
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.control.Label
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.VBox
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.TextAlignment
import scalafx.scene.{Group, Scene}

import scala.collection.JavaConversions._


object DefaultVertexNode {
  val DefaultPadding: Double =
    10

  /**
    * Case class whose instances can be passed to <i>getDimensions()</i>
    *
    * @param text         The vertex text
    * @param padding      The vertex padding
    * @param styleClasses The CSS classes that should be applied to the vertex (in addition to the "vertex" predefined one)
    */
  case class DimensionQuery(
                             text: String,
                             padding: Double = DefaultPadding,
                             styleClasses: List[String] = List()
                           )


  /**
    * Given a list of stylesheets and a list of query objects,
    * returns the list of Dimension2D for BasicVertexNode objects,
    * instantiated in a dedicated invisible scene
    *
    * @param sceneStylesheets
    * @param queries
    * @return The list of dimensions, in the same order as the input queries
    */
  def getDimensions(sceneStylesheets: List[String], queries: List[DimensionQuery]): List[Dimension2D] = {
    val layoutBox =
      new VBox

    val scene =
      new Scene(layoutBox) {
        stylesheets.setAll(sceneStylesheets: _*)
      }


    val labels =
      queries.map(query =>
        new Label {
          text = query.text
        }
      )


    val groupNodes =
      labels.zip(queries).map {
        case (label, query) =>
          new Group {
            children.add(label)

            styleClass.setAll("vertex")
            styleClass.addAll(query.styleClasses)
          }
            .delegate
      }

    layoutBox.children.setAll(groupNodes: _*)

    layoutBox.applyCss()

    labels.zip(queries).map {
      case (label, query) =>
        val padding =
          query.padding

        new Dimension2D(
          label.delegate.prefWidth(-1)
            + 2 * padding,

          label.delegate.prefHeight(-1)
            + 2 * padding
        )
    }
  }
}


/**
  * Default, interactive implementation of VertexNode
  */
class DefaultVertexNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
](
   val graphCanvas: GraphCanvas[V, L, G],
   padding: Double = DefaultVertexNode.DefaultPadding
 )
  extends Group
    with VertexNode[V, L, G] {

  private var dragAnchor: Point2D = _

  protected val label = new Label {
    styleClass.add("label")

    textAlignment =
      TextAlignment.Center


    layoutX <==
      centerX - width / 2


    layoutY <==
      centerY - height / 2
  }

  protected val body = new Rectangle {
    styleClass.add("body")

    width <==
      label.width + 2 * padding


    height <==
      label.height + 2 * padding


    layoutX <==
      label.layoutX - padding


    layoutY <==
      label.layoutY - padding
  }


  children.addAll(body, label)


  override def render(): Unit = {
    super.render()

    label.text =
      vertex.text

    centerX() =
      vertex.center.x

    centerY() =
      vertex.center.y
  }


  override def width: ReadOnlyDoubleProperty =
    body.width


  override def height: ReadOnlyDoubleProperty =
    body.height

  handleEvent(MouseEvent.Any) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.consume()
    }
  }


  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              dragAnchor =
                mouseEvent.point

              if (mouseEvent.controlDown) {
                controller.setVertexSelectedState(vertex, !vertex.selected)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              } else if (!vertex.selected) {
                controller.setSelection(Set(vertex), Set())
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }

            case 2 =>
              val selectedVertexes =
                graph.selectedVertexes

              if (selectedVertexes.size == 1 && graph.selectedLinks.isEmpty) {
                val selectedVertex =
                  selectedVertexes.head

                controller.editVertex(selectedVertex)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }

            case _ =>
          }

        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              if (graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(vertex) && graph.selectedLinks.isEmpty) {
                val selectedVertex =
                  graph.selectedVertexes.head

                controller.createLink(selectedVertex, vertex)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }
          }

        case _ =>
      }
      ()
    }
  }


  handleEvent(MouseEvent.MouseDragged) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          val mousePoint =
            mouseEvent.point

          val delta =
            mousePoint - dragAnchor


          if (vertex.selected) {
            controller.dragSelection(delta)
              .foreach(newGraph => {
                dragAnchor =
                  mousePoint

                graph =
                  newGraph
              })
          }
        case _ =>
      }
    }
  }
}
