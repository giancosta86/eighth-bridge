package info.gianlucacosta.eighthbridge.theory

package object features {
  type Weight =
    Double

  object Weight {
    val PositiveInfinity: Weight =
      Double.PositiveInfinity

    val NegativeInfinity: Weight =
      Double.NegativeInfinity
  }
}
