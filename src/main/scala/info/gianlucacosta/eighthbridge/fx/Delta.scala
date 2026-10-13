package info.gianlucacosta.eighthbridge.fx

import scalafx.geometry.Point2D

case class Delta(x: Double, y: Double)

object Delta {
  implicit class Point2DDeltaExtensions(point: Point2D) {
    def +(delta: Delta): Point2D =
      new Point2D(
        point.x + delta.x,
        point.y + delta.y
      )

    def -(other: Point2D): Delta =
      new Delta(
        point.x - other.x,
        point.y - other.y
      )
  }
}
