package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.theory.features.{Labeled, Named}
import javafx.beans.property.SimpleStringProperty
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyStringProperty}
import scalafx.geometry.Point2D

class LabeledVertexNode(vertex: VisualVertex) extends VertexNode(vertex) {
  private val _label =
    new SimpleStringProperty()

  val label: ReadOnlyStringProperty =
    _label

  private var dragAnchor: Point2D = _

  private val labelControl =
    new VertexLabel(this)

  children.add(labelControl)


  override val width: ReadOnlyDoubleProperty =
    labelControl.width

  override val height: ReadOnlyDoubleProperty =
    labelControl.height

  override protected[fx] def updateFxProps(updatedVertex: VisualVertex): Unit = {
    super.updateFxProps(updatedVertex)

    _label() =
      updatedVertex.asInstanceOf[Labeled].label
  }

  /*
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
                controller.setVertexSelectedState(vertex, !vertex().selected)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              } else if (!vertex().selected) {
                controller.setSelection(Set(vertex), Set())
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }

            case 2 =>
              val selectedVertexes =
                graph.selectedVertexes

              if (selectedVertexes.size == 1 && graph.selectedArcs.isEmpty) {
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
              if (graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(vertex) && graph.selectedArcs.isEmpty) {
                val selectedVertex =
                  graph.selectedVertexes.head

                controller.createArc(selectedVertex, vertex)
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


          if (vertex().selected) {
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
  }*/
}
