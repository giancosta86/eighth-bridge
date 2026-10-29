package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.entities.{Entity, IntBasedEntity}
import scalafx.geometry.{Dimension2D, Point2D}
import info.gianlucacosta.eighthbridge.fx.graph.Delta._

final class LinkHandle(
  centerValue: Point2D,
  dimensionValue: Dimension2D
) extends IntBasedEntity with Cloneable {
  private var _center: Point2D =
    centerValue

  private var _dimension: Dimension2D =
    dimensionValue

  def center: Point2D =
    _center

  def setCenter(center: Point2D): LinkHandle = {
    val result =
      clone().asInstanceOf[LinkHandle]

    result._center =
      center

    result
  }

  def moveBy(delta: Delta): LinkHandle =
    setCenter(center + delta)

  def dimension: Dimension2D =
    _dimension

  def setDimension(dimension: Dimension2D): LinkHandle = {
    val result =
      clone().asInstanceOf[LinkHandle]

    result._dimension =
      dimension

    result
  }
}

object LinkHandle {
  def makeDefaultCircle(center: Point2D): LinkHandle =
    new LinkHandle(
      center,
      new Dimension2D(
        18,
        18
      )
    )
}

