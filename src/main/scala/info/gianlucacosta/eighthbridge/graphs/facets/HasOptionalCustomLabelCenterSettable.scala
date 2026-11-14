package info.gianlucacosta.eighthbridge.graphs.facets

import scalafx.geometry.Point2D

trait HasOptionalCustomLabelCenterSettable extends TimestampedPrototype with HasOptionalCustomLabelCenter {
  private var _customLabelCenter: Option[Point2D] =
    None

  override def customLabelCenter: Option[Point2D] =
    _customLabelCenter

  def setCustomLabelCenter(customLabelCenter: Option[Point2D]): this.type =
    if (customLabelCenter != _customLabelCenter) {
      val result =
        clone()

      result._customLabelCenter =
        customLabelCenter

      result
    } else
      this
}
