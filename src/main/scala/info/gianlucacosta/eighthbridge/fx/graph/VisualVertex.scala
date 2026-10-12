package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Vertex
import info.gianlucacosta.eighthbridge.theory.features.Timestamped
import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
trait VisualVertex
  extends Vertex with Timestamped {
  def center: Point2D

  def setCenter(center: Point2D): this.type

  def selected: Boolean

  def setSelected(selected: Boolean): this.type

  def styleClasses: Set[String]

  def setStyleClasses(styleClasses: Set[String]): this.type
}
