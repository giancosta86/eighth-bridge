package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.fx.graph.features.Labeled
import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class LabeledVisualArc(
                             selected: Boolean,
                             labelCenter: Option[Point2D],
                             handlePoints: List[ArcHandlePoint],
                             styleClasses: Set[String],
                             arrowEnd: Option[ArcArrowEnd],
                             label: String,
                             protected val timestamp: Long
) extends VisualArc with Labeled {
  override def setSelected(selected: Boolean): this.type = {
    if (selected != this.selected)
      copy(selected = selected)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setLabelCenter(labelCenter: Option[Point2D]): this.type = {
    if (labelCenter != this.labelCenter)
      copy(labelCenter = labelCenter)
        .updateTimestamp()
        .asInstanceOf[this.type]
    else
      this
  }

  override def setLabel(label: String): this.type = {
    if (label != this.label)
      copy(label = label)
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
