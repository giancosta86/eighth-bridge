package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.Point2D

case class Delta2D(x: Double, y: Double)

//TODO! Move this to Helios FX?
object Delta2D {
  implicit class Point2DDeltaExtensions(self: Point2D) {
    def +(delta: Delta2D): Point2D =
      new Point2D(
        self.x + delta.x,
        self.y + delta.y
      )

    def -(delta: Delta2D): Point2D =
      new Point2D(
        self.x - delta.x,
        self.y - delta.y
      )

    def -(other: Point2D): Delta2D =
      new Delta2D(
        self.x - other.x,
        self.y - other.y
      )
  }
}
