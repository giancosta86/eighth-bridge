package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.{Dimension2D, Point2D}

import java.lang.Math.{abs, signum}

//TODO! To Helios-fx?
object Segments {
  /**
   * Returns the intersection point between the segment connecting two points (`sourcePoint` and `targetCenter)`)
   * and the rectangle whose center is targetCenter and whose sides are `targetSize`.
   *
   * @param sourcePoint  The origin of the segment - usually out of the rectangle.
   * @param targetCenter The center of the rectangle.
   * @param targetSize   The size of the rectangle.
   * @return The intersection point - which could be None if the source point is internal to the rectangle.
   */
  def getIntersectionWithRectangle(sourcePoint: Point2D, targetCenter: Point2D, targetSize: Dimension2D): Option[Point2D] = {
    val deltaX =
      sourcePoint.x - targetCenter.x

    val deltaY =
      sourcePoint.y - targetCenter.y

    val halfWidth =
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

  def getIntersectionWithRoundedRectangle(
                                           sourcePoint: Point2D,
                                           targetCenter: Point2D,
                                           targetSize: Dimension2D,
                                           borderRadius: Double
                                         ): Option[Point2D] = {
    require(0 <= borderRadius && borderRadius <= Math.min(targetSize.width / 2, targetSize.height / 2))

    getIntersectionWithRectangle(
      sourcePoint =
        sourcePoint,
      targetCenter =
        targetCenter,
      targetSize =
        targetSize
    ).map(intersectionWithRectangle => {
      if (borderRadius > 0) {
        val overlappingWidth =
          targetSize.width / 2 - borderRadius

        val overlappingHeight =
          targetSize.height / 2 - borderRadius

        val zetaX =
          intersectionWithRectangle.x - targetCenter.x

        val zetaY =
          intersectionWithRectangle.y - targetCenter.y

        if (Math.abs(zetaX) > overlappingWidth && Math.abs(zetaY) > overlappingHeight) {
          val m =
            zetaY / zetaX

          val deltaX =
            Math.signum(zetaX) * overlappingWidth

          val deltaY =
            Math.signum(zetaY) * overlappingHeight

          val deltaOverFour =
            m * deltaX * (2 * deltaY - m * deltaX) - deltaY * deltaY + borderRadius * borderRadius * (m * m + 1)


          println(s"DELTA OVER FOUR IS: ${deltaOverFour}")

          val omega =
            (deltaX + m * deltaY + Math.signum(deltaX) * Math.sqrt(deltaOverFour)) / (m * m + 1)

          new Point2D(
            targetCenter.x + omega,
            targetCenter.y + m * omega
          )
        } else
          intersectionWithRectangle
      } else {
        intersectionWithRectangle
      }
    })
  }
}
