package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

class NamedVisualVertex(centerValue: Point2D, nameValue: String)
  extends LabeledVisualVertex(centerValue, labelValue = nameValue) with Named  {
  private var _name: String =
    nameValue

  override def name: String =
    _name

  override def setName(name: String): this.type = {
    if (name != _name) {
      val result =
        this.updateTimestamp()

      result._name =
        name

      result
    } else
      this
  }
}
