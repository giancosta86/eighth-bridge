package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.{Dimension2D, Point2D}

import java.lang.Math.{abs, signum}

//TODO! To Helios-fx?
object Segments {
  /**
   * Returns the intersection point between the segment connecting two points (`sourcePoint` and `targetCenter)`)
   * and the rectangle whose center is targetCenter.
   *
   * @param sourcePoint The origin of the segment lying externally or on the sides of the rectangle.
   * @param targetCenter The center of the rectangle.
   * @param targetSize The size of the rectangle.
   * @return The intersection point - which could be None if the source point is internal to the rectangle.
   */
  def getIntersectionWithRectangle(sourcePoint: Point2D, targetCenter: Point2D, targetSize: Dimension2D): Option[Point2D] = {
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
}
