package com.example.sycb;

import GreatDouBaba.Bookmarkhtml;
import GreatDouBaba.GDB_DTB_High_Interface;
import GreatDouBaba.Historyhtml;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.*;
import android.view.View.OnTouchListener;
import android.view.animation.*;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.graphics.Bitmap;


public class WebActivity extends Activity {

	private WebView webView;
	private GestureDetector mGestureDetector;
	private ImageView img;
	private EditText addField;
	private Button goButton;
	public static GDB_DTB_High_Interface GDHI;
	private boolean longPress = false;
	private float longPressX, longPressY;
	private int choosenItem = 0;
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		System.out.println("Start");
		setContentView(R.layout.webcontent);

		webView = (WebView) findViewById(R.id.webView);
		addField  = (EditText) findViewById(R.id.editText1);
		goButton = (Button) findViewById(R.id.button1);
		webView.getSettings().setJavaScriptEnabled(true);

		webView.loadUrl("http://www.google.com");
		addField.setText("http://www.google.com");

		GDHI = new GDB_DTB_High_Interface(this);
		addField.setOnEditorActionListener(new OnEditorActionListener(){

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_GO
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
					webView.loadUrl(addField.getText().toString());
				return false;
			}
			
		});
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon){
				addField.setText(url);
				addField.setVisibility(View.VISIBLE);
		    	goButton.setVisibility(View.VISIBLE);
		    	if(!(webView.getUrl().indexOf("data:text/html")==0))
		    		GDHI.viewPage(webView);
				super.onPageStarted(view, url, favicon);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				System.out.println(url);
		    	addField.setVisibility(View.INVISIBLE);
		    	goButton.setVisibility(View.INVISIBLE);
				WebActivity.GDHI.setAction("viewPage");
				WebActivity.GDHI.setWebview(view);
				Thread viewP = new Thread(WebActivity.GDHI);
				viewP.start();

				/*
				 * String[][] test = WebActivity.GDHI.getHistory();
				 * if(test!=null) for(int i=0; i<test.length; i++){
				 * System.out.println(test[i][0]+ "-"+ test[i][1]+ "-"+
				 * test[i][2]); } else
				 * System.out.println("SHITTTTTTTTTTTTTTTTTTT"); String[] test2
				 * = WebActivity.GDHI.getUrlInfo(test[0][1]);
				 * System.out.println(test2[0]+ "\n"+ test2[1]+ "\n"+ test2[2]);
				 */
			}
		});
		
		img = (ImageView) findViewById(R.id.imageView1);
		mGestureDetector = new GestureDetector(this, new MyOnGestureListener());
		webView.setOnTouchListener(new OnTouchListener() {
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        Log.i(getClass().getName(), "onTouch-----" + getActionName(event.getAction()));
		        if (event.getAction() == MotionEvent.ACTION_MOVE && longPress){
		        	float offX = event.getX() - longPressX;
		        	float offY = event.getY() - longPressY;
		        	float offD = (float) (Math.atan2(offY, offX)/2/Math.PI*360f);
		        	Log.i("Move - atan2", Double.toString(Math.atan2(offY, offX)/2/Math.PI*360f));
		        	if(Math.sqrt(
		        			(offX * offX)+
		        			(offY * offY)) > 50){
		        		if (offD < 45 && offD > -90) {
		        			img.setImageDrawable(getResources().getDrawable(R.drawable.rmenu));
		        			choosenItem = 1;
		        		}
		        		else if (offD < -90 || offD > 135) {
		        			img.setImageDrawable(getResources().getDrawable(R.drawable.lmenu));
		        			choosenItem = 2;
		        		}
		        		else if (offD < 135 && offD > 45){
		        			img.setImageDrawable(getResources().getDrawable(R.drawable.dmenu));
		        			choosenItem = 3;
		        		}
		        	}else {
		        		img.setImageDrawable(getResources().getDrawable(R.drawable.normalmenu));
		        		choosenItem = 0;
		        	}
		        }
		        if (event.getAction() == MotionEvent.ACTION_UP){
		        	if (longPress){
		        		img.setVisibility(View.INVISIBLE);
		        	
		        		longPress = false;
		        		switch (choosenItem) {
		        			case 1://Bookmark
		        				webView.loadData(Bookmarkhtml.Generatehtml(GDHI), "text/html", "UTF-8");
		        				Log.d("LPmenu", "Bookmarkhtml.Generatehtml");
		        				break;
		        			case 2://History
		        				webView.loadData(Historyhtml.Generatehtml(GDHI), "text/html", "UTF-8");
		        				Log.d("LPmenu", "Historyhtml.Generatehtml");
		        				break;
		        			case 3://add
		        				GDHI.addBookmark(webView);
		        				Log.d("LPmenu", "GDHI.addBookmark");
		        				break;
		        		}
		        	}
		        }
		        if (!longPress)
		        	return mGestureDetector.onTouchEvent(event);
		        else 
		        	return true;
		    }
		});
		// GDHI.viewPage(webView);
		System.out.println("End");

		// String customHtml =
		// "<html><body><h2>Greetings from JavaCodeGeeks</h2></body></html>";
		// webView.loadData(customHtml, "text/html", "UTF-8");
		Toast.makeText(this,
					"---->      Swipe from the left edge to GO BACK\n"+
					"Swipe from the right edge to GO FORWARD  <----\n"+
					"       Long press for the Function Menu         ",
					Toast.LENGTH_LONG).show();

	}
	public void goURL(View view){
		webView.loadUrl(addField.getText().toString());
	}
	public void showToast(String string){
		Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
	}
	
	class MyOnGestureListener extends SimpleOnGestureListener {
		
		

		@Override
		public void onLongPress(MotionEvent e) {
		    Log.i(getClass().getName(), "onLongPress-----" + getActionName(e.getAction()));
		    //Set image visible
		    longPress = true;
		    img.setVisibility(View.VISIBLE);
		    longPressX = e.getX();
		    longPressY = e.getY();
		    img.setX(e.getX()-150);
		    img.setY(e.getY()-150);
		    Animation scaleAnimation = new ScaleAnimation(0.1f, 1, 0.1f, 1, e.getX(), e.getY());
		    scaleAnimation.setDuration(200);
		    img.startAnimation(scaleAnimation);
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
		    Log.i(getClass().getName(), "onSingleTapUp-----" + getActionName(e.getAction()));
		    return false;
		}


		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		    Log.i(getClass().getName(),
		            "onScroll-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
		                    + e2.getX() + "," + e2.getY() + ")");
		    return false;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		    Log.i(getClass().getName(),
		            "onFling-----" + getActionName(e2.getAction()) + ",(" + e1.getX() + "," + e1.getY() + ") ,("
		                    + e2.getX() + "," + e2.getY() + ")");
		    if (e1.getX() < 50 && e2.getX() > 150 && Math.abs(e2.getY() - e1.getY()) < Math.abs(e2.getX() - e1.getX())){
		    	webView.goBack();
		    	showToast("Go Back");
		    }
		    if (e1.getX() >550 && e2.getX() < 450 && Math.abs(e2.getY() - e1.getY()) < Math.abs(e2.getX() - e1.getX())){
		    	webView.goForward();
		    	showToast("Go Forward");
		    }
		    
		    if(e1.getY() - e2.getY() < -200){
		    	addField.setVisibility(View.VISIBLE);
		    	goButton.setVisibility(View.VISIBLE);
		    }
		    if(e1.getY() - e2.getY() > 200){
		    	addField.setVisibility(View.INVISIBLE);
		    	goButton.setVisibility(View.INVISIBLE);
		    }
		    return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		    Log.i(getClass().getName(), "onShowPress-----" + getActionName(e.getAction()));
		}

		@Override
		public boolean onDown(MotionEvent e) {
		    Log.i(getClass().getName(), "onDown-----" + getActionName(e.getAction()));
		    return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
		    Log.i(getClass().getName(), "onDoubleTap-----" + getActionName(e.getAction()));
		    return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
		    Log.i(getClass().getName(), "onDoubleTapEvent-----" + getActionName(e.getAction()));
		    return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
		    Log.i(getClass().getName(), "onSingleTapConfirmed-----" + getActionName(e.getAction()));
		    return false;
		}
	}
	
	private String getActionName(int action) {
	    String name = "";
	    switch (action) {
	        case MotionEvent.ACTION_DOWN: {
	            name = "ACTION_DOWN";
	            break;
	        }
	        case MotionEvent.ACTION_MOVE: {
	            name = "ACTION_MOVE";
	            break;
	        }
	        case MotionEvent.ACTION_UP: {
	            name = "ACTION_UP";
	            break;
	        }
	        default:
	        break;
	    }
	    return name;
	}

}


