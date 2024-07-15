import upickle.default._
import org.scalajs.dom
import org.scalajs.dom.raw.HTMLElement
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js
import scala.scalajs.js.annotation._
import scala.collection.mutable.Map

import services.ComWrapper
import base.{ Global, JsWrapper, Messages, Logging, _ }
import shared._
import shared.Ids._


@JSExportTopLevel("Main")
object Main extends ComWrapper with JsWrapper with Mgmt:
 
  /** main - entry point of application
   */  
  @JSExport
  def start(usecase: String, param: String, version: String, language: String, csrfToken: String): Unit = 
    // def evtMessage(e: dom.MessageEvent) = { 
    //   debug(s"Message from Server: ${e.data}")
    // }

    // set visibility of basic html elements
    addClass(gE(Main_JavascriptEnableInfo), "d-none")
    initUser
    
    // init app global variables
    setLang(language) 
    setCsrf(csrfToken)

    val evtSource = new dom.raw.EventSource(s"/helper/sse?id=${randomString(6)}")  
    evtSource.onmessage = { (e: dom.MessageEvent) => debug(s"Message from Server: ${e.data}") }

    Logging.setLogLevel("debug")
    debug(s"Main program initialized usecase/param:${usecase}/${param} version:${version} lang:${Global.lang}")
    ucExec(usecase, param)

  @JSExport
  def exec(usecase: String, param: String): Unit = ucExec(usecase, param)

  @JSExport
  def handleGoogleCredential(credentials: String): Unit = usecases.Auth.googleLogin(credentials)

  @JSExport
  def event(elem: HTMLElement, event: dom.Event): Unit =
    try
      val (usecase, key) = elem.id.toTuple("_")
      debug(s"event -> usecase:${usecase} key:${key} elem:${elem.id}")      
      Global.ucMap(usecase).event(elem, event)
    catch
      case e: Exception => error(s"event -> elem:${elem.id} failed") 

  @JSExport
  def getLogLevel():Option[String] =  Logging.getLogLevel()
 
  @JSExport
  def setLogLevel(value: String="") = Logging.setLogLevel(value)
