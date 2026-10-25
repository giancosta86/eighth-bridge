package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.features.{Labeled, Named}
import scalafx.geometry.Point2D

class LabeledVisualVertex(
                         centerValue: Point2D, labelValue: String
           ) extends VisualVertex(centerValue) with Labeled with Cloneable {
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