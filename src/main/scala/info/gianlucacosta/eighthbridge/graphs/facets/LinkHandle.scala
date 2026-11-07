package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.graphs.entities.IntBasedEntity
import scalafx.geometry.{Dimension2D, Point2D}

final class LinkHandle(
  centerValue: Point2D,
  dimensionValue: Dimension2D
) extends Prototype
    with IntBasedEntity
    with HasCenterSettable
    with HasDimensionSettable {
  override protected val initialCenter: Point2D =
    centerValue

  override protected val initialDimension: Dimension2D =
    dimensionValue
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

