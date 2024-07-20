package usecases.group1

import base._
import shared._
import services._
import org.scalajs.dom.{ Event }
import org.scalajs.dom.raw.{ HTMLElement, HTMLTextAreaElement }
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue


object ChatExample extends UseCase with Mgmt with ComWrapper with JsWrapper with NameOf:
  import Ids._

  object Ids:  
    // used id attributes in template
    val ChatExample_Send:String         = nameOf(ChatExample_Send)
    val ChatExample_RcvMsgs:String      = nameOf(ChatExample_RcvMsgs)
    val ChatExample_Receiver:String     = nameOf(ChatExample_Receiver)
    val ChatExample_Message:String      = nameOf(ChatExample_Message)
  
  def render(param: String = ""): Boolean = 
    import cviews.usecases.group1._
    setMain(html.ChatExample(Global.unique))


  override def event(elem: HTMLElement, event: Event) =
    println(s"Event ${elem.id}")
    elem.id match
      //case ChatExample_Send => sendChatMsg( getInput(gE(ChatExample_Receiver),""), getInput(gE(ChatExample_Message),"") )
      case ChatExample_Send => 
        sendChatMsg(getId, getInput(gE(ChatExample_Receiver),""), getInput(gE(ChatExample_Message),"") ).map {
          case Left(err)  => error(s"sendChatMsg -> ${err}") 
          case Right(res) => info(s"sendChatMsg -> ${res}")   
        }
      case _                => error(s"event -> invalid elem/key: ${elem.id}")     

  def receiveMsg(msg: String) =
    val textarea = gE(ChatExample_RcvMsgs).asInstanceOf[HTMLTextAreaElement]
    textarea.value = if textarea.value != "" then textarea.value + "\n" + msg else msg

