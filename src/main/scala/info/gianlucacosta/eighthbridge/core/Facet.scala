package info.gianlucacosta.eighthbridge.core

import scala.reflect.{ClassTag, classTag}

//TODO! To Helios-core?
object Facet {
  def as[TFacet: ClassTag](instance: AnyRef): Option[TFacet] =
    instance match {
      case instance if classTag[TFacet].runtimeClass.isInstance(instance) =>
        Some(instance.asInstanceOf[TFacet])

      case _ =>
        None
    }
}
