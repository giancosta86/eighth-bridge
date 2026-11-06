package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.features.{ConcreteCloneable, HasHandlesSettable, HasOptionalArcBarbsSettable, HasSelectedSettable, HasStyleClassesSettable, HasTimestampSettable}

class VisualLink extends ConcreteCloneable
  with Link
  with HasSelectedSettable
  with HasHandlesSettable
  with HasStyleClassesSettable
  with HasOptionalArcBarbsSettable

object VisualLink {
  def DefaultDirected: VisualLink =
    new VisualLink {
      protected override val initialBarbs: Option[ArcBarbs] =
        Some(ArcBarbs.Default)
    }

  def DefaultUndirected: VisualLink =
    new VisualLink {
      protected override val initialBarbs: Option[ArcBarbs] =
        None
    }
}