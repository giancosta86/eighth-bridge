package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import java.util.UUID

import scalafx.geometry.Point2D

/**
  * Default VisualVertex implementation
  *
  * @param center
  * @param selected
  * @param id
  */
case class DefaultVisualVertex(
                                center: Point2D,

                                selected: Boolean = false,

                                styleClasses: List[String] = List(),

                                id: UUID = UUID.randomUUID()
                              ) extends VisualVertex[DefaultVisualVertex] {

  override def visualCopy(center: Point2D, selected: Boolean): DefaultVisualVertex =
    copy(center = center,
      selected = selected)
}