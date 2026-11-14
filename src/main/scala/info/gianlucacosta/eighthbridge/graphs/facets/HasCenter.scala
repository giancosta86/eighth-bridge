package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.Vertex
import scalafx.geometry.Point2D

trait HasCenter extends Vertex {
  def center: Point2D
}
