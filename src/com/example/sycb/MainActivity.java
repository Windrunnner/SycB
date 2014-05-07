package com.example.sycb;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private Button button;

	public void onCreate(Bundle savedInstanceState) {
		final Context context = this;

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.buttonUrl);

		button.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View arg0) {
			EditText IPText = (EditText) (findViewById(R.id.IPText));
			WebActivity.hostIP = IPText.getText().toString();
		    Intent intent = new Intent(context, WebActivity.class);
		    startActivity(intent);
		  }

		});
	}
}



















/*
import GreatDouBaba.GDB_DTB_High_Interface;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
 
public class MainActivity extends Activity {
	private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        //MySQLiteHelper db = new MySQLiteHelper(this);
        System.out.println("Start");
        *//**
         * CRUD Operations
         * *//*
        // add Books
        db.addBook(new Book("Android Application Development Cookbook", "Wei Meng Lee"));   
        db.addBook(new Book("Android Programming: The Big Nerd Ranch Guide", "Bill Phillips and Brian Hardy"));       
        db.addBook(new Book("Learn Android App Development", "Wallace Jackson"));
        System.out.println("1234567890");
        
        // get all books
        List<Book> list = db.getAllBooks();
        
        GDB_DTB_High_Interface GDHI = new GDB_DTB_High_Interface(this);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("http://www.google.com");
        System.out.println("End");
        
        // delete one book
      //  db.deleteBook(list.get(0));
 
        // get all books
      //  db.getAllBooks();
 
    }
 
}*/