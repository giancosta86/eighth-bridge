package info.gianlucacosta.eighthbridge.theory.features

trait Labeled {
  def label: String

  def setLabel(label: String): this.type
}
