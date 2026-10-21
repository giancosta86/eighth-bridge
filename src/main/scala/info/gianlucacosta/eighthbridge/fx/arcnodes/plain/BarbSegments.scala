package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import info.gianlucacosta.eighthbridge.fx.{ArcNode, VertexNode}
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.Group
import scalafx.scene.shape.Line
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.collections.ObservableBuffer
import scalafx.Includes._

//TODO! All over the codebase, ensure that GUI stuff are as private as possible!
private class BarbSegments(sourceVertexNode: VertexNode, targetVertexNode: VertexNode, initialArcBarbs: ArcBarbs) extends Group {
  private val self =
    this

  private val intersectionPoint =
    new SimpleObjectProperty[Option[Point2D]]

  intersectionPoint <==
    Bindings.createObjectBinding(
      () =>
        ArcBarbs.getIntersection(
          new Point2D(
            sourceVertexNode.centerX(),
            sourceVertexNode.centerY()
          ),

          new Point2D(
            targetVertexNode.centerX(),
            targetVertexNode.centerY()
          ),

          new Dimension2D(
            targetVertexNode.width(),
            targetVertexNode.height()
          )
        ),

      sourceVertexNode.centerX,
      sourceVertexNode.centerY,
      targetVertexNode.centerX,
      targetVertexNode.centerY,
      targetVertexNode.width,
      targetVertexNode.height
    )

  private val arcBarbs =
    new SimpleObjectProperty[ArcBarbs](initialArcBarbs)

  private val sidePoints =
    new SimpleObjectProperty[Option[(Point2D, Point2D)]]

  sidePoints <== Bindings.createObjectBinding(
    () =>
      intersectionPoint().flatMap(actualIntersectionPoint =>
        ArcBarbs.getSidePoints(
          new Point2D(
            sourceVertexNode.centerX(),
            sourceVertexNode.centerY()
          ),

          actualIntersectionPoint,

          arcBarbs()
        )
      ),

    sourceVertexNode.centerX,
    sourceVertexNode.centerY,
    intersectionPoint,
    arcBarbs
  )

  private val canDrawBarbs =
    new SimpleBooleanProperty

  canDrawBarbs <== Bindings.createBooleanBinding(
    () =>
      sidePoints().isDefined,

    sidePoints
  )

  private val leftSegment =
    new BarbSegment {
      intersectionPoint <==
        self.intersectionPoint

      sidePoint <==
        Bindings.createObjectBinding(
          () =>
            sidePoints().map(_._1),

          sidePoints
        )
    }

  private val rightSegment =
    new BarbSegment {
      intersectionPoint <==
        self.intersectionPoint

      sidePoint <==
        Bindings.createObjectBinding(
          () =>
            sidePoints().map(_._2),

          sidePoints
        )
    }

  children.addAll(leftSegment, rightSegment)
}
