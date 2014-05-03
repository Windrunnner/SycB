package GreatDouBaba;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GDB_DTB_Low_Interface extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "_info.db";

	private static final int DATABASE_VERSION = 1;

	private static final String CREATE_History_TABLE = "CREATE TABLE HISTORY ( "
			+ "Id INTEGER primary key, "
			+ "Date TEXT, "
			+ "Url TEXT, "
			+ "Title TEXT, " + "Pageid INTEGER );";

	private static final String[] History_columns = { "Id", "Date", "Url",
			"Title", "Pageid" };

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
			+ "Screenshot TEXT, " + "Ico TEXT, " + "Title TEXT, "
			+ "Content TEXT);";

	private static final String CREATE_WORD_TABLE = "CREATE TABLE WORD ("
			+ "Id INTEGER, " + "Word TEXT);";

	
	private static final String[] Word_colums = { "Id", "Word" };

	private static final String CREATE_CONTENT_TABLE = "CREATE TABLE CONTENT ("
			+ "Id INTEGER, " + "Word TEXT);";

	
	private static final String[] CONTENT_colums = { "Id", "Word" };

	private static final String[] View_colums = { "Id", "Url", "Count",
			"Screenshot", "Ico", "Title", "Content TEXT" };

	
	
	private static final String CREATE_CURRENT_TABLE = "CREATE TABLE CURRENT ( "
			+ "Url TEXT, " + "Id INTEGER );";

	
	private static final String[] Current_colums = { "Url", "Id" };

	private static final String CREATE_PASSWORD_TABLE = "CREATE TABLE PASSWORD ( "
			+ "Url TEXT, "
			+ "Account_lab TEXT, "
			+ "Password_lab TEXT, "
			+ "Account TEXT, " + "Password TEXT);";

	
	
	private static final String[] Password_colums = { "Url", "Account_lab",
			"Password_lab", "Account", "Password" };

	public GDB_DTB_Low_Interface(Context context, String CurrentUser) {
		super(context, (CurrentUser + DATABASE_NAME), null, DATABASE_VERSION);
		Log.d("GDB_DTB_Low_Interface Create End", "test");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_History_TABLE);
		db.execSQL(CREATE_BOOKMARK_TABLE);
		db.execSQL(CREATE_BACKGROUND_TABLE);
		db.execSQL(CREATE_VIEW_TABLE);
		db.execSQL(CREATE_CURRENT_TABLE);
		db.execSQL(CREATE_PASSWORD_TABLE);
		db.execSQL(CREATE_WORD_TABLE);
		db.execSQL(CREATE_CONTENT_TABLE);
		Log.d("GDB_DTB_Low_Interface Init End", "test");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS History");
		db.execSQL("DROP TABLE IF EXISTS Bookmark");
		db.execSQL("DROP TABLE IF EXISTS Background");
		db.execSQL("DROP TABLE IF EXISTS View");
		db.execSQL("DROP TABLE IF EXISTS Current");
		db.execSQL("DROP TABLE IF EXISTS Password");
		db.execSQL("DROP TABLE IF EXISTS Word");
		db.execSQL("DROP TABLE IF EXISTS Content");
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
		cursor.close();

		Log.d("Adding History End", "test");
		return save;
	}

	public void addBookmark(String Url, String Title) {
		Log.d("Adding Bookmark Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM BOOKMARK WHERE Url = ?",
				new String[] { Url });
		if (cursor.getCount() != 0) {
			cursor.close();
			return;
		}
		ContentValues values = new ContentValues();
		values.put("Url", Url);
		values.put("Title", Title);
		db.insert("BOOKMARK", null, values);
		cursor.close();
		Log.d("Adding Bookmark End", "test");
	}

	public void addView(String Url, String Screenshot, String Ico,
			String Title, String Content) {
		Log.d("Adding View Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT Count FROM VIEW WHERE Url = ?",
				new String[] { Url });
		ContentValues values = new ContentValues();
		if (cursor == null || cursor.getCount() == 0) {
			Log.d("Adding View New", "test");
			values.put("Url", Url);
			values.put("Count", 1);
			values.put("Screenshot", Screenshot);
			values.put("Ico", Ico);
			values.put("Title", Title);
			values.put("Content", Content);
			db.insert("VIEW", null, values);
		} else {
			Log.d("Adding View Old", "test");
			cursor.moveToFirst();
			int LastCount = Integer.parseInt(cursor.getString(0));
			values.put("Count", (LastCount + 1));
			db.update("VIEW", values, "Url = ?", new String[] { Url });
		}
		cursor.close();

		Log.d("Adding View End", "test");
	}

	public void addCurrent(String Url, int Id) {
		Log.d("Adding Current Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM CURRENT WHERE Id = ?",
				new String[] { String.valueOf(Id) });
		ContentValues values = new ContentValues();
		values.put("Url", Url);
		if (cursor == null || cursor.getCount() == 0) {
			values.put("Id", Id);
			db.insert("CURRENT", null, values);
		} else {
			db.update("CURRENT", values, "Id = ?",
					new String[] { String.valueOf(Id) });
		}
		cursor.close();

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

		Log.d("Adding Password End", "test");
	}
	
	public String[][] getHistory() {
		Log.d("Get History Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT Date, Url, Title, Id FROM HISTORY order by Date DESC",
				null);
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();

			return null;
		}
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[][] rev = new String[count][4];
		for (int i = 0; i < count; i++) {
			rev[i][0] = cursor.getString(0);
			rev[i][1] = cursor.getString(1);
			rev[i][2] = cursor.getString(2);
			rev[i][3] = cursor.getString(3);
			cursor.moveToNext();
		}
		cursor.close();

		return rev;
	}

	public String[][] getBookmark() {
		Log.d("Get Bookmark Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery("SELECT Url, Title, Id FROM Bookmark", null);
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();

			return null;
		}
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[][] rev = new String[count][3];
		for (int i = 0; i < count; i++) {
			rev[i][0] = cursor.getString(0);
			rev[i][1] = cursor.getString(1);
			rev[i][2] = cursor.getString(2);
			cursor.moveToNext();
		}
		cursor.close();

		return rev;
	}

	public String[] getCurrent() {
		Log.d("Get Current Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT Url FROM CURRENT", null);
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[] rev = new String[count];
		for (int i = 0; i < count; i++) {
			rev[i] = cursor.getString(0);
			cursor.moveToNext();
		}
		cursor.close();

		return rev;
	}

	public String[][] getView(int Count) {
		Log.d("Get View Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db
				.rawQuery(
						("SELECT Count, Url, Title FROM VIEW order by Count DESC LIMIT " + Count),
						null);
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();

			return null;
		}
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[][] rev = new String[count][3];
		for (int i = 0; i < count; i++) {
			rev[i][0] = cursor.getString(0);
			rev[i][1] = cursor.getString(1);
			rev[i][2] = cursor.getString(2);
			cursor.moveToNext();
		}
		cursor.close();

		return rev;
	}

	public String[] getUrlInfo(String Url) {
		Log.d("Get UrlInfo Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(
				"SELECT Count, Screenshot, Ico, Title FROM VIEW WHERE Url = ?",
				new String[] { Url });
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();

			return null;
		}
		cursor.moveToFirst();
		String[] rev = new String[4];
		rev[0] = cursor.getString(0);
		rev[1] = cursor.getString(1);
		rev[2] = cursor.getString(2);
		rev[3] = cursor.getString(3);
		cursor.close();
		return rev;
	}

	public String[] getWordByTitleHasCase(String Word) {
		return getWord(Word, true, true);
	}

	public String[] getWordByContentHasCase(String Word) {
		return getWord(Word, false, true);
	}
	public String[] getWordByTitleNoCase(String Word) {
		return getWord(Word, true, false);
	}

	public String[] getWordByContentNoCase(String Word) {
		return getWord(Word, false, false);
	}

	private String[] getWord(String Word, boolean ByTitle, boolean HasCase) {
		Log.d("Get Word Start", "test");
		String[] Words = Word.split(" ");
		String[] Ids = getWordIds(Words[0], ByTitle, HasCase);
		if(Ids == null)
			return null;
		for (int i = 1; i < Words.length; i++) {
			String[] newIds = getWordIds(Words[i], ByTitle, HasCase);
			if(Ids==null || newIds==null)
				return null;
			List<String> list1 = new ArrayList<String>(Arrays.asList(Ids));
			List<String> list2 = new ArrayList<String>(Arrays.asList(newIds));
			list1.retainAll(list2);
			Ids = list1.toArray(new String[0]);
		}
		return getWordWithIds(Ids);
	}

	private String[] getWordWithIds(String[] Ids) {
		SQLiteDatabase db = this.getWritableDatabase();
		String allUrls = "SELECT Url FROM VIEW WHERE Id = ? ";
		for (int i = 1; i < Ids.length; i++)
			allUrls += "OR Id = ? ";
		Cursor cursor = db.rawQuery(allUrls, Ids);
		cursor.moveToFirst();
		int count = cursor.getCount();
		if (count == 0)
			return null;
		String[] rev = new String[count];
		for (int i = 0; i < count; i++) {
			rev[i] = cursor.getString(0);
			cursor.moveToNext();
		}
		return rev;
	}

	private String[] getWordIds(String Word, boolean ByTitle, boolean HasCase) {
		Log.d("Get Word Id Start", Word);
		SQLiteDatabase db = this.getWritableDatabase();
		String excutStr;
		if (ByTitle)
			excutStr = "SELECT Id FROM WORD WHERE Word = ?";
		else
			excutStr = "SELECT Id FROM CONTENT WHERE Word = ?";
		if(!HasCase)
			excutStr += " COLLATE NOCASE";
		Cursor cursor = db.rawQuery(excutStr, new String[] { Word });
		if (cursor.getCount() == 0){
			cursor.close();
			return null;
		}
		cursor.moveToFirst();
		int count = cursor.getCount();
		String[] Ids = new String[count];
		for (int i = 0; i < count; i++) {
			Ids[i] = cursor.getString(0);
			cursor.moveToNext();
		}
		cursor.close();
		return Ids;
	}

	public void addWordToContent(String[] Words, String Url) {
		Log.d("Adding Word Start", "test");
		if (Words == null || Url == null)
			return;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM VIEW WHERE Url = ?",
				new String[] { Url });
		if (cursor.getCount() == 0) {
			Log.d("Adding Word Failed", "test");
			cursor.close();
			return;
		}
		cursor.moveToFirst();
		String Id = cursor.getString(0);
		cursor = db.rawQuery("SELECT * FROM CONTENT WHERE Id = ?",
				new String[] { Id });
		if (cursor.getCount() == 0) {
			String query = "INSERT INTO CONTENT VALUES";
			for (int i = 0, j; i < Words.length && i<300; i++) {
				if(Words[i].length()>30)
					continue;
				for (j = 0; j < i; j++)
					if (Words[j].equals(Words[i])) {
						j = -1;
						break;
					}
				if (j == -1)
					continue;
				if(Words[i].equals(""))
					continue;
				//ContentValues values = new ContentValues();
				//values.put("Id", Id);
				//values.put("Word", Words[i]);
				//db.insert("CONTENT", null, values);
				query += " (" +(Id) + ",'" + Words[i]  + "'),";
				Log.d("insert CONTENT", Words[i]);
			}
			db.execSQL(query.substring(0, query.length() - 1));
			
		} else
			Log.d("Adding Word Exsit", "test");
		cursor.close();
		Log.d("Adding Word End", "test");
	}

	public void addWordToTitle(String[] Words, String Url) {
		Log.d("Adding Word Start", "test");
		if (Words == null || Url == null)
			return;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM VIEW WHERE Url = ?",
				new String[] { Url });
		if (cursor.getCount() == 0) {
			Log.d("Adding Word Failed", "test");
			cursor.close();
			return;
		}
		cursor.moveToFirst();
		String Id = cursor.getString(0);
		cursor = db.rawQuery("SELECT * FROM WORD WHERE Id = ?",
				new String[] { Id });
		if (cursor.getCount() == 0) {
			String query = "INSERT INTO WORD VALUES";
			for (int i = 0, j; i < Words.length; i++) {
				for (j = 0; j < i; j++)
					if (Words[j].equals(Words[i])) {
						j = -1;
						break;
					}
				if (j == -1)
					continue;
				//ContentValues values = new ContentValues();
				//values.put("Id", Id);
				//values.put("Word", Words[i]);
				//db.insert("WORD", null, values);
				query += " (" +(Id) + ",'" + Words[i]  + "'),";
				Log.d("insert Word", Words[i]);
			}
			db.execSQL(query.substring(0, query.length() - 1));
		} else
			Log.d("Adding Word Exsit", "test");
		cursor.close();
		Log.d("Adding Word End", "test");
	}

	public void deleteHistory(String Id) {
		Log.d("Delete History Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT Url FROM HISTORY WHERE Id = ?",
				new String[] { Id });
		cursor.moveToFirst();
		if (cursor == null || cursor.getCount() == 0) {
			cursor.close();
			return;
		}
		String Url = cursor.getString(0);
		cursor = db.rawQuery("SELECT Count FROM VIEW WHERE Url = ?",
				new String[] { Url });
		ContentValues values = new ContentValues();
		cursor.moveToFirst();
		int LastCount = Integer.parseInt(cursor.getString(0));
		values.put("Count", (LastCount - 1));
		db.update("VIEW", values, "Url = ?", new String[] { Url });
		db.delete("HISTORY", "Id = ?", new String[] { Id });
		cursor.close();
		Log.d("Delete History End", "test");
	}

	public void deleteBookmark(String Id) {
		Log.d("Delete Bookmark Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("BOOKMARK", "Id = ?", new String[] { Id });
		Log.d("Delete Bookmark End", "test");
	}

	public void deleteCurrent(String Id) {
		Log.d("Delete Current Start", "test");
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("CURRENT", "Id = ?", new String[] { Id });
		Log.d("Delete Current End", "test");
	}

	public String[] getHistoryColumns(){
		return History_columns;
	}
	
	public String[] getBookmarkColumns(){
		return Bookmark_colums;
	}
	
	public String[] getBackGroundColumns(){
		return BackGround_colums;
	}
	
	public String[] getWordColumns(){
		return Word_colums;
	}
	
	public String[] getContentColumns(){
		return CONTENT_colums;
	}
	
	public String[] getViewColumns(){
		return View_colums;
	}
	
	public String[] getCurrentColumns(){
		return Current_colums;
	}
	
	public String[] getPasswordColumns(){
		return Password_colums;
	}
}

/*
 * public String[] getWordByTitle(String Word){ Log.d("Get Word Start", "test");
 * SQLiteDatabase db = this.getWritableDatabase(); Cursor cursor = db .rawQuery(
 * ("SELECT Url FROM VIEW order by Count DESC WHERE Title REGEXP '"+ Word +"'"),
 * null); if (cursor == null || cursor.getCount() == 0){ cursor.close();
 * 
 * return null; } cursor.moveToFirst(); int count = cursor.getCount(); String[]
 * rev = new String[count]; for (int i = 0; i < count; i++) { rev[i] =
 * cursor.getString(0); cursor.moveToNext(); } cursor.close(); return rev; }
 * 
 * public String[] getWordByContent(String Word){ Log.d("Get Word Start",
 * "test"); SQLiteDatabase db = this.getWritableDatabase(); Cursor cursor = db
 * .rawQuery(
 * ("SELECT Url FROM VIEW order by Count DESC WHERE Content REGEXP '"+ Word
 * +"'"), null); if (cursor == null || cursor.getCount() == 0){ cursor.close();
 * return null; } cursor.moveToFirst(); int count = cursor.getCount(); String[]
 * rev = new String[count]; for (int i = 0; i < count; i++) { rev[i] =
 * cursor.getString(0); cursor.moveToNext(); } cursor.close(); return rev; }
 */

