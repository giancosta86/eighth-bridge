package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.{Dimension2D, Point2D}

object ArcArrowEnd {
  val Default = ArcArrowEnd(
    angle = math.Pi / 6,
    length = 15
  )

  def getIntersection(vertexCenter: Point2D, vertexSize: Dimension2D)(arrowSource: Point2D): Option[Point2D] = {
    val arrowSourceWithinVertexRectangle =
      vertexCenter.x - vertexSize.width/2 < arrowSource.x &&
        arrowSource.x < vertexCenter.x + vertexSize.width/2 &&
        vertexCenter.y - vertexSize.height/2 < arrowSource.y &&
        arrowSource.y < vertexCenter.y + vertexSize.height/2

    if (arrowSourceWithinVertexRectangle)
      None
    else {
      val deltaX =
        vertexCenter.x - arrowSource.x

      if (deltaX == 0) {
        val sy =
          Math.signum(arrowSource.y - vertexCenter.y)

        Some(
          new Point2D(
            vertexCenter.x,

            vertexCenter.y + sy * vertexSize.height / 2
          )
        )
      } else {
        val angularCoefficient =
          (arrowSource.y - vertexCenter.y) / deltaX

        if (Math.abs(arrowSource.y / arrowSource.x) <= vertexSize.height / vertexSize.width) {
          val sx =
            Math.signum(arrowSource.x - vertexCenter.x)

          Some(
            new Point2D(
              vertexCenter.x + sx * vertexSize.width / 2,

              vertexCenter.y - sx * angularCoefficient * vertexSize.width / 2
            )
          )
        } else {
          val sy =
            Math.signum(arrowSource.y - vertexCenter.y)

          Some(
            new Point2D(
              vertexCenter.x - sy * vertexSize.height / (2 * angularCoefficient),

              vertexCenter.y + sy * vertexSize.height / 2
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
