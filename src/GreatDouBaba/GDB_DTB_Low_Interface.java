package GreatDouBaba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GDB_DTB_Low_Interface extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "_info.db";

	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_History_TABLE = "CREATE TABLE HISTORY ( "
			+ "Id INTEGER primary key, "
			+ "Date TEXT, "
			+ "Url TEXT, "
			+ "Title TEXT, " + "Pageid INTEGER );";

	private static final String[] History_columns = { "Id", "Date", "Url",
			"Title", "Pageid" };

	private static final String CREATE_WORD_TABLE = "CREATE TABLE WORD ("
			+ "Id INTEGER, " + "Word TEXT);";

	private static final String[] Word_colums = { "Id", "Word" };

	private static final String CREATE_BOOKMARK_TABLE = "CREATE TABLE BOOKMARK ("
			+ "Id INTEGER primary key, " + "Url TEXT, " + "Title TEXT);";

	private static final String[] Bookmark_colums = { "Id", "Url", "Title", };

	private static final String CREATE_BACKGROUND_TABLE = "CREATE TABLE BACKGROUND ( "
			+ "Id INTEGER primary key, "
			+ "X_cor integer, "
			+ "Y_cor integer, "
			+ "X_size integer, "
			+ "Y_size integer, "
			+ "Commend TEXT, " + "Type TEXT, " + "Ico TEXT );";

	private static final String[] BackGround_colums = { "X_cor", "Y_cor",
			"X_size", "Y_size", "Comment", "Type", "Ico" };

	private static final String CREATE_VIEW_TABLE = "CREATE TABLE VIEW ( "
			+ "Id INTEGER primary key, " + "Url TEXT, " + "Count INTEGER, "
			+ "Screenshot TEXT, " + "Ico TEXT );";

	private static final String[] View_colums = { "Id", "Url", "Count",
			"Screenshot", "Ico" };

	private static final String CREATE_CURRENT_TABLE = "CREATE TABLE CURRENT ( "
			+ "Url TEXT, " + "Ico TEXT, " + "Id INTEGER );";

	private static final String[] Current_colums = { "Url", "Ico", "Id" };

	private static final String CREATE_PASSWORD_TABLE = "CREATE TABLE PASSWORD ( "
			+ "Url TEXT, "
			+ "Account_lab TEXT, "
			+ "Password_lab TEXT, "
			+ "Account TEXT, " + "Password TEXT);";

	private static final String[] Password_colums = { "Url", "Account_lab",
			"Password_lab", "Account", "Password" };

	public GDB_DTB_Low_Interface(Context context, String CurrentUser) {
		super(context, (CurrentUser + DATABASE_NAME), null, DATABASE_VERSION);
		DATABASE_NAME = CurrentUser + DATABASE_NAME;
		Log.d("GDB_DTB_Low_Interface Create End", "test");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_History_TABLE);
		db.execSQL(CREATE_WORD_TABLE);
		db.execSQL(CREATE_BOOKMARK_TABLE);
		db.execSQL(CREATE_BACKGROUND_TABLE);
		db.execSQL(CREATE_VIEW_TABLE);
		db.execSQL(CREATE_CURRENT_TABLE);
		db.execSQL(CREATE_PASSWORD_TABLE);
		Log.d("GDB_DTB_Low_Interface Init End", "test");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS History");
		db.execSQL("DROP TABLE IF EXISTS Word");
		db.execSQL("DROP TABLE IF EXISTS Bookmark");
		db.execSQL("DROP TABLE IF EXISTS Background");
		db.execSQL("DROP TABLE IF EXISTS View");
		db.execSQL("DROP TABLE IF EXISTS Current");
		db.execSQL("DROP TABLE IF EXISTS Password");
		this.onCreate(db);
	}

	public boolean addHistroy(String Date, String Url, String Title,
			String Pageid) {
		Log.d("Adding History Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT * FROM History WHERE Url = ? AND Pageid = ?",
				new String[] { Url, Pageid });
		ContentValues values = new ContentValues();
		values.put("Date", Date);
		values.put("Url", Url);
		values.put("Title", Title);
		values.put("Pageid", Pageid);
		boolean save = cursor.getCount() == 0;
		if (save)
			db.insert("HISTORY", null, values);
		else
			db.update("HISTORY", values, "Url = ? AND Pageid = ?",
					new String[] { Url, Pageid });
		db.close();
		Log.d("Adding History End", "test");
		return save;
	}

	public void addWord(String[] Words, String Url) {
		Log.d("Adding Word Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM VIEW WHERE Url = ?",
				new String[] { Url });
		if (cursor.getCount() == 0) {
			Log.d("Adding Word Failed", "test");
			return;
		}
		cursor.moveToFirst();
		int Id = Integer.parseInt(cursor.getString(0));
		int Count = Integer.parseInt(cursor.getString(2));
		if (Count == 1) {
			for (int i = 0; i < Words.length; i++) {
				ContentValues values = new ContentValues();
				values.put("Id", Id);
				values.put("Word", Words[i]);
				db.insert("WORD", null, values);
				Log.d("insert Word", Words[i]);
			}
			db.close();
		} else
			Log.d("Adding Word Exsit", "test");
		Log.d("Adding Word End", "test");
	}

	public void addBookmark(String Url, String Title) {
		Log.d("Adding Bookmark Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Url", Url);
		values.put("Title", Title);
		db.insert("BOOKMARK", null, values);
		db.close();
		Log.d("Adding Bookmark End", "test");
	}

	public void addView(String Url, String Screenshot, String Ico) {
		Log.d("Adding View Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM VIEW WHERE Url = ?",
				new String[] { Url });
		ContentValues values = new ContentValues();
		if (cursor.getCount() == 0) {
			Log.d("Adding View New", "test");
			values.put("Url", Url);
			values.put("Count", 1);
			values.put("Screenshot", Screenshot);
			values.put("Ico", Ico);
			db.insert("VIEW", null, values);
		} else {
			Log.d("Adding View Old", "test");
			cursor.moveToFirst();
			int LastCount = Integer.parseInt(cursor.getString(2));
			values.put("Count", (LastCount + 1));
			db.update("VIEW", values, "Url = ?", new String[] { Url });
		}
		db.close();
		Log.d("Adding View End", "test");
	}

	public void addCurrent(String Url, int Id) {
		Log.d("Adding Current Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM CURRENT WHERE Id = ?",
				new String[] { String.valueOf(Id) });
		ContentValues values = new ContentValues();
		values.put("Url", Url);
		if (cursor.getCount() == 0) {
			values.put("Id", Id);
			db.insert("CURRENT", null, values);
		} else {
			db.update("CURRENT", values, "Id = ?",
					new String[] { String.valueOf(Id) });
		}
		db.close();
		Log.d("Adding Current End", "test");
	}

	public void addPassword(String Url, String Account_lab,
			String Password_lab, String Account, String Password) {
		Log.d("Adding Password Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("Url", Url);
		values.put("Account_lab", Account_lab);
		values.put("Password_lab", Password_lab);
		values.put("Account", Account);
		values.put("Password", Password);
		db.insert("PASSWORD", null, values);
		db.close();
		Log.d("Adding Password End", "test");
	}

	public void deleteCurrent(int Id) {
		Log.d("Delete Current Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("CURRENT", "Id = ?", new String[] { String.valueOf(Id) });
		db.close();
		Log.d("Delete Current End", "test");
	}

	public String[][] getHistory() {
		Log.d("Get History Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT Date, Url, Title FROM HISTORY order by Date DESC",
						null);
		if(cursor.getCount()==0)
			return null;
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[][] rev = new String[count][3];
		for(int i=0; i<count; i++){
			rev[i][0] = cursor.getString(0);
			rev[i][1] = cursor.getString(1);
			rev[i][2] = cursor.getString(2);
			cursor.moveToNext();
		}
		return rev;
	}

	public String[] getUrlInfo(String Url){
		Log.d("Get UrlInfo Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						"SELECT Count, Screenshot, Ico FROM VIEW WHERE Url = ?",
						new String[]{ Url });
		if(cursor.getCount()==0)
			return null;
		cursor.moveToFirst();
		String[] rev = new String[3];
		rev[0] = cursor.getString(0);
		rev[1] = cursor.getString(1);
		rev[2] = cursor.getString(2);
		return rev;
	}
}
