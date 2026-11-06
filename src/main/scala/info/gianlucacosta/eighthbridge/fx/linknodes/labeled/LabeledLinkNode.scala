package info.gianlucacosta.eighthbridge.fx.linknodes.labeled

import info.gianlucacosta.eighthbridge.fx.GraphCanvas
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, LabeledVisualLink}
import info.gianlucacosta.eighthbridge.fx.linknodes.plain.PlainLinkNode
import info.gianlucacosta.helios.fx.geometry.Segment
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

class LabeledLinkNode(graphCanvas: GraphCanvas, initialArc: LabeledVisualLink)
extends PlainLinkNode(graphCanvas, initialArc) {
  override type L <:
    LabeledVisualLink

  private val firstHandlePoint =
    Bindings.createObjectBinding[Option[Point2D]](
      () =>
        link().handles.headOption.map(_.center),

      link
    )

  private val labelAnchorPoint =
    Bindings.createObjectBinding[Point2D](
      () => {
        val sourcePoint =
          sourceVertexNode.vertex().center

        val targetPoint =
          firstHandlePoint().getOrElse {
            targetVertexNode.vertex().center
          }

        Segment(sourcePoint, targetPoint)
          .center
      },

      sourceVertexNode.vertex,
      firstHandlePoint,
      targetVertexNode.vertex
    )

  private val label =
    new LinkLabel(
      initialCenter =
        initialArc.customLabelCenter.getOrElse(labelAnchorPoint()),
      onDrag =
        handleDragOnLabel,
      onSecondaryDoubleClick =
        handleSecondaryDoubleClickOnLabel
    ) {
      text <==
        Bindings.createStringBinding(
          () =>
            link().label,

          link
        )

      center <==
        Bindings.createObjectBinding[Point2D](
          () =>
            link()
              .customLabelCenter
              .getOrElse(labelAnchorPoint()),

          link,
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
            LabeledLinkNode.this.hover() && link().customLabelCenter.isDefined,

          LabeledLinkNode.this.hover,
          link
        )
    }
  }

  children.add(labelConnector)
  children.add(label)

  private def handleDragOnLabel(delta: Delta): Unit = {
    val actualCenter: Point2D =
      link()
        .customLabelCenter
        .getOrElse(labelAnchorPoint())

    controller.dragLinkLabel(link(), actualCenter, delta)
  }

  private def handleSecondaryDoubleClickOnLabel(): Unit = {
    controller.resetLinkLabelCenter(link())
  }
}
