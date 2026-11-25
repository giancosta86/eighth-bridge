package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.core.directed.ArcBarbs
import info.gianlucacosta.eighthbridge.fx.linknodes.plain.BarbsNode.ArcBarbsExtensions
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.Group

import java.lang.Math.{cos, sqrt, toRadians}

private class BarbsNode(
                         initialSourcePoint: Point2D,
                         initialIntersectionPoint: Option[Point2D],
                         initialArcBarbs: Option[ArcBarbs]
                       ) extends Group {
  val sourcePoint: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D](initialSourcePoint)

  val intersectionPoint: ObjectProperty[Option[Point2D]] =
    new SimpleObjectProperty[Option[Point2D]](initialIntersectionPoint)

  val arcBarbs: ObjectProperty[Option[ArcBarbs]] =
    new SimpleObjectProperty[Option[ArcBarbs]](initialArcBarbs)

  private val sidePoints =
    Bindings.createObjectBinding[Option[(Point2D, Point2D)]](
      () =>
        arcBarbs().flatMap(arcBarbs =>
          intersectionPoint().flatMap(intersectionPoint =>
            arcBarbs.getSidePoints(
              arrowSource =
                sourcePoint(),
              arrowTarget =
                intersectionPoint
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

private object BarbsNode {
  implicit class ArcBarbsExtensions(self: ArcBarbs) {
    def getSidePoints(arrowSource: Point2D, arrowTarget: Point2D): Option[(Point2D, Point2D)] = {
      if (arrowSource == arrowTarget) {
        return None
      }

      val barbAngle =
        toRadians(self.angleInDegrees)

      val deltaX =
        arrowSource.x - arrowTarget.x

      val deltaY =
        arrowSource.y - arrowTarget.y

      val squaredDeltaY =
        deltaY * deltaY

      val a =
        deltaX * deltaX + squaredDeltaY

      val d =
        arrowSource.x * deltaX + arrowSource.y * deltaY - a + self.length * sqrt(a) * cos(barbAngle)

      val halfB =
        arrowTarget.y * deltaX * deltaY - arrowTarget.x * squaredDeltaY - d * deltaX

      val c =
        squaredDeltaY * (arrowTarget.x * arrowTarget.x + arrowTarget.y * arrowTarget.y - self.length * self.length) + d * (d - 2 * arrowTarget.y * deltaY)

      val deltaQuarterRoot =
        sqrt(halfB * halfB - a * c)

      val leftX =
        (-halfB - deltaQuarterRoot) / a

      val leftY =
        (d - deltaX * leftX) / deltaY

      val rightX =
        (-halfB + deltaQuarterRoot) / a

      val rightY =
        (d - deltaX * rightX) / deltaY

      Some(
        (
          new Point2D(leftX, leftY),
          new Point2D(rightX, rightY)
        )
      )
    }
  }
}