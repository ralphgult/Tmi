package tm.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;

public class RKCloudChatTouchImageView extends ImageView {
   // We can be in one of these 3 states
	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	
	private static final int CLICK = 3;
	
	private int mode = NONE;	
	private Matrix matrix;

    // Remember some things for zooming
	private PointF last = new PointF();
	private PointF start = new PointF();
	private float minScale = 1f;
	private float maxScale = 3f;
	private float[] m;
    
    private float mLastMotionX, mLastMotionY;
    private boolean isMoved;  
    private Runnable mLongPressRunnable; // long event
    private static final int TOUCH_SLOP = 20;
    private boolean hasResLongEvent = false;

    private boolean atLifeSide = false, atRightSide = false;

    private int viewWidth, viewHeight;
   
    private float saveScale = 1f;
    private  float origWidth, origHeight;
    private int oldMeasuredWidth, oldMeasuredHeight;
    private ScaleGestureDetector mScaleDetector;

    public RKCloudChatTouchImageView(Context context) {
        super(context);
        sharedConstructing(context);
    }

    public RKCloudChatTouchImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        sharedConstructing(context);
    }
    
    /**
     * 图片放大且不在图片的左右边界时，返回真，否则返回假
     */
    public boolean isScaleAndNotAtSide(){
    	if(saveScale == 1.0f) {
    		return false;
    	} else {
    		if(atLifeSide || atRightSide)
    			return false;
    		else
    			return true;
    	}
    }
    
    private void sharedConstructing(Context context) {
        super.setClickable(true);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        matrix = new Matrix();
        m = new float[9];
        setImageMatrix(matrix);
        setScaleType(ScaleType.MATRIX);
        
        mLongPressRunnable = new Runnable() {
            @Override  
            public void run() {
            	hasResLongEvent = true;
                performLongClick();  
            }  
        }; 

        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	
                mScaleDetector.onTouchEvent(event);
                PointF curr = new PointF(event.getX(), event.getY());

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	last.set(curr);
                        start.set(last);
                        mode = DRAG;
                        atLifeSide = atRightSide = false;
                        
                        // add long click event
	                    mLastMotionX = curr.x;  
	                    mLastMotionY = curr.y;  
	                    isMoved = false;
	                    hasResLongEvent = false;
	                    postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());
                        break;
                        
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            float deltaX = curr.x - last.x;
                            float deltaY = curr.y - last.y;
                            float fixTransX = getFixDragTrans(deltaX, viewWidth, origWidth * saveScale);
                            float fixTransY = getFixDragTrans(deltaY, viewHeight, origHeight * saveScale);
                            matrix.postTranslate(fixTransX, fixTransY);
                            
                            if(saveScale>1.0f) {
                            	matrix.getValues(m);
                                float transX = m[Matrix.MTRANS_X];
                            	if(transX > 0){
                            		atLifeSide = true;
                            	} else {
                            		atLifeSide = false;
                            	}
                            	
                            	if(transX < (viewWidth - origWidth * saveScale)){
                            		atRightSide = true;
                            	} else {
                            		atRightSide = false;
                            	}
                            } else {
                            	atLifeSide = atRightSide = false;
                            }
                            
                            fixTrans();
                            last.set(curr.x, curr.y);
                        }
                        
                        // cancel long click
	                    if(isMoved) break;  
	                    if(Math.abs(mLastMotionX-curr.x)>TOUCH_SLOP || Math.abs(mLastMotionY-curr.y)>TOUCH_SLOP) {  
	                       //移动超过阈值，则表示移动了  
	                    	isMoved = true;
	                    	hasResLongEvent = false;
	                    	removeCallbacks(mLongPressRunnable);  
	                    }
                        break;

                    case MotionEvent.ACTION_UP:
                        mode = NONE;
                        int xDiff = (int) Math.abs(curr.x - start.x);
                        int yDiff = (int) Math.abs(curr.y - start.y);
                        if (xDiff < CLICK && yDiff < CLICK){
                        	if(hasResLongEvent == false) //
                        		performClick();
                        }
                        
                        // cancel long click
                        hasResLongEvent = false;
                        removeCallbacks(mLongPressRunnable);
                        break;

                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        break;
                        
                    case MotionEvent.ACTION_CANCEL:
                    	// cancel long click
                        hasResLongEvent = false;
                        removeCallbacks(mLongPressRunnable);
                    	break;
                }
                
                setImageMatrix(matrix);
                invalidate();
                return true; // indicate event was handled
            }

        });
    }

    public void setMaxZoom(float x) {
        maxScale = x;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mode = ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = saveScale;
            saveScale *= mScaleFactor;
            if (saveScale > maxScale) {
                saveScale = maxScale;
                mScaleFactor = maxScale / origScale;
            } else if (saveScale < minScale) {
                saveScale = minScale;
                mScaleFactor = minScale / origScale;
            }

            if (origWidth * saveScale <= viewWidth || origHeight * saveScale <= viewHeight)
                matrix.postScale(mScaleFactor, mScaleFactor, viewWidth / 2, viewHeight / 2);
            else
                matrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

    void fixTrans() {
        matrix.getValues(m);
        float transX = m[Matrix.MTRANS_X];
        float transY = m[Matrix.MTRANS_Y];
        
        float fixTransX = getFixTrans(transX, viewWidth, origWidth * saveScale);
        float fixTransY = getFixTrans(transY, viewHeight, origHeight * saveScale);

        if (fixTransX != 0 || fixTransY != 0)
            matrix.postTranslate(fixTransX, fixTransY);
    }

    float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }
    
    float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);
        
        //
        // Rescales image on rotation
        //
        if (oldMeasuredHeight == viewWidth && oldMeasuredHeight == viewHeight
                || viewWidth == 0 || viewHeight == 0)
            return;
        oldMeasuredHeight = viewHeight;
        oldMeasuredWidth = viewWidth;

        if (saveScale == 1) {
            //Fit to screen.
            float scale;

            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;
            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();
            
            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            float scaleX = (float) viewWidth / (float) bmWidth;
            float scaleY = (float) viewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            matrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) viewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) viewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;

            matrix.postTranslate(redundantXSpace, redundantYSpace);

            origWidth = viewWidth - 2 * redundantXSpace;
            origHeight = viewHeight - 2 * redundantYSpace;
            setImageMatrix(matrix);
        }
        fixTrans();
    }
}