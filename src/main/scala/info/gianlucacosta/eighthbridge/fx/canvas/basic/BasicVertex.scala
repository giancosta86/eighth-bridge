package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualVertex

/**
  * Vertex dedicated to the "basic" package
  */
trait BasicVertex[V <: BasicVertex[V]] extends VisualVertex[V] {
  this: V =>

  def text: String

  override def toString: String =
    text
}
