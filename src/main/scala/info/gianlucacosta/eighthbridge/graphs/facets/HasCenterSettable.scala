package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import scalafx.geometry.Point2D

trait HasCenterSettable extends HasTimestampSettable with HasCenter {
  protected val initialCenter: Point2D =
    new Point2D(0,0)

  private var _center: Point2D =
    initialCenter

  override def center: Point2D =
    _center

  def setCenter(center: Point2D): this.type =
    if (center != _center) {
      val result =
        updateTimestamp()

      result._center =
        center

      result
    } else
      this

  //TODO! Search for the calls to setCenter, so as to use this simplified form!
  def setCenter(x: Double, y: Double): this.type =
    setCenter(
      new Point2D(x, y)
    )

  //TODO! Use this all over the library!
  def moveBy(delta: Delta): this.type =
    setCenter(center + delta)
}
