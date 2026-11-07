package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.facets.{Prototype, HasCenterSettable, HasSelectedSettable, HasStyleClassesSettable}

class VisualVertex extends Prototype
  with HasCenterSettable
  with HasSelectedSettable
  with HasStyleClassesSettable