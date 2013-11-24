package edu.virginia.cs2110.ghosthunter;

import java.io.InputStream;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

public class GifMovieView extends View {

	private Movie movie;
	private long movieStart;
	private int gifId;
	
	public GifMovieView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setAttrs(attrs);
		init();
	}

	public GifMovieView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAttrs(attrs);
		init();
	}

	public GifMovieView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		if (gifId != 0) {
	        InputStream is = getContext().getResources().openRawResource(gifId);
	        movie = Movie.decodeStream(is);
	        movieStart = 0;
	        this.invalidate();
	    }
	}
	
	public void setGIFResource(int resId) {
	    this.gifId = resId;
	    init();
	}

	public int getGIFResource() {
	    return this.gifId;
	}
	
	private void setAttrs(AttributeSet attrs) {
		if (attrs != null) {
	        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.edu_virginia_cs2110_ghosthunter_GifMovieView, 0, 0);
	        String gifSource = a.getString(R.styleable.edu_virginia_cs2110_ghosthunter_GifMovieView_src);
	        String sourceName = Uri.parse(gifSource).getLastPathSegment().replace(".gif", "");
	        setGIFResource(getResources().getIdentifier(sourceName, "drawable", getContext().getPackageName()));
	        a.recycle();
	    }
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.TRANSPARENT);
		super.onDraw(canvas);
		
		final long now = SystemClock.uptimeMillis();
		
		if (movieStart == 0) {
			movieStart = now;
		}
		if (movie != null) {
			final int relTime = (int)((now - movieStart) % movie.duration());
		    movie.setTime(relTime);
		    movie.draw(canvas, movie.width(), movie.height());
		    this.invalidate();
		}
	}

}
