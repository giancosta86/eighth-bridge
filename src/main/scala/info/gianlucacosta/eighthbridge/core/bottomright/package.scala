package info.gianlucacosta.eighthbridge.core

import scalafx.geometry.Point2D

//TODO! Move all the package to helios-fx!
package object bottomright {
  implicit class Point2DExtension(self: Point2D) {
    def mergeToBottomRight(other: Point2D): Point2D =
      new Point2D(
        Math.max(self.x, other.x),
        Math.max(self.y, other.y)
      )
  }
}
