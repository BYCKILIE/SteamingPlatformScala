package models
// AUTO-GENERATED Slick data model for table Comments
trait CommentsTable {

  self:TablesRoot with PostTable with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Comments
   *  @param data Database column data SqlType(varchar), Length(1000,true), Default(None)
   *  @param userId Database column user_id SqlType(int8)
   *  @param postId Database column post_id SqlType(int8), Default(None)
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
  case class CommentsRow(data: Option[String] = None, userId: Long, postId: Option[Long] = None, postingDate: Option[String] = None)
  /** GetResult implicit for fetching CommentsRow objects using plain SQL queries */
  implicit def GetResultCommentsRow(implicit e0: GR[Option[String]], e1: GR[Long], e2: GR[Option[Long]]): GR[CommentsRow] = GR{
    prs => import prs._
    CommentsRow.tupled((<<?[String], <<[Long], <<?[Long], <<?[String]))
  }
  /** Table description of table comments. Objects of this class serve as prototypes for rows in queries. */
  class Comments(_tableTag: Tag) extends profile.api.Table[CommentsRow](_tableTag, "comments") {
    def * = (data, userId, postId, postingDate).<>(CommentsRow.tupled, CommentsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((data, Rep.Some(userId), postId, postingDate)).shaped.<>({r=>import r._; _2.map(_=> CommentsRow.tupled((_1, _2.get, _3, _4)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column data SqlType(varchar), Length(1000,true), Default(None) */
    val data: Rep[Option[String]] = column[Option[String]]("data", O.Length(1000,varying=true), O.Default(None))
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column post_id SqlType(int8), Default(None) */
    val postId: Rep[Option[Long]] = column[Option[Long]]("post_id", O.Default(None))
    /** Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
    val postingDate: Rep[Option[String]] = column[Option[String]]("posting_date", O.Length(16,varying=true), O.Default(None))

    /** Foreign key referencing Post (database name comments_post_id_fkey) */
    lazy val postFk = foreignKey("comments_post_id_fkey", postId, Post)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name comments_user_id_fkey) */
    lazy val usersFk = foreignKey("comments_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Comments */
  lazy val Comments = new TableQuery(tag => new Comments(tag))
}
