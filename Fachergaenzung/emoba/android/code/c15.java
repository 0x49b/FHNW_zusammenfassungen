package ch.fhnw.edu.mad;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;

public class HelloWorldContentProvider extends ContentProvider {
	public static final String AUTHORITY = "ch.fhnw.edu.mad.helloworld";
	private static final String CONTENT_AUTHORITY = "content://" + AUTHORITY;
	private static final Uri CONTENT_WORDS_URI = Uri.parse(CONTENT_AUTHORITY + "/words");
	private static final int Words = 1;
	private static final int Word_ID = 2;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static {
		sURIMatcher.addURI(AUTHORITY, "words", Words);
		sURIMatcher.addURI(AUTHORITY, "words/#", Word_ID);
	}	

	private DBHelper dbHelper;	

	@Override
	public String getType(Uri uri) {
		int match = sURIMatcher.match(uri);
		switch (match) {
		case Words:
			// Using Android's vendor-specific MIME format for multiple rows
			return "vnd.android.cursor.dir/vnd.ch.fhnw.edu.mad.helloworld.word";
		case Word_ID:
			// Using Android's vendor-specific MIME format for single row
			return "vnd.android.cursor.item/vnd.ch.fhnw.edu.mad.helloworld.word";
		default:
			return null;
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int match = sURIMatcher.match(uri);
		switch (match) {
		case Words:
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			long newId = db.insert(DBHelper.DATABASE_TABLE_NAME,null,values);
			Uri rowUri = ContentUris.withAppendedId(CONTENT_WORDS_URI, newId);
			dbHelper.close();
			return rowUri;
		}
		return null;
	}
	
	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "INSERT INTO " + DBHelper.DATABASE_TABLE_NAME + " VALUES (?,?);";
		SQLiteStatement statement = db.compileStatement(sql);
		db.beginTransaction();
		for (int i = 0; i < 100; i++) {
			statement.clearBindings();
			statement.bindLong(1, values[i].getAsLong("Id"));
			statement.bindString(2, values[i].getAsString("HelloString"));
			statement.execute();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		dbHelper.close();
		return 100;
		
//		return super.bulkInsert(uri, values);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new DBHelper(this.getContext());
		return true;
	}


	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int nr = db.delete(DBHelper.DATABASE_TABLE_NAME, null, null);
		dbHelper.close();
		return nr;
	}	

}
