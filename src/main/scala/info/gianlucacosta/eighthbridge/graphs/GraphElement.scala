package info.gianlucacosta.eighthbridge.graphs

import java.util.UUID

/**
  * Generic graph element (vertex, link, binding) - identified by its UUID.
  */
trait GraphElement {
  /**
    * The unique identification value.
    */
  val id: UUID

  override final def equals(obj: Any): Boolean =
    obj match {
      case other: GraphElement =>
        id == other.id

      case _ =>
        false
    }

  override final def hashCode(): Int =
    id.hashCode()
}