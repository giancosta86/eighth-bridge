package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.entities.IntBasedEntity
import scalafx.geometry.Point2D

final class LinkHandle extends TimestampedPrototype
    with IntBasedEntity
    with HasCenterSettable
    with HasDimensionSettable {

  override def toString: String =
    s"LinkHandle{(${dimension.width} x ${dimension.height}) @ (${center.x}; ${center.y})}"
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

