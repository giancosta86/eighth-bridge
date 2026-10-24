package info.gianlucacosta.eighthbridge.fx.arcnodes.labeled

import info.gianlucacosta.eighthbridge.fx.arcnodes.plain.PlainArcNode
import info.gianlucacosta.eighthbridge.fx.graph.{LabeledVisualArc, VisualArc}
import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.theory.features.Labeled
import info.gianlucacosta.helios.fx.geometry.Segment
import scalafx.beans.binding.Bindings
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line

class LabeledArcNode(graphCanvas: GraphCanvas, initialArc: LabeledVisualArc)
extends PlainArcNode(graphCanvas, initialArc) {
  private val firstHandlePoint =
    Bindings.createObjectBinding[Option[Point2D]](
      () =>
        arc().handles.headOption.map(_.center),

      arc
    )

  private val labelAnchorPoint =
    Bindings.createObjectBinding[Point2D](
      () => {
        val sourcePoint =
          sourceVertexNode.center()

        val targetPoint =
          firstHandlePoint().getOrElse {
            targetVertexNode.center()
          }

        Segment(sourcePoint, targetPoint)
          .center
      },

      sourceVertexNode.center,
      firstHandlePoint,
      targetVertexNode.center
    )

  private val label =
    new ArcLabel(
      onDrag = handleDragOnLabel,
      onDoubleClick = handleDoubleClickOnLabel
    ) {
      text <==
        Bindings.createStringBinding(
          () =>
            //TODO! Introduce a custom type and apply it to the arc property, so as to skip these casts
            arc().asInstanceOf[Labeled].label,

          arc
        )

      center <==
        Bindings.createObjectBinding[Point2D](
          () =>
            arc()
              .asInstanceOf[LabeledVisualArc] //TODO! Can I get rid of this cast?
              .customLabelCenter
              .getOrElse(labelAnchorPoint()),

          arc,
          labelAnchorPoint
        )
    }


  private val labelConnector = {
    new LabelConnector {
      startX <==
        Bindings.createDoubleBinding(
          () =>
            label.center().x,

          label.center
        )

      startY <==
        Bindings.createDoubleBinding(
          () =>
            label.center().y,

          label.center
        )

      endX <==
        Bindings.createDoubleBinding(
          () =>
            labelAnchorPoint().x,

          labelAnchorPoint
        )

      endY <==
        Bindings.createDoubleBinding(
          () =>
            labelAnchorPoint().y,

          labelAnchorPoint
        )

      visible <==
        Bindings.createBooleanBinding(
          () =>
            label.hover() && arc().asInstanceOf[LabeledVisualArc].customLabelCenter.isDefined,

          label.hover,
          arc
        )
    }
  }

  children.add(labelConnector)
  children.add(label)

  private def handleDragOnLabel(updatedCenter: Point2D): Unit = {
    //TODO! Pass through the controller!
    println(s"&&&&&&&&&&&&&&&&&&&&&&& THE UPDATED LABEL CENTER IS ${updatedCenter}")

    val updatedArc =
      arc()
        .asInstanceOf[LabeledVisualArc]
        .setCustomLabelCenter(Some(updatedCenter))

    graph =
      graph.updateArc(updatedArc)
  }

  private def handleDoubleClickOnLabel(): Unit = {
    //TODO! Pass through the controller!

    val updatedArc =
      arc()
        .asInstanceOf[LabeledVisualArc]
        .setCustomLabelCenter(None)

    graph =
      graph.updateArc(updatedArc)
  }
}
