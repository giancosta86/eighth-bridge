package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.graphs.facets.LinkHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.property.ObjectProperty
import scalafx.scene.Group

private class HandleNodeGroup(
                               initialHandles: List[LinkHandle],
                               onHandleNodeRightClick: HandleNodeRightClickHandler,
                               onHandleNodeDrag: HandleNodeDragHandler
                             ) extends Group {
  val handles: ObjectProperty[List[LinkHandle]] =
    new SimpleObjectProperty[List[LinkHandle]]

  private val handleNodes =
    new SimpleObjectProperty[List[HandleNode]](Nil)

  handles.onInvalidate {
    val updatedHandles =
      handles()

    val currentHandleNodes =
      handleNodes().toSet

    val handleNodesToRemove =
      currentHandleNodes.filter(handleNode =>
        !updatedHandles.contains(handleNode.handle())
      )

    handleNodesToRemove.foreach(children.remove(_))

    val keptHandleNodesByHandle: Map[LinkHandle, HandleNode] =
      (currentHandleNodes -- handleNodesToRemove)
        .map(handleNode =>
          handleNode.handle() -> handleNode
      )
        .toMap


    handleNodes() =
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
