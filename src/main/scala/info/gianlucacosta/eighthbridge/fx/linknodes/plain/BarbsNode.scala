package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import info.gianlucacosta.eighthbridge.fx.{Centered, Segments, VertexNode}
import javafx.beans.property.SimpleObjectProperty
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.Includes._

private class BarbsNode(
                          initialSourcePointProvider: Centered,
                         targetVertexNode: VertexNode,
                          initialArcBarbs: Option[ArcBarbs]
                       ) extends Group {
  val sourcePointProvider =
    new SimpleObjectProperty[Centered](initialSourcePointProvider)

  private val sourcePoint =
    Bindings.createObjectBinding[Point2D](
      () => new Point2D(
        sourcePointProvider().centerX(),
        sourcePointProvider().centerY()
      ),

      sourcePointProvider,
      sourcePointProvider().centerX,
      sourcePointProvider().centerY
    )

  private val intersectionPoint =
    Bindings.createObjectBinding(
      () =>
        Segments.getIntersectionWithRectangle(
          sourcePoint(),

          targetVertexNode.center(),

          new Dimension2D(
            targetVertexNode.width(),
            targetVertexNode.height()
          )
        ),

      sourcePoint,
      targetVertexNode.center,
      targetVertexNode.width,
      targetVertexNode.height
    )

  val arcBarbs =
    new SimpleObjectProperty[Option[ArcBarbs]](initialArcBarbs)

  private val sidePoints =
    Bindings.createObjectBinding[Option[(Point2D, Point2D)]](
      () =>
        arcBarbs().flatMap(arcBarbs =>
          intersectionPoint().flatMap(intersectionPoint =>
            ArcBarbs.getSidePoints(
              sourcePoint(),

              intersectionPoint,

              arcBarbs
            )
          )
        ),

      arcBarbs,
      intersectionPoint,
      sourcePoint
    )

  private val sideSegments =
    Bindings.createObjectBinding[Option[(BarbSegment, BarbSegment)]](
      () =>
        intersectionPoint().flatMap(intersectionPoint =>
          sidePoints().map(sidePoints =>
            (
              new BarbSegment(
                intersectionPoint =
                  intersectionPoint,

                sidePoint =
                  sidePoints._1
              ),

              new BarbSegment(
                intersectionPoint =
                  intersectionPoint,

                sidePoint =
                  sidePoints._2
              )
            )
          )
        ),

      intersectionPoint,
      sidePoints
    )

  sideSegments.onChange {
    children.clear()

    sideSegments().foreach { case (leftSegment, rightSegment) =>
      children.addAll(leftSegment, rightSegment)
    }
  }
}
