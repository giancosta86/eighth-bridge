package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.features.{Labeled, Named}
import scalafx.geometry.Point2D

class LabeledVisualLink(
                        barbsValue: Option[ArcBarbs],
                        labelValue: String
                      ) extends VisualLink(barbsValue) with Labeled {
  private var _label: String =
    labelValue

  private var _customLabelCenter: Option[Point2D] =
    None

  def label: String =
    _label

  def setLabel(label: String): this.type =
    if (label != _label) {
      val result =
        updateTimestamp()

      result._label =
        label

      result
    } else
      this

  def customLabelCenter: Option[Point2D] =
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
