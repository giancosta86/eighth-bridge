package info.gianlucacosta.eighthbridge.theory

package object features {
  //TODO! Replace "Long" with "Timestamp" whenever possible!
  type Timestamp =
    Long

  type Weight =
    Double

  object Weight {
    val PositiveInfinity: Weight =
      Double.PositiveInfinity

    val NegativeInfinity: Weight =
      Double.NegativeInfinity
  }
}
