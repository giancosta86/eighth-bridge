package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.{Dimension2D, Point2D}
import Math._

object ArcBarbs {
  val Default = ArcBarbs(
    angleInDegrees = 30,
    length = 15
  )

  /**
   * Returns the intersection point between the segment connecting two points (`sourcePoint` and `targetCenter)`)
   * and the rectangle whose center is targetCenter.
   *
   * @param sourcePoint The origin of the segment lying externally or on the sides of the rectangle.
   * @param targetCenter The center of the rectangle.
   * @param targetSize The size of the rectangle.
   * @return The intersection point - which could be None if the source point is internal to the rectangle.
   */
  def getIntersection(sourcePoint: Point2D, targetCenter: Point2D, targetSize: Dimension2D): Option[Point2D] = {
    val deltaX =
      sourcePoint.x - targetCenter.x

    val deltaY =
      sourcePoint.y - targetCenter.y

    val halfWidth  =
      targetSize.width / 2

    val halfHeight =
      targetSize.height / 2

    if (abs(deltaX) < halfWidth && abs(deltaY) < halfHeight) {
      return None
    }

    val sY =
      signum(deltaY)

    val angularCoefficient =
      deltaY / deltaX

    if (abs(angularCoefficient) == Double.PositiveInfinity) {
      Some(
        new Point2D(
          targetCenter.x,
          targetCenter.y + sY * halfHeight
      ))
    } else if (abs(angularCoefficient) <= halfHeight / halfWidth) {
        val sX =
          signum(deltaX)

        Some(
          new Point2D(
            targetCenter.x + sX * halfWidth,
            targetCenter.y + angularCoefficient * sX * halfWidth
          )
        )
      } else
        Some(
          new Point2D(
            targetCenter.x + sY * halfHeight * (1 / angularCoefficient),
            targetCenter.y + sY * halfHeight
          )
        )
  }

  def getSidePoints(source: Point2D, target: Point2D, barbs: ArcBarbs): Option[(Point2D, Point2D)] = {
    if (source == target) {
      return None
    }

    val barbAngle =
      toRadians(barbs.angleInDegrees)

    val deltaX =
      source.x - target.x

    val deltaY =
      source.y - target.y

    val squaredDeltaY =
      deltaY * deltaY

    val a =
      deltaX * deltaX + squaredDeltaY

    val d =
      source.x * deltaX + source.y * deltaY - a + barbs.length * sqrt(a) * cos(barbAngle)

    val halfB =
      target.y * deltaX * deltaY - target.x * squaredDeltaY - d * deltaX

    val c =
      squaredDeltaY * (target.x * target.x + target.y * target.y - barbs.length * barbs.length) + d * (d - 2 * target.y * deltaY)

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

case class ArcBarbs(
                     angleInDegrees: Double,
                     length: Double
                    ) {
  require(0 < angleInDegrees && angleInDegrees < 90)

  require(length > 0)
}
