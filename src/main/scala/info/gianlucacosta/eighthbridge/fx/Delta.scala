package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.Point2D

case class Delta(x: Double, y: Double)

object Delta {
  implicit class Point2DDeltaExtensions(self: Point2D) {
    def +(delta: Delta): Point2D =
      new Point2D(
        self.x + delta.x,
        self.y + delta.y
      )

    def -(other: Point2D): Delta =
      new Delta(
        self.x - other.x,
        self.y - other.y
      )
  }
}
