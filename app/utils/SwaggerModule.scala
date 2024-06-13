package utils

import javax.inject.{Inject, Provider}
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class SwaggerModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[SwaggerConfig].toProvider[SwaggerConfigProvider]
  )
}

class SwaggerConfigProvider @Inject()(configuration: Configuration, environment: Environment) extends Provider[SwaggerConfig] {
  override def get(): SwaggerConfig = {
    val baseUrl = configuration.get[String]("swagger.baseUrl")
    SwaggerConfig(baseUrl)
  }
}

case class SwaggerConfig(baseUrl: String)
