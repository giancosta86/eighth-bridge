package info.gianlucacosta.eighthbridge.fx.arcnodes.labeled

import info.gianlucacosta.eighthbridge.fx.arcnodes.plain.PlainArcNode
import info.gianlucacosta.eighthbridge.fx.graph.{LabeledVisualArc, VisualArc}
import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.theory.features.Labeled
import info.gianlucacosta.helios.fx.geometry.Segment
import scalafx.beans.binding.Bindings
import scalafx.Includes._
import scalafx.geometry.Point2D

class LabeledArcNode(graphCanvas: GraphCanvas, initialArc: LabeledVisualArc)
extends PlainArcNode(graphCanvas, initialArc) {
  private val firstHandlePoint =
    Bindings.createObjectBinding[Option[Point2D]](
      () =>
        arc().handles.headOption.map(_.center),

      arc
    )

  private val barycenter =
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
    new ArcLabel {
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
              .getOrElse(barycenter()),

          arc,
          barycenter
        )
    }

  children.add(label)
}
