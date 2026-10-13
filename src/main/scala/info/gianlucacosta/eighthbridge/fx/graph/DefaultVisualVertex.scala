package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.Point2D

case class DefaultVisualVertex(
                              center: Point2D,
                              selected: Boolean,
                              styleClasses: Set[String],
                              protected val timestamp: Long
                            ) extends VisualVertex {


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

  override protected def setTimestamp(timestamp: Long): this.type =
    copy(timestamp = timestamp)
      .asInstanceOf[this.type]
}
