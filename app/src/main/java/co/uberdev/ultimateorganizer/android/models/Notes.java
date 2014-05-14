package co.uberdev.ultimateorganizer.android.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import co.uberdev.ultimateorganizer.core.CoreAttachment;
import co.uberdev.ultimateorganizer.core.CoreDataRules;
import co.uberdev.ultimateorganizer.core.CoreNotes;
import co.uberdev.ultimateorganizer.core.CoreSelectable;

/**
 * Created by begum on 24/04/14.
 */
public class Notes extends CoreNotes implements CoreSelectable
{
    private transient SQLiteDatabase db;

    public Notes(SQLiteDatabase db)
    {
        this.db = db;
    }

    public Notes()
    {
        this(null);
    }

    public SQLiteDatabase getDb()
    {
        return db;
    }

    public void setDb(SQLiteDatabase db)
    {
        this.db = db;
    }

    @Override
	public String getTableName() {
		return CoreDataRules.tables.notes;
	}

	@Override
	public boolean loadFromDb(String sqlCriteria, String[] params, int limit) {
        try
        {
            if(db != null)
            {
                String sqlString = "SELECT * FROM "+CoreDataRules.tables.notes;
                if(params.length > 0)
                    sqlString +=  " WHERE "+sqlCriteria;

                Cursor loader = db.rawQuery(sqlString,	params);

                if(!loader.moveToFirst())
                    return true; // no rows in the db, still valid

                while(!loader.isAfterLast())
                {
                    Note note = new Note();
                    note.setLocalId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.notes.localId))); // local id = sqlite id
                    note.setId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.notes.id))); // task id = server id
                    note.setOwnerId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.notes.ownerId)));
                    note.setContent(loader.getString(loader.getColumnIndex(CoreDataRules.columns.notes.content)));
                    note.setDateCreated(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.notes.dateCreated)));
                    note.setLastModified(loader.getInt(loader.getColumnIndex(CoreDataRules.columns.notes.lastModified)));

                    CoreAttachment attachment = CoreAttachment.fromJson(loader.getString(loader.getColumnIndex(CoreDataRules.columns.notes.attachment)), CoreAttachment.class);
                    note.setAttachment(attachment);
                    note.setRelatedTaskId(loader.getLong(loader.getColumnIndex(CoreDataRules.columns.notes.relatedTaskID)));

                    this.add(note);

                    loader.moveToNext();
                }

                loader.close();
                return true;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return false;
	}

	@Override
	public boolean loadFromDb(int limit) {
		return false;
	}

	@Override
	public boolean loadFromDb() {
		return false;
	}
}
