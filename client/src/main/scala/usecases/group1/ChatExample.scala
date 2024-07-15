package usecases.group1

import base._
import shared._

object ChatExample extends UseCase with JsWrapper with NameOf:

  object Ids:  
    // used id attributes in template
    val ChatExample_Send:String         = nameOf(ChatExample_Send)
    val ChatExample_RcvMsgs:String      = nameOf(ChatExample_RcvMsgs)
  
  def render(param: String = ""): Boolean = 
    import cviews.usecases.group1._
    setMain(html.ChatExample("xxxxx"))


