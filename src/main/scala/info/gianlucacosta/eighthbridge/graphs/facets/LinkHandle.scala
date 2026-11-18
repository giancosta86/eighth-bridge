package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.entities.IntBasedEntity
import scalafx.geometry.Point2D

class LinkHandle extends TimestampedPrototype
    with IntBasedEntity
    with HasCenterSettable
    with HasDimensionSettable {

  def bottomRight: Point2D =
    new Point2D(
      center.x + dimension.width / 2,
      center.y + dimension.height / 2
    )

  override def toString: String =
    s"LinkHandle{(${dimension.width} x ${dimension.height}) @ (${center.x}; ${center.y})}"
}

object LinkHandle {
  def createDefault(center: Point2D): LinkHandle =
    new LinkHandle()
      .setCenter(center)
      .setDimension(
        18,
        18
      )
}

