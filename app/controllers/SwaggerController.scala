package controllers

import play.api.mvc.{Action, AnyContent, InjectedController}
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityRequirement

class SwaggerController extends InjectedController {

  @Operation(summary = "Swagger UI")
  @SecurityRequirement(name = "bearerAuth")
  def index: Action[AnyContent] = Action {
    Redirect(url = "/swagger-ui/index.html?url=/swagger.json", MOVED_PERMANENTLY)
  }
}
