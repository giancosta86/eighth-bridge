package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.fx.graph.features.Labeled
import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

class LabeledVisualArc(arrowEndValue: Option[ArcArrowEnd], labelValue: String) extends VisualArc(arrowEndValue) with Labeled {
  private var _label: String =
    labelValue

  def label: String =
    _label

  def setLabel(label: String) =
    if (label != _label) {
      val result =
        updateTimestamp()

      result._label =
        label

      result
    } else
      this

}
