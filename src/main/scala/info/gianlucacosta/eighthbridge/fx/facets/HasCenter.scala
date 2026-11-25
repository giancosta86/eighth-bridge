package info.gianlucacosta.eighthbridge.fx.facets

import info.gianlucacosta.eighthbridge.core.Vertex
import scalafx.geometry.Point2D

trait HasCenter extends Vertex {
  def center: Point2D
}
