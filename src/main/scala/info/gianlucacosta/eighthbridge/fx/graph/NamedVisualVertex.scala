package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.{Labeled, Named}
import scalafx.geometry.Point2D

class NamedVisualVertex(
                         centerValue: Point2D, nameValue: String
           ) extends VisualVertex(centerValue) with Named with Labeled with Cloneable {
  private var _name: String =
    nameValue

  def name: String =
    _name

  def label: String =
    _name

  def setName(name: String): this.type =
    if (name != _name) {
      val result =
        updateTimestamp()

      result._name =
        name

      result
    } else
      this
}