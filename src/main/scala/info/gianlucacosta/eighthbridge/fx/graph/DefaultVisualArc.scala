package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class DefaultVisualArc(
                             selected: Boolean,
                             customLabelCenter: Option[Point2D],
                             internalPoints: List[ArcInternalPoint],
                             styleClasses: Set[String],
                             arrowEnd: Option[ArcArrowEnd],
                             name: String
) extends VisualArc with Named {
  override def setSelected(selected: Boolean): DefaultVisualArc.this.type =
    copy(selected = selected)
      .asInstanceOf[this.type]

  override def setCustomLabelCenter(customLabelCenter: Option[Point2D]): DefaultVisualArc.this.type =
    copy(customLabelCenter = customLabelCenter)
      .asInstanceOf[this.type]

  override def setName(name: String): DefaultVisualArc.this.type =
    copy(name = name)
      .asInstanceOf[this.type]

  override def setInternalPoints(value: List[ArcInternalPoint]): DefaultVisualArc.this.type =
    copy(internalPoints = value)
      .asInstanceOf[this.type]

  override def setStyleclasses(styleClasses: Set[String]): DefaultVisualArc.this.type =
    copy(styleClasses = styleClasses)
      .asInstanceOf[this.type]

  override def setArrowEnd(arrowEnd: Option[ArcArrowEnd]): DefaultVisualArc.this.type =
    copy(arrowEnd = arrowEnd)
      .asInstanceOf[this.type]
}
