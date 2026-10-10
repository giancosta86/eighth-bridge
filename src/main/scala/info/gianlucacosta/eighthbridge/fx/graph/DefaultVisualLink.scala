package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.Point2D

case class DefaultVisualLink(
          selected: Boolean,
          labelCenter: Option[Point2D],
          name: String,
          internalPoints: List[Point2D],
          styleClasses: Set[String],
          arrow: LinkArrow = LinkArrow.Default,
          handleRadius: LinkHandleRadius = LinkHandleRadius.Default
) extends VisualLink {
  override def setSelected(value: Boolean): DefaultVisualLink.this.type =
    copy(selected = value)
      .asInstanceOf[this.type]

  override def setLabelCenter(value: Option[Point2D]): DefaultVisualLink.this.type =
    copy(labelCenter = value)
      .asInstanceOf[this.type]

  override def setName(value: String): DefaultVisualLink.this.type =
    copy(name = value)
      .asInstanceOf[this.type]

  override def setInternalPoints(value: List[Point2D]): DefaultVisualLink.this.type =
    copy(internalPoints = value)
      .asInstanceOf[this.type]

  override def setStyleclasses(value: Set[String]): DefaultVisualLink.this.type =
    copy(styleClasses = value)
      .asInstanceOf[this.type]

  override def setArrow(value: LinkArrow): DefaultVisualLink.this.type =
    copy(arrow = value)
      .asInstanceOf[this.type]

  override def setHandleRadius(value: LinkHandleRadius): DefaultVisualLink.this.type =
    copy(handleRadius = value)
      .asInstanceOf[this.type]
}
