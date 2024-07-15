package controllers

import javax.inject._
import upickle.default._
import scala.concurrent.{ExecutionContext, Future}
import scala.jdk.CollectionConverters._
import scala.quoted.*
import play.api.mvc._
import play.api.Logging
import play.api.i18n.{ I18nSupport, Messages, Langs, Lang }

import models._
import shared._
import shared.model._

@Singleton
class Main @Inject()(cc: ControllerComponents, userRepo: UserRepository)(implicit ec: ExecutionContext) 
  extends AbstractController(cc) with I18nSupport with Logging  {

  def home(name: String, param: String=""): Action[AnyContent] = Action { implicit request =>
    import java.util.Base64
    import java.nio.charset.StandardCharsets

    val msgs: Messages = messagesApi.preferred(request)
    val msgsB64 = encMsgs(messagesApi.messages(msgs.lang.code))

    logger.trace(s"home -> name=${name} param=${param}")
    Ok(views.html.main(name, param, msgsB64) )
  }

}