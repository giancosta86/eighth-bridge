package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class NamedVisualVertex(
                              center: Point2D,
                              selected: Boolean,
                              styleClasses: Set[String],
                              name: String,
                              protected val timestamp: Long = System.nanoTime()
                              ) extends VisualVertex with Named {


  override def setCenter(center: Point2D): this.type = {
    if (center != this.center)
      copy(center = center)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setSelected(selected: Boolean): this.type = {
    if (selected != this.selected)
      copy(selected = selected)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setStyleClasses(styleClasses: Set[String]): this.type = {
    if (styleClasses != this.styleClasses)
      copy(styleClasses = styleClasses)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  def setName(name: String): this.type = {
    if (name != this.name)
      copy(name = name)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override protected def setTimestamp(timestamp: Long): this.type =
    copy(timestamp = timestamp)
      .asInstanceOf[this.type]
}
