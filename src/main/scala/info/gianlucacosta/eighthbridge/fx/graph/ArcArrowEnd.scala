package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.{Dimension2D, Point2D}

object ArcArrowEnd {
  val Default = ArcArrowEnd(
    angle = math.Pi / 6,
    length = 15
  )

  def getIntersection(targetCenter: Point2D, targetSize: Dimension2D)(source: Point2D): Option[Point2D] = {
    val arrowSourceWithinVertexRectangle =
      targetCenter.x - targetSize.width/2 < source.x &&
        source.x < targetCenter.x + targetSize.width/2 &&
        targetCenter.y - targetSize.height/2 < source.y &&
        source.y < targetCenter.y + targetSize.height/2

    if (arrowSourceWithinVertexRectangle)
      None
    else {
      val deltaX =
        targetCenter.x - source.x

      if (deltaX == 0) {
        val sy =
          Math.signum(source.y - targetCenter.y)

        Some(
          new Point2D(
            targetCenter.x,

            targetCenter.y + sy * targetSize.height / 2
          )
        )
      } else {
        val angularCoefficient =
          (source.y - targetCenter.y) / deltaX

        if (Math.abs(angularCoefficient) <= targetSize.height / targetSize.width) {
          val sx =
            Math.signum(source.x - targetCenter.x)

          Some(
            new Point2D(
              targetCenter.x + sx * targetSize.width / 2,

              targetCenter.y - sx * angularCoefficient * targetSize.width / 2
            )
          )
        } else {
          val sy =
            Math.signum(source.y - targetCenter.y)

          Some(
            new Point2D(
              targetCenter.x - sy * targetSize.height / (2 * angularCoefficient),

              targetCenter.y + sy * targetSize.height / 2
            )
          )
        }
      }
    }
  }
}

case class ArcArrowEnd(
                      angle: Double,
                      length: Double
                    )
