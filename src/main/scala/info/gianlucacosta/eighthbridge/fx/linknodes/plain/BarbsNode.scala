package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D
import scalafx.scene.Group

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

  sidePoints.onChange {
    updateSegments()
  }

  private val leftSegment =
    new BarbSegment

  private val rightSegment =
    new BarbSegment

  updateSegments()

  private def updateSegments(): Unit = {
    sidePoints() match {
      case Some((leftPoint, rightPoint)) =>
        val actualIntersectionPoint =
          intersectionPoint().get

        leftSegment.sidePoint() =
          leftPoint

        leftSegment.intersectionPoint() =
          actualIntersectionPoint

        rightSegment.sidePoint() =
          rightPoint

        rightSegment.intersectionPoint() =
          actualIntersectionPoint

        if (children.isEmpty) {
          children.addAll(leftSegment, rightSegment)
        }

      case None =>
        children.clear()
    }
  }
}
