package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.entities.Entity
import scalafx.geometry.{Dimension2D, Point2D}
import info.gianlucacosta.eighthbridge.fx.graph.Delta._


case class ArcHandle(
  center: Point2D,
  dimension: Dimension2D
) extends Entity {
  def moveBy(delta: Delta): ArcHandle =
    copy(
      center =
        center + delta
    )
}

object ArcHandle {
  def makeCircle(center: Point2D): ArcHandle =
    ArcHandle(
      center,
      new Dimension2D(
        18,
        18
      )
    )
}

