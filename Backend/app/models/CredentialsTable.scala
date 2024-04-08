package models
// AUTO-GENERATED Slick data model for table Credentials
trait CredentialsTable {

  self:TablesRoot with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Credentials
   *  @param username Database column username SqlType(varchar), Length(100,true)
   *  @param email Database column email SqlType(varchar), Length(100,true)
   *  @param password Database column password SqlType(varchar), Length(200,true)
   *  @param userId Database column user_id SqlType(int8)
   *  @param role Database column role SqlType(varchar), Length(20,true), Default(Some(normal)) */
  case class CredentialsRow(username: String, email: String, password: String, userId: Long, role: Option[String] = Some("normal"))
  /** GetResult implicit for fetching CredentialsRow objects using plain SQL queries */
  implicit def GetResultCredentialsRow(implicit e0: GR[String], e1: GR[Long], e2: GR[Option[String]]): GR[CredentialsRow] = GR{
    prs => import prs._
    CredentialsRow.tupled((<<[String], <<[String], <<[String], <<[Long], <<?[String]))
  }
  /** Table description of table credentials. Objects of this class serve as prototypes for rows in queries. */
  class Credentials(_tableTag: Tag) extends profile.api.Table[CredentialsRow](_tableTag, "credentials") {
    def * = (username, email, password, userId, role).<>(CredentialsRow.tupled, CredentialsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(username), Rep.Some(email), Rep.Some(password), Rep.Some(userId), role)).shaped.<>({r=>import r._; _1.map(_=> CredentialsRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column username SqlType(varchar), Length(100,true) */
    val username: Rep[String] = column[String]("username", O.Length(100,varying=true))
    /** Database column email SqlType(varchar), Length(100,true) */
    val email: Rep[String] = column[String]("email", O.Length(100,varying=true))
    /** Database column password SqlType(varchar), Length(200,true) */
    val password: Rep[String] = column[String]("password", O.Length(200,varying=true))
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column role SqlType(varchar), Length(20,true), Default(Some(normal)) */
    val role: Rep[Option[String]] = column[Option[String]]("role", O.Length(20,varying=true), O.Default(Some("normal")))

    /** Foreign key referencing Users (database name credentials_user_id_fkey) */
    lazy val usersFk = foreignKey("credentials_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (email) (database name unique_email) */
    val index1 = index("unique_email", email, unique=true)
    /** Uniqueness Index over (username) (database name unique_username) */
    val index2 = index("unique_username", username, unique=true)
  }
  /** Collection-like TableQuery object for table Credentials */
  lazy val Credentials = new TableQuery(tag => new Credentials(tag))
}
