package info.gianlucacosta.eighthbridge.fx

import java.net.URL

/**
  * Object providing default CSS stylesheet: you can employ it
  * as a starting point in your app by adding it to
  * the scene's stylesheet list (via scene.getStylesheets().add()) or as
  * required by your GUI framework.
  */
object Styles {
  val resourceUrl: URL =
    getClass.getResource("Styles.css")
}