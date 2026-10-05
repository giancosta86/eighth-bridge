package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import java.util.UUID

import scalafx.geometry.Point2D

/**
  * Default VisualLink implementation
  *
  * @param internalPoints
  * @param selected
  * @param labelCenter
  * @param id
  */
case class DefaultVisualLink(
                              internalPoints: List[Point2D] = Nil,

                              selected: Boolean = false,

                              labelCenter: Option[Point2D] = None,

                              styleClasses: List[String] = List(),

                              id: UUID = UUID.randomUUID()

                            ) extends VisualLink {
  override def visualCopy(internalPoints: List[Point2D], selected: Boolean, labelCenter: Option[Point2D]): this.type =
    copy(
      internalPoints = internalPoints,
      selected = selected,
      labelCenter = labelCenter
    ).asInstanceOf[this.type]
}
