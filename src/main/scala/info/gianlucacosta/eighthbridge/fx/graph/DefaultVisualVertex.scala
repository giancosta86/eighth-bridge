package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

case class DefaultVisualVertex(
                              center: Point2D,
                              selected: Boolean,
                              styleClasses: Set[String],
                              name: String,
                              protected val timestamp: Long = System.nanoTime()
                              ) extends VisualVertex with Named {


  override def setCenter(center: Point2D): this.type =
    copy(center = center)
      .asInstanceOf[this.type]

  override def setSelected(selected: Boolean): this.type =
    copy(selected = selected)
      .asInstanceOf[this.type]

  override def setStyleClasses(styleClasses: Set[String]): this.type =
    copy(styleClasses = styleClasses)
      .asInstanceOf[this.type]

  def setName(name: String): this.type =
    copy(name = name)
      .asInstanceOf[this.type]

  override protected def setTimestamp(timestamp: Long): this.type =
    copy(timestamp = timestamp)
      .asInstanceOf[this.type]
}
