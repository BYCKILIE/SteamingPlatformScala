package models
// AUTO-GENERATED Slick data model for table Messages
trait MessagesTable {

  self:TablesRoot with LiveStreamTable with UsersTable with UsersTable  =>

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}
  /** Entity class storing rows of table Messages
   *  @param data Database column data SqlType(varchar), Length(1000,true), Default(None)
   *  @param senderId Database column sender_id SqlType(int8)
   *  @param receiverId Database column receiver_id SqlType(int8), Default(None)
   *  @param liveStreamId Database column live_stream_id SqlType(int8), Default(None)
   *  @param postingDate Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
  case class MessagesRow(data: Option[String] = None, senderId: Long, receiverId: Option[Long] = None, liveStreamId: Option[Long] = None, postingDate: Option[String] = None)
  /** GetResult implicit for fetching MessagesRow objects using plain SQL queries */
  implicit def GetResultMessagesRow(implicit e0: GR[Option[String]], e1: GR[Long], e2: GR[Option[Long]]): GR[MessagesRow] = GR{
    prs => import prs._
    MessagesRow.tupled((<<?[String], <<[Long], <<?[Long], <<?[Long], <<?[String]))
  }
  /** Table description of table messages. Objects of this class serve as prototypes for rows in queries. */
  class Messages(_tableTag: Tag) extends profile.api.Table[MessagesRow](_tableTag, "messages") {
    def * = (data, senderId, receiverId, liveStreamId, postingDate).<>(MessagesRow.tupled, MessagesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((data, Rep.Some(senderId), receiverId, liveStreamId, postingDate)).shaped.<>({r=>import r._; _2.map(_=> MessagesRow.tupled((_1, _2.get, _3, _4, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column data SqlType(varchar), Length(1000,true), Default(None) */
    val data: Rep[Option[String]] = column[Option[String]]("data", O.Length(1000,varying=true), O.Default(None))
    /** Database column sender_id SqlType(int8) */
    val senderId: Rep[Long] = column[Long]("sender_id")
    /** Database column receiver_id SqlType(int8), Default(None) */
    val receiverId: Rep[Option[Long]] = column[Option[Long]]("receiver_id", O.Default(None))
    /** Database column live_stream_id SqlType(int8), Default(None) */
    val liveStreamId: Rep[Option[Long]] = column[Option[Long]]("live_stream_id", O.Default(None))
    /** Database column posting_date SqlType(varchar), Length(16,true), Default(None) */
    val postingDate: Rep[Option[String]] = column[Option[String]]("posting_date", O.Length(16,varying=true), O.Default(None))

    /** Foreign key referencing LiveStream (database name messages_live_stream_id_fkey) */
    lazy val liveStreamFk = foreignKey("messages_live_stream_id_fkey", liveStreamId, LiveStream)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name messages_receiver_id_fkey) */
    lazy val usersFk2 = foreignKey("messages_receiver_id_fkey", receiverId, Users)(r => Rep.Some(r.id), onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name messages_sender_id_fkey) */
    lazy val usersFk3 = foreignKey("messages_sender_id_fkey", senderId, Users)(r => r.id, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Messages */
  lazy val Messages = new TableQuery(tag => new Messages(tag))
}
