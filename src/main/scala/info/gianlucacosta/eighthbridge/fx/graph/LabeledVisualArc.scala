package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.{Labeled, Named}
import scalafx.geometry.Point2D

class LabeledVisualArc(barbsValue: Option[ArcBarbs], labelValue: String) extends VisualArc(barbsValue) with Labeled {
  private var _label: String =
    labelValue

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

}
