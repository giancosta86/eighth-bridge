package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.graphs.entities.IntBasedEntity
import scalafx.geometry.{Dimension2D, Point2D}

final class LinkHandle extends Prototype
    with IntBasedEntity
    with HasCenterSettable
    with HasDimensionSettable {

  override def toString: String =
    s"(${dimension.width} x ${dimension.height}) @ (${center.x}; ${center.y})"
}

object LinkHandle {
  def makeDefaultCircle(center: Point2D): LinkHandle =
    new LinkHandle()
      .setCenter(center)
      .setDimension(
        18,
        18
      )
}

