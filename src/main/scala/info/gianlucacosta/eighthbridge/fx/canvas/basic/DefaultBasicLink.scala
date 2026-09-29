package info.gianlucacosta.eighthbridge.fx.canvas.basic

import java.util.UUID

import scalafx.geometry.Point2D

/**
  * Default BasicLink implementation
  */
case class DefaultBasicLink(
                             text: String = "",
                             styleClasses: List[String] = List(),
                             internalPoints: List[Point2D] = List(),
                             selected: Boolean = false,
                             labelCenter: Option[Point2D] = None,
                             id: UUID = UUID.randomUUID()
                           ) extends BasicLink[DefaultBasicLink] {


  override def visualCopy(
                           internalPoints: List[Point2D],
                           selected: Boolean,
                           labelCenter: Option[Point2D]
                         ): DefaultBasicLink =
    copy(
      internalPoints = internalPoints,
      selected = selected,
      labelCenter = labelCenter
    )
}
