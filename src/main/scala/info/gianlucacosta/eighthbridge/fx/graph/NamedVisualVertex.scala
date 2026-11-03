package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.features.Named
import scalafx.geometry.Point2D

object NamedVisualVertex {
  private def checkConstraints(name: String): Unit = {
    require(name.nonEmpty, "The name cannot be empty!")
  }
}

class NamedVisualVertex(centerValue: Point2D, nameValue: String)
  extends LabeledVisualVertex(centerValue, labelValue = nameValue) with Named  {
  NamedVisualVertex.checkConstraints(nameValue)

  private var _name: String =
    nameValue

  override def name: String =
    _name

  override def setName(name: String): this.type = {
    if (name != _name) {
      NamedVisualVertex.checkConstraints(name)

      val result =
        this
          .updateTimestamp()
          .setLabel(name)

      result._name =
        name

      result
    } else
      this
  }
}
