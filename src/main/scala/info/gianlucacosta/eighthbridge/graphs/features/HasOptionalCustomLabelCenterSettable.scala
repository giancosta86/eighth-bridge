package info.gianlucacosta.eighthbridge.graphs.features

import scalafx.geometry.Point2D

trait HasOptionalCustomLabelCenterSettable extends HasTimestampSettable with HasOptionalCustomLabelCenter {
  protected val initialCustomLabelCenter: Option[Point2D] =
    None

  private var _customLabelCenter: Option[Point2D] =
    initialCustomLabelCenter

  override def customLabelCenter: Option[Point2D] =
    _customLabelCenter

  def setCustomLabelCenter(customLabelCenter: Option[Point2D]): this.type =
    if (customLabelCenter != _customLabelCenter) {
      val result =
        updateTimestamp()

      result._customLabelCenter =
        customLabelCenter

      result
    } else
      this
}
