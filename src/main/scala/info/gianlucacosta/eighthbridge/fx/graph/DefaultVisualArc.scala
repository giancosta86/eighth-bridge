package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class DefaultVisualArc(
                             selected: Boolean,
                             customLabelCenter: Option[Point2D],
                             handlePoints: List[ArcHandlePoint],
                             styleClasses: Set[String],
                             arrowEnd: Option[ArcArrowEnd],
                             name: String,
                             protected val timestamp: Long
) extends VisualArc {
  override def setSelected(selected: Boolean): this.type = {
    if (selected != this.selected)
      copy(selected = selected)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setCustomLabelCenter(customLabelCenter: Option[Point2D]): this.type = {
    if (customLabelCenter != this.customLabelCenter)
      copy(customLabelCenter = customLabelCenter)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setName(name: String): this.type = {
    if (name != this.name)
      copy(name = name)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setHandlePoints(handlePoints: List[ArcHandlePoint]): this.type = {
    if (handlePoints != this.handlePoints)
      copy(handlePoints = handlePoints)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setStyleclasses(styleClasses: Set[String]): this.type = {
    if (styleClasses != this.styleClasses)
      copy(styleClasses = styleClasses)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setArrowEnd(arrowEnd: Option[ArcArrowEnd]): this.type = {
    if (arrowEnd != this.arrowEnd)
      copy(arrowEnd = arrowEnd)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override protected def setTimestamp(timestamp: Long): this.type =
    copy(timestamp = timestamp)
      .asInstanceOf[this.type]
}
