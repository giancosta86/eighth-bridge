package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.property.{ObjectProperty, ReadOnlyObjectProperty}
import scalafx.scene.Group
import scalafx.Includes._

private class HandleNodeGroup(
                               initialHandles: List[ArcHandle],
                               onHandleNodeRightClick: HandleNodeRightClickHandler,
                               onHandleNodeDrag: HandleNodeDragHandler
                             ) extends Group {
  val handles: ObjectProperty[List[ArcHandle]] =
    new SimpleObjectProperty[List[ArcHandle]]

  private val _handleNodes =
    new SimpleObjectProperty[List[HandleNode]](Nil)

  val handleNodes: ReadOnlyObjectProperty[List[HandleNode]] =
    _handleNodes

  handles.onInvalidate {
    val updatedHandles =
      handles()

    //println(s"/////////////// The updated handles are: ${updatedHandles}")

    val currentHandleNodes =
      _handleNodes().toSet

    val handleNodesToRemove =
      currentHandleNodes.filter(handleNode =>
        !updatedHandles.contains(handleNode.handle())
      )

    handleNodesToRemove.foreach(children.remove(_))

    val keptHandleNodesByHandle: Map[ArcHandle, HandleNode] =
      (currentHandleNodes -- handleNodesToRemove)
        .map(handleNode =>
          handleNode.handle() -> handleNode
      )
        .toMap


    _handleNodes() =
      updatedHandles.map(handle => {
        val foundHandleNode =
          keptHandleNodesByHandle
            .get(handle)

        foundHandleNode match {
          case Some(foundHandleNode) =>
            foundHandleNode.handle() =
              handle

            foundHandleNode

          case None =>
            val brandNewHandleNode =
              new HandleNode(
                handle,
                onRightClick =
                  onHandleNodeRightClick,
                onDrag =
                  onHandleNodeDrag
              )

            children.add(brandNewHandleNode)

            brandNewHandleNode
        }
      })
  }

  handles() =
    initialHandles
}
