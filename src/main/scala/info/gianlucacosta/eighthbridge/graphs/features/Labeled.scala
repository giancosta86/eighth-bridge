package info.gianlucacosta.eighthbridge.graphs.features

trait Labeled {
  def label: String

  def setLabel(label: String): this.type

  override def toString: String =
    label
}
