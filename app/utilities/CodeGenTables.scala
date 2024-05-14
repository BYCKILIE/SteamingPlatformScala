package utilities

object CodeGenTables extends App {
  slick.codegen.SourceCodeGenerator.run(
    "slick.jdbc.PostgresProfile",
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/streaming_platform?user=postgres&password=Sarmale2505",
    "C:\\Projects\\SD_Project\\project\\backend\\app",
    "models", None, None, ignoreInvalidDefaults = true, outputToMultipleFiles = true
  )
}
