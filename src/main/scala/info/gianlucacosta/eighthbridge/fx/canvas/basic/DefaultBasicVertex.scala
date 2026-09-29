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
                             ) extends BasicVertex[DefaultBasicVertex] {


  override def visualCopy(center: Point2D, selected: Boolean): DefaultBasicVertex =
    copy(
      center = center,
      selected = selected
    )
}
