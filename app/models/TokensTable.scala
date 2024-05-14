package models
// AUTO-GENERATED Slick data model for table Tokens
trait TokensTable {

  self:TablesRoot with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Tokens
   *  @param userId Database column user_id SqlType(int8), Default(None)
   *  @param token Database column token SqlType(varchar), Length(255,true), Default(None) */
  case class TokensRow(userId: Option[Long] = None, token: Option[String] = None)
  /** GetResult implicit for fetching TokensRow objects using plain SQL queries */
  implicit def GetResultTokensRow(implicit e0: GR[Option[Long]], e1: GR[Option[String]]): GR[TokensRow] = GR{
    prs => import prs._
    TokensRow.tupled((<<?[Long], <<?[String]))
  }
  /** Table description of table tokens. Objects of this class serve as prototypes for rows in queries. */
  class Tokens(_tableTag: Tag) extends profile.api.Table[TokensRow](_tableTag, "tokens") {
    def * = (userId, token).<>(TokensRow.tupled, TokensRow.unapply)

    /** Database column user_id SqlType(int8), Default(None) */
    val userId: Rep[Option[Long]] = column[Option[Long]]("user_id", O.Default(None))
    /** Database column token SqlType(varchar), Length(255,true), Default(None) */
    val token: Rep[Option[String]] = column[Option[String]]("token", O.Length(255,varying=true), O.Default(None))

    /** Foreign key referencing Users (database name users_tokens_user_id_fkey) */
    lazy val usersFk = foreignKey("users_tokens_user_id_fkey", userId, Users)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)

    /** Uniqueness Index over (token) (database name users_tokens_token_key) */
    val index1 = index("users_tokens_token_key", token, unique=true)
  }
  /** Collection-like TableQuery object for table Tokens */
  lazy val Tokens = new TableQuery(tag => new Tokens(tag))
}
