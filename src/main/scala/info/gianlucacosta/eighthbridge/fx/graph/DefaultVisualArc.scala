package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.Point2D

case class DefaultVisualArc(
                             selected: Boolean,
                             labelCenter: Option[Point2D],
                             name: String,
                             internalPoints: List[Point2D],
                             styleClasses: Set[String],
                             arrow: ArcArrow = ArcArrow.Default,
                             handleRadius: ArcHandleRadius = ArcHandleRadius.Default
) extends VisualArc {
  override def setSelected(value: Boolean): DefaultVisualArc.this.type =
    copy(selected = value)
      .asInstanceOf[this.type]

  override def setLabelCenter(value: Option[Point2D]): DefaultVisualArc.this.type =
    copy(labelCenter = value)
      .asInstanceOf[this.type]

  override def setName(value: String): DefaultVisualArc.this.type =
    copy(name = value)
      .asInstanceOf[this.type]

  override def setInternalPoints(value: List[Point2D]): DefaultVisualArc.this.type =
    copy(internalPoints = value)
      .asInstanceOf[this.type]

  override def setStyleclasses(value: Set[String]): DefaultVisualArc.this.type =
    copy(styleClasses = value)
      .asInstanceOf[this.type]

  override def setArrow(value: ArcArrow): DefaultVisualArc.this.type =
    copy(arrow = value)
      .asInstanceOf[this.type]

  override def setHandleRadius(value: ArcHandleRadius): DefaultVisualArc.this.type =
    copy(handleRadius = value)
      .asInstanceOf[this.type]
}
