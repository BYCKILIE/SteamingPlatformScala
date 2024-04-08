package models
// AUTO-GENERATED Slick data model for table LiveStream
trait LiveStreamTable {

  self:TablesRoot with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table LiveStream
   *  @param id Database column id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param title Database column title SqlType(varchar), Length(100,true), Default(None)
   *  @param description Database column description SqlType(varchar), Length(1000,true), Default(None)
   *  @param data Database column data SqlType(bytea), Default(None)
   *  @param views Database column views SqlType(int4), Default(Some(0))
   *  @param likes Database column likes SqlType(int4), Default(Some(0))
   *  @param dislikes Database column dislikes SqlType(int4), Default(Some(0))
   *  @param userId Database column user_id SqlType(int8)
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
  case class LiveStreamRow(id: Long, title: Option[String] = None, description: Option[String] = None, data: Option[Array[Byte]] = None, views: Option[Int] = Some(0), likes: Option[Int] = Some(0), dislikes: Option[Int] = Some(0), userId: Long, postingDate: Option[String] = None)
  /** GetResult implicit for fetching LiveStreamRow objects using plain SQL queries */
  implicit def GetResultLiveStreamRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Array[Byte]]], e3: GR[Option[Int]]): GR[LiveStreamRow] = GR{
    prs => import prs._
    LiveStreamRow.tupled((<<[Long], <<?[String], <<?[String], <<?[Array[Byte]], <<?[Int], <<?[Int], <<?[Int], <<[Long], <<?[String]))
  }
  /** Table description of table live_stream. Objects of this class serve as prototypes for rows in queries. */
  class LiveStream(_tableTag: Tag) extends profile.api.Table[LiveStreamRow](_tableTag, "live_stream") {
    def * = (id, title, description, data, views, likes, dislikes, userId, postingDate).<>(LiveStreamRow.tupled, LiveStreamRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(id), title, description, data, views, likes, dislikes, Rep.Some(userId), postingDate)).shaped.<>({r=>import r._; _1.map(_=> LiveStreamRow.tupled((_1.get, _2, _3, _4, _5, _6, _7, _8.get, _9)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(bigserial), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column title SqlType(varchar), Length(100,true), Default(None) */
    val title: Rep[Option[String]] = column[Option[String]]("title", O.Length(100,varying=true), O.Default(None))
    /** Database column description SqlType(varchar), Length(1000,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(1000,varying=true), O.Default(None))
    /** Database column data SqlType(bytea), Default(None) */
    val data: Rep[Option[Array[Byte]]] = column[Option[Array[Byte]]]("data", O.Default(None))
    /** Database column views SqlType(int4), Default(Some(0)) */
    val views: Rep[Option[Int]] = column[Option[Int]]("views", O.Default(Some(0)))
    /** Database column likes SqlType(int4), Default(Some(0)) */
    val likes: Rep[Option[Int]] = column[Option[Int]]("likes", O.Default(Some(0)))
    /** Database column dislikes SqlType(int4), Default(Some(0)) */
    val dislikes: Rep[Option[Int]] = column[Option[Int]]("dislikes", O.Default(Some(0)))
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
    val postingDate: Rep[Option[String]] = column[Option[String]]("posting_date", O.Length(16,varying=true), O.Default(None))

    /** Foreign key referencing Users (database name live_stream_user_id_fkey) */
    lazy val usersFk = foreignKey("live_stream_user_id_fkey", userId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table LiveStream */
  lazy val LiveStream = new TableQuery(tag => new LiveStream(tag))
}
