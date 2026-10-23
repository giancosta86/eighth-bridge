package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.property.{ObjectProperty, ReadOnlyObjectProperty}
import scalafx.scene.Group
import scalafx.Includes._

private class HandleNodeGroup(initialHandles: List[ArcHandle]) extends Group {
  val handles: ObjectProperty[List[ArcHandle]] =
    new SimpleObjectProperty[List[ArcHandle]](initialHandles)

  val handleNodes: ReadOnlyObjectProperty[List[HandleNode]] =
    ReadOnlyProperties.createObject(
      () => Nil,

      handles
    )

}
