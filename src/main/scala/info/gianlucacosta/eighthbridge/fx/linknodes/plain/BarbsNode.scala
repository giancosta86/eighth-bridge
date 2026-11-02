package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import info.gianlucacosta.eighthbridge.fx.{VertexNode}
import javafx.beans.property.SimpleObjectProperty
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.Includes._

private class BarbsNode(
                          initialSourcePoint: Point2D,
                          initialIntersectionPoint: Option[Point2D],
                          initialArcBarbs: Option[ArcBarbs]
                       ) extends Group {
  val sourcePoint =
    new SimpleObjectProperty[Point2D](initialSourcePoint)

  val intersectionPoint =
    new SimpleObjectProperty[Option[Point2D]](initialIntersectionPoint)

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
