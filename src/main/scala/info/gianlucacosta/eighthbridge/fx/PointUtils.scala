package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.Point2D

//TODO! To helios-fx!
object PointUtils {
  implicit class Point2DExtension(self: Point2D) {
    def mergeToBottomRight(other: Point2D): Point2D =
      new Point2D(
        Math.max(self.x, other.x),
        Math.max(self.y, other.y)
      )
  }
}
