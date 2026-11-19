package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.Vertex
import info.gianlucacosta.eighthbridge.core.facets.{HasCenter, TimestampedPrototype}

trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenter
