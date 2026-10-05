package info.gianlucacosta.eighthbridge.fx.canvas.basic

import java.util.UUID

import scalafx.geometry.Point2D

/**
  * Default BasicVertex implementation
  */
case class DefaultBasicVertex(
                               text: String = "",
                               styleClasses: List[String] = List(),
                               center: Point2D = Point2D.Zero,
                               selected: Boolean = false,
                               id: UUID = UUID.randomUUID()
                             ) extends BasicVertex {

  override def visualCopy(center: Point2D, selected: Boolean): this.type =
    copy(
      center = center,
      selected = selected
    ).asInstanceOf[this.type]
}
