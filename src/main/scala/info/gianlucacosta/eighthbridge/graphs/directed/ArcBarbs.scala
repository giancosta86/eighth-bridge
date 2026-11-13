package info.gianlucacosta.eighthbridge.graphs.directed

import scalafx.geometry.Point2D

import java.lang.Math._

case class ArcBarbs(
                     angleInDegrees: Double,
                     length: Double
                   ) {
  require(0 < angleInDegrees && angleInDegrees < 90)

  require(length > 0)

  def getSidePoints(source: Point2D, target: Point2D): Option[(Point2D, Point2D)] = {
    if (source == target) {
      return None
    }

    val barbAngle =
      toRadians(angleInDegrees)

    val deltaX =
      source.x - target.x

    val deltaY =
      source.y - target.y

    val squaredDeltaY =
      deltaY * deltaY

    val a =
      deltaX * deltaX + squaredDeltaY

    val d =
      source.x * deltaX + source.y * deltaY - a + length * sqrt(a) * cos(barbAngle)

    val halfB =
      target.y * deltaX * deltaY - target.x * squaredDeltaY - d * deltaX

    val c =
      squaredDeltaY * (target.x * target.x + target.y * target.y - length * length) + d * (d - 2 * target.y * deltaY)

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
