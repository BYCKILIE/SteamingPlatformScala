package models
// AUTO-GENERATED Slick data model for table History
trait HistoryTable {

  self:TablesRoot with PostTable with UsersTable with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table History
   *  @param userId Database column user_id SqlType(int8)
   *  @param postId Database column post_id SqlType(int8), Default(None)
   *  @param profileId Database column profile_id SqlType(int8), Default(None)
   *  @param viewingDate Database column viewing_date SqlType(varchar), Length(16,true), Default(None) */
  case class HistoryRow(userId: Long, postId: Option[Long] = None, profileId: Option[Long] = None, viewingDate: Option[String] = None)
  /** GetResult implicit for fetching HistoryRow objects using plain SQL queries */
  implicit def GetResultHistoryRow(implicit e0: GR[Long], e1: GR[Option[Long]], e2: GR[Option[String]]): GR[HistoryRow] = GR{
    prs => import prs._
    HistoryRow.tupled((<<[Long], <<?[Long], <<?[Long], <<?[String]))
  }
  /** Table description of table history. Objects of this class serve as prototypes for rows in queries. */
  class History(_tableTag: Tag) extends profile.api.Table[HistoryRow](_tableTag, "history") {
    def * = (userId, postId, profileId, viewingDate).<>(HistoryRow.tupled, HistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userId), postId, profileId, viewingDate)).shaped.<>({r=>import r._; _1.map(_=> HistoryRow.tupled((_1.get, _2, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column post_id SqlType(int8), Default(None) */
    val postId: Rep[Option[Long]] = column[Option[Long]]("post_id", O.Default(None))
    /** Database column profile_id SqlType(int8), Default(None) */
    val profileId: Rep[Option[Long]] = column[Option[Long]]("profile_id", O.Default(None))
    /** Database column viewing_date SqlType(varchar), Length(16,true), Default(None) */
    val viewingDate: Rep[Option[String]] = column[Option[String]]("viewing_date", O.Length(16,varying=true), O.Default(None))

    /** Foreign key referencing Post (database name history_post_id_fkey) */
    lazy val postFk = foreignKey("history_post_id_fkey", postId, Post)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name history_profile_id_fkey) */
    lazy val usersFk2 = foreignKey("history_profile_id_fkey", profileId, Users)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name history_user_id_fkey) */
    lazy val usersFk3 = foreignKey("history_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table History */
  lazy val History = new TableQuery(tag => new History(tag))
}
