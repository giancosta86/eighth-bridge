package info.gianlucacosta.eighthbridge.fx.arcnodes.labeled

import info.gianlucacosta.eighthbridge.fx.arcnodes.plain.PlainLinkNode
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, LabeledVisualLink, VisualLink}
import info.gianlucacosta.eighthbridge.fx.{LinkNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.graphs.features.Labeled
import info.gianlucacosta.helios.fx.geometry.Segment
import scalafx.beans.binding.Bindings
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line
import info.gianlucacosta.helios.fx.Includes._
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import javafx.beans.property.SimpleObjectProperty

import java.util.Date

class LabeledLinkNode(graphCanvas: GraphCanvas, initialArc: LabeledVisualLink)
extends PlainLinkNode(graphCanvas, initialArc) {
  private val self =
    this

  override type A <:
    LabeledVisualLink

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
    new LinkLabel(
      initialCenter = initialArc.customLabelCenter.getOrElse(labelAnchorPoint()),
      onDrag = handleDragOnLabel,
      onSecondaryDoubleClick = handleSecondaryDoubleClickOnLabel
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
            self.hover() && arc().customLabelCenter.isDefined,

          self.hover,
          arc
        )
    }
  }

  children.add(labelConnector)
  children.add(label)

  private def handleDragOnLabel(delta: Delta): Unit = {
    val link =
      arc()

    val actualCenter: Point2D =
      link
        .customLabelCenter
        .getOrElse(labelAnchorPoint())

    controller.dragLinkLabel(link, actualCenter, delta)
  }

  private def handleSecondaryDoubleClickOnLabel(): Unit = {
    controller.resetLinkLabelCenter(arc())
  }
}
