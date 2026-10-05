package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualVertex

/**
  * Vertex dedicated to the "basic" package
  */
trait BasicVertex extends VisualVertex {
  def text: String

  override def toString: String =
    text
}
