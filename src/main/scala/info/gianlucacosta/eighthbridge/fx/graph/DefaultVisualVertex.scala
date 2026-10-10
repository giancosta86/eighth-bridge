package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class DefaultVisualVertex(
                              center: Point2D,
                              selected: Boolean,
                              styleClasses: Set[String],
                              name: String
                              ) extends VisualVertex with Named {


  override def setCenter(value: Point2D): DefaultVisualVertex.this.type =
    copy(center = value)
      .asInstanceOf[this.type]

  override def setSelected(value: Boolean): DefaultVisualVertex.this.type =
    copy(selected = value)
      .asInstanceOf[this.type]

  override def setStyleClasses(value: Set[String]): DefaultVisualVertex.this.type =
    copy(styleClasses = value)
      .asInstanceOf[this.type]

  def setName(value: String): this.type =
    copy(name = value)
      .asInstanceOf[this.type]
}
