package info.gianlucacosta.eighthbridge.graphs.facets

import scalafx.geometry.Dimension2D

trait HasDimensionSettable extends HasTimestampSettable with HasDimension {
  private var _dimension: Dimension2D =
    new Dimension2D(0, 0)

  def dimension: Dimension2D =
    _dimension

  def setDimension(dimension: Dimension2D): this.type = {
    if (dimension != _dimension) {
      val result =
        updateTimestamp()

      result._dimension =
        dimension

      result
    } else
      this
  }

  def setDimension(width: Double, height: Double): this.type =
    setDimension(
      new Dimension2D(
        width,
        height
      )
    )
}
