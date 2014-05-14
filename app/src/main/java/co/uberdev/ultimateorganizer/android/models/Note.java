package co.uberdev.ultimateorganizer.android.models;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreNote;
import co.uberdev.ultimateorganizer.core.CoreStorable;

/**
 * Created by begum on 24/04/14.
 */
public class Note extends CoreNote implements CoreStorable
{
    private transient SQLiteDatabase db;

    public Note(SQLiteDatabase db) {
        super();
        this.db = db;
        this.content = "";
		this.status = 0;
		this.localId = -1;
    }

    public Note()
    {
        this(null);
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    public void setDb(SQLiteDatabase db) {
        this.db = db;
    }

	@Override
	public String getTableName() {
		return CoreDataRules.tables.notes;
	}

	// Inserts the information about the note into the table
    @Override
	public boolean insert() {
		if(db != null) {
            try {
                String insertSql = "INSERT INTO " + getTableName() + " (" +
                    CoreDataRules.columns.notes.id + ", " +
                    CoreDataRules.columns.notes.ownerId + ", " +
                    CoreDataRules.columns.notes.content + ", " +
                    CoreDataRules.columns.notes.dateCreated + ", " +
                    CoreDataRules.columns.notes.lastModified + ", " +
                    CoreDataRules.columns.notes.attachment + ", " +
                    CoreDataRules.columns.notes.relatedTaskID + " " +
                    ") VALUES (?,?,?,?,?,?,?)";


                int n = 1;
                SQLiteStatement ss = db.compileStatement(insertSql);
                ss.bindLong(n++, getId());
                ss.bindLong(n++, getOwnerId());
                ss.bindString(n++, getContent());
                ss.bindLong(n++, getDateCreated());
                ss.bindLong(n++, getLastModified());
                ss.bindString(n++, getAttachment() != null ? getAttachment().asJsonString() : "");
                ss.bindLong(n++, getRelatedTaskId());

                long localId = ss.executeInsert();

				setLocalId(localId);

                ss.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return false;
	}

    // Updates the note's information in the database
	@Override
	public boolean update() {
        if(db != null)
        {
            try
            {
                int n = 1;
                String updateSql = "UPDATE " + CoreDataRules.tables.notes + " SET " +
                    CoreDataRules.columns.notes.localId + " = ?, " +
                    CoreDataRules.columns.notes.id + " = ?, " +
                    CoreDataRules.columns.notes.ownerId + " = ? , " +
                    CoreDataRules.columns.notes.content + " = ? , " +
                    CoreDataRules.columns.notes.dateCreated + " = ? , " +
                    CoreDataRules.columns.notes.lastModified + " = ? , " +
                    CoreDataRules.columns.notes.attachment + " = ? , " +
                    CoreDataRules.columns.notes.relatedTaskID + " = ?  " +
                    " WHERE " + CoreDataRules.columns.notes.localId + " = ?";

                SQLiteStatement ss = db.compileStatement(updateSql);
                ss.bindLong(n++, getLocalId());
                ss.bindLong(n++, getId());
                ss.bindLong(n++, getOwnerId());
                ss.bindString(n++, getContent());
                ss.bindLong(n++, getDateCreated());
                ss.bindLong(n++, getLastModified());
                ss.bindString(n++, getAttachment() != null ? getAttachment().asJsonString() : "");
                ss.bindLong(n++, getRelatedTaskId());

                ss.bindLong(n++, getLocalId());

                ss.execute();
                ss.close();

                return true;
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }

        return false;
	}

	@Override
	public boolean remove() {
        if(db != null && localId > 0)
        {
            String removeSql = "DELETE FROM "+CoreDataRules.tables.notes+" WHERE id = "+localId;
            // TODO: complete this.
        }
        return false;
	}
}
