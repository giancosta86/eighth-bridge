package info.gianlucacosta.eighthbridge.graphs.directed

import scalafx.geometry.Point2D

import java.lang.Math._

case class ArcBarbs(
                     angleInDegrees: Double,
                     length: Double
                   ) {
  require(0 < angleInDegrees && angleInDegrees < 90)

  require(length > 0)

  def getSidePoints(arrowSource: Point2D, arrowTarget: Point2D): Option[(Point2D, Point2D)] = {
    if (arrowSource == arrowTarget) {
      return None
    }

    val barbAngle =
      toRadians(angleInDegrees)

    val deltaX =
      arrowSource.x - arrowTarget.x

    val deltaY =
      arrowSource.y - arrowTarget.y

    val squaredDeltaY =
      deltaY * deltaY

    val a =
      deltaX * deltaX + squaredDeltaY

    val d =
      arrowSource.x * deltaX + arrowSource.y * deltaY - a + length * sqrt(a) * cos(barbAngle)

    val halfB =
      arrowTarget.y * deltaX * deltaY - arrowTarget.x * squaredDeltaY - d * deltaX

    val c =
      squaredDeltaY * (arrowTarget.x * arrowTarget.x + arrowTarget.y * arrowTarget.y - length * length) + d * (d - 2 * arrowTarget.y * deltaY)

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

object ArcBarbs {
  val Default = ArcBarbs(
    angleInDegrees = 30,
    length = 15
  )
}
