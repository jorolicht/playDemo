package controllers.test

import javax.inject._
import java.time.Instant
import java.util.Base64
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

import scala.concurrent.{ExecutionContext, Future}
import upickle.default._
import play.api.mvc._
import play.api.libs.mailer._
import play.api.{ Environment, Configuration, Logging }
import play.api.i18n.{ I18nSupport, Messages, Langs, Lang }

import org.apache.commons.mail.EmailAttachment

import models._
import shared._
import services._
import shared.model.{ Error, User }

@Singleton
class Auth @Inject()(cc: ControllerComponents, mailer: MailerClient, cfg: Configuration, userRepo: UserRepository)
  (implicit ec: ExecutionContext) extends AbstractController(cc) with I18nSupport with Logging with Encryption {

  given decoder: Base64.Decoder = Base64.getDecoder
  given encoder: Base64.Encoder = Base64.getEncoder

  //
  // Controller endpoints for TESTING
  //
  def getUserVerifyLink(email: String="", id: Long=0L): Action[AnyContent] = Action.async { implicit request =>
    logger.trace(s"getUserVerifyLink -> email=${email}")
    val msgs: Messages = messagesApi.preferred(request)
    userRepo.getUser(email,id).map {
      case Left(err)  => BadRequest(toJson(err.add("getUserVerifyLink")))
      case Right(usr) => 
        val verifyCode = Base64.getEncoder().encodeToString(usr.verifyInfo.getBytes())
        val verifyLink = msgs("email.register.link", verifyCode)
        Ok(verifyLink)
    }
  }     

  def getUserInfo(email: String="", id: Long=0L): Action[AnyContent] = Action.async { implicit request =>
    logger.trace(s"getUserInfo -> email=${email} id=${id}")
    userRepo.getUser(email, id).map {
      case Left(err)  => BadRequest(toJson(err.add("getUserInfo")))
      case Right(usr) => Ok(toJson(usr))
    }
  }       

  def checkUserAuth(): Action[AnyContent] = Action { implicit request =>
    logger.trace(s"checkUserAuth")
    getUserFromCookie(request) match {
      case Left(err)  => logger.trace(s"User not authenticated: ${err}"); Ok("User not authenticated")
      case Right(usr) => logger.trace(s"User authenticated: ${usr}");     Ok(s"User: ${usr} authenticated")
    }
  }

  // setUserVerify - (un)sets users verify flag
  def setUserVerify(id: Long, value: Boolean=false): Action[AnyContent] = Action.async { implicit request =>
    logger.trace(s"setUserVerify -> id=${id} value=${value}")
    userRepo.setVerified(id, value).map {
      case Left(err)  => BadRequest(toJson(err.add("setUserVerify")))
      case Right(res) => Ok(toJson(res))
    }
  }

}
  