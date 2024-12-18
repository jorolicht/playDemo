# Playframework Environment

This is an attempt to create a full-fledged Playframework application with all the needed components. The challenge is to identify a necessary component stack and to keep the components up to date (kind of LAMP stack with playframework and Scala/ScalaJs as a basis).

## Component List
- Scala - programming language ([Scala] (https://www.scala-lang.org/))
- Scala.js - write Scala code that runs on the web browser ([Scala.js] (https://www.scala-js.org/))
- Play Framework - Web Framework for Scala ([Play] (https://www.playframework.com/))
- Giter8 Template - for Play Framework with Scala.js ([Giter8 Template](https://github.com/vmunier/play-scalajs.g8))
- uPickle - serialization library for Scala/Scala.js ([uPickle](http://com-lihaoyi.github.io/upickle))
- Bootstrap - free and open-source frontend framework ([Bootstrap](https://getbootstrap.com/docs/5.2/getting-started/introduction/))
- Font Awesome - icon library ([FONT AWESOME](https://github.com/FortAwesome/Font-Awesome))
- SB Admin - open source admin dashboard template ([Start Bootstrap - SB Admin](https://github.com/StartBootstrap/startbootstrap-sb-admin))
- Twirl - Html template engine ([Twirl](https://github.com/playframework/twirl))
- Logging - simple/tiny scalajs logging API ([scalajs-logging](https://github.com/scala-js/scala-js-logging))
- Mail - a powerful Scala Mailing library ([Play Mailer](https://github.com/playframework/play-mailer))
- Anorm - simple SQL data access ([Anorm](http://playframework.github.io/anorm/))
- MySql - popular open source database ([MySql](https://www.mysql.com/))
- Google Sign In - get user credential ([Google Identity](https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid))
- Cats - abstractions for functional programming ([Cats](https://typelevel.org/cats/))


## Components

### Playframework with Scala.js skeleton
Install Playframework with Scala.js skeleton through a Giter8 template. 
This shows how you can integrate a Play project with a Scala.js project.
For details see https://github.com/vmunier/play-scalajs.g8

See file projects/plugins.sbt for necessary plugins (versions):
```
addSbtPlugin("com.vmunier"               % "sbt-web-scalajs"           % "1.3.0")
addSbtPlugin("org.scala-js"              % "sbt-scalajs"               % "1.16.0")
addSbtPlugin("org.playframework"         % "sbt-plugin"                % "3.0.2")
addSbtPlugin("org.portable-scala"        % "sbt-scalajs-crossproject"  % "1.3.2")
addSbtPlugin("com.typesafe.sbt"          % "sbt-gzip"                  % "1.0.2")
addSbtPlugin("com.typesafe.sbt"          % "sbt-digest"                % "1.1.4")
```

Set Scala version in build.sbt: ```ThisBuild / scalaVersion := "3.3.3"```

### uPickle
uPickle (pronounced micro-pickle) is a lightweight JSON and binary (MessagePack) serialization library for Scala and ScalaJs (see http://com-lihaoyi.github.io/upickle/)

Add to build.sbt: ```libraryDependencies += "com.lihaoyi" %%% "upickle" % "3.2.0"``` for client and 
                  ```libraryDependencies += "com.lihaoyi" %% "upickle" % "3.2.0"```  for server


### Bootstrap
Bootstrap is a free and open-source CSS framework directed at responsive, mobile-first front-end web development.
See ([getbootstrap](https://getbootstrap.com/docs/5.2/getting-started/introduction/))

Copy-paste the stylesheet <link> into your <head> before all other stylesheets to load our CSS.
<link href="https://cdn.jsdelivr.net/npm/bootstrap@@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous"> 

Include every Bootstrap JavaScript plugin and dependency with one of our two bundles.
<script src="https://cdn.jsdelivr.net/npm/bootstrap@@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>

You have to escape the @-Symbol (double @@), twirl-Template!

Or download the distribution and copy it to the public folder, then:
<link rel="stylesheet" href='@routes.Assets.versioned("bootstrap-5.2.3/css/bootstrap.min.css")'>
<script src='@routes.Assets.versioned("bootstrap-5.2.3/js/bootstrap.bundle.min.js")'></script>


### Font Awesome
Add awesome font ([see github](https://github.com/FortAwesome/Font-Awesome))
Look for older versions "Check the ([releases](https://github.com/FortAwesome/Font-Awesome/releases))
Download the fontawesome-free-6.6.0-web.zip archive and copy it to the server/public folder.

<link rel="stylesheet" href='routes.Assets.versioned("fontawesome-free-6.6.0-web/css/all.min.css")'>


### SB Admin
SB Admin is an open source, admin dashboard template for Bootstrap created by Start Bootstrap.
See: https://github.com/StartBootstrap/startbootstrap-sb-admin
Download dist folder and copy style sheet and javascript files to public folder add following lines to main.scala.html

<link rel="stylesheet" href='@routes.Assets.versioned("sbAdmin-7.0.7/css/styles.css")'>
<script src='@routes.Assets.versioned("sbAdmin-7.0.7/js/scripts.js")'></script>


### Twirl Template
Twirl template files are expected to be placed under src/main/twirl or src/test/twirl, similar to scala. Using template files also on client site, just add in build.sbt (no plugin required)
```lazy val client = project.enablePlugins(SbtTwirl)```


### Logging
On client site I take the simple/tiny scalajs logging API
See https://github.com/scala-js/scala-js-logging, copied the source on client site to org.scalajs.logging.
No new version for Scala 3 found (last update in 2020), so copied source and build it.
On server site standard logging api, see conf/logback.xml for configuration.


### Mail
See https://github.com/playframework/play-mailer for details.

Add to build.sbt (server): 
```
libraryDependencies += "org.playframework" %% "play-mailer" % "10.0.0"
libraryDependencies += "org.playframework" %% "play-mailer-guice" % "10.0.0"
```

### Anorm/MySQL
```
libraryDependencies ++= Seq(
      jdbc,
      evolutions,
      "com.mysql" % "mysql-connector-j" % "8.3.0",      
      "org.playframework.anorm" %% "anorm" % "2.7.0",
)
```


### Google Sign In

#### Sign in on client side (Javascript)

As prerequisite you have to configure the Google API and obtain a Client ID, see: https://developers.google.com/identity/gsi/web/guides/get-google-api-clientid. Don't forget to add (http://localhost:9000) as an authorized URL, to test it locally.

Then you could integrate the login button into your web page, see https://developers.google.com/identity/gsi/web/guides/display-button. To test it locally set up a simple web server (e.g. ```python3 -m http.server 9000```) and create a simple html page (e.g. index.html) with the following content:

```
<script src="https://accounts.google.com/gsi/client"></script>
<script>
  function handleCredentialResponse(response) {
    if (response.credential) {
      var credential = response.credential;
      console.log('ID Token: ' + credential);
      // Send the ID Token to your server for authentication
      // e.g. post to checkGoogleCredentials action
    } else {
      console.log('No credential available');
    }
  }  
</script>
<div id="g_id_onload"
    data-client_id="xxxxxxxxx.apps.googleusercontent.com"
    data-context="signin"
    data-ux_mode="popup"
    data-callback="handleCredentialResponse"
    data-auto_prompt="false">
</div>
<div class="g_id_signin"
    data-type="standard"
    data-shape="rectangular"
    data-theme="outline"
    data-text="signin_with"
    data-size="large"
    data-logo_alignment="left">
</div>
```

#### Verify Credentials on server side 

See verify the Google ID token on your server side (https://developers.google.com/identity/gsi/web/guides/verify-google-id-token).There is no Scala example, so using the JAVA example and rework it for Scala. 

Required libraries for server:
```
libraryDependencies += "com.google.api-client" % "google-api-client" % "2.4.0"
```

Post action verifying the token, get the user data.
```
def checkGoogleCredentials(): Action[AnyContent] = Action { implicit request =>
  import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
  import com.google.api.client.http.javanet.NetHttpTransport
  import com.google.api.client.json.gson.GsonFactory

  val transport   = new NetHttpTransport()
  val jsonFactory = GsonFactory.getDefaultInstance
  val verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
    .setAudience(Seq("xxxxxxxxx.apps.googleusercontent.com").asJava)
    .build()

  val idTokenString= request.body.asText.getOrElse("")
  val idToken = verifier.verify(idTokenString)

  if (idToken != null) then
    val payload = idToken.getPayload
    val userId = payload.getSubject
    val email = payload.getEmail

  String email = payload.getEmail();
  boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
  String name = (String) payload.get("name");
  String pictureUrl = (String) payload.get("picture");
  String locale = (String) payload.get("locale");
  String familyName = (String) payload.get("family_name");
  String givenName = (String) payload.get("given_name");



    // You can access more information from the payload as needed
    Ok(s"Verified user: $userId, email: $email")


  else
    BadRequest("Invalid ID token.")
}
```

### Cats
Add cats-core library for client and server targets to get [EitherT](https://typelevel.org/cats/datatypes/eithert.html).EitherT[F[_], A, B] is a lightweight wrapper for F[Either[A, B]] that makes it easy to compose Eithers and Fs together which is extremly useful.

```
libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0"   //server
libraryDependencies += "org.typelevel" %%% "cats-core" % "2.12.0"  //client
```

# Production Environment

## Ubuntu 24.04 LTS Server

### Installation of Docker

https://docs.docker.com/engine/install/ubuntu/








