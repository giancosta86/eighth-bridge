package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import scalafx.geometry.Point2D

trait HasCenterSettable extends TimestampedPrototype with HasCenter {
  private var _center: Point2D =
    new Point2D(0,0)

  override def center: Point2D =
    _center

  def setCenter(center: Point2D): this.type =
    if (center != _center) {
      val result =
        clone()

      result._center =
        center

      result
    } else
      this

  def setCenter(x: Double, y: Double): this.type =
    setCenter(
      new Point2D(x, y)
    )
}
