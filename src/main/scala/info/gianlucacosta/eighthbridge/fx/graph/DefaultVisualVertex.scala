package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class DefaultVisualVertex(
                              center: Point2D,
                              selected: Boolean,
                              styleClasses: Set[String],
                              name: String
                              ) extends VisualVertex with Named {


  override def setCenter(center: Point2D): DefaultVisualVertex.this.type =
    copy(center = center)
      .asInstanceOf[this.type]

  override def setSelected(selected: Boolean): DefaultVisualVertex.this.type =
    copy(selected = selected)
      .asInstanceOf[this.type]

  override def setStyleClasses(styleClasses: Set[String]): DefaultVisualVertex.this.type =
    copy(styleClasses = styleClasses)
      .asInstanceOf[this.type]

  def setName(name: String): this.type =
    copy(name = name)
      .asInstanceOf[this.type]
}
