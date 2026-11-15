package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.facets.{HasCenter, HasSelected, HasStyleClasses, TimestampedPrototype}

trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenter
  with HasSelected
