package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.entities.Entity
import scalafx.geometry.{Dimension2D, Point2D}
import info.gianlucacosta.eighthbridge.fx.graph.Delta._


final class ArcHandle(
  private var _center: Point2D,
  private var _dimension: Dimension2D
) extends Entity with Cloneable {
  def center: Point2D =
    _center

  def dimension: Dimension2D =
    _dimension

  def moveTo(updatedCenter: Point2D): ArcHandle = {
    val result =
      clone().asInstanceOf[ArcHandle]

    result._center =
      updatedCenter

    result
  }
}

object ArcHandle {
  def makeCircle(center: Point2D): ArcHandle =
    new ArcHandle(
      center,
      new Dimension2D(
        18,
        18
      )
    )
}

