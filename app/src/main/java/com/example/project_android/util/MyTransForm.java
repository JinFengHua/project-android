package com.example.project_android.util;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

import com.blankj.utilcode.util.ImageUtils;
import com.squareup.picasso.Transformation;

import static android.graphics.Bitmap.createBitmap;

public class MyTransForm {

    public static class RoundCornerTransForm implements Transformation {
        private float radius;

        public RoundCornerTransForm(float radius) {
            this.radius = radius;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            return ImageUtils.toRoundCorner(source,radius,true);
        }

        @Override
        public String key() {
            return "right";
        }
    }

//    截取中间部分
    public static class SquareTransForm implements Transformation {
        private float size;

        public SquareTransForm(float size) {
            this.size = size;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            float width = source.getWidth();
            float height = source.getHeight();
            float scaleWidth,scaleHeight,x,y;
            Bitmap nBitmap;
            Bitmap cBitmap = null;
            Matrix matrix = new Matrix();

            if ((width/height)<= 1){
                scaleWidth = size / width;
                scaleHeight = scaleWidth;
                y = ((height*scaleHeight - size) / 2)/scaleHeight;
                x = 0;
            }else {
                scaleWidth = size / height;
                scaleHeight = scaleWidth;
                x = ((width*scaleWidth -size ) / 2)/scaleWidth;
                y = 0;
            }
            matrix.postScale(scaleWidth / 1f, scaleHeight / 1f);
            try {
                if (width - x > 0 && height - y > 0&&source!=null)
                    cBitmap = createBitmap(source, (int) Math.abs(x), (int) Math.abs(y), (int) Math.abs(width - x),
                            (int) Math.abs(height - y), matrix, false);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("SOURCE-->","x:"+source.getWidth()+" y:"+source.getHeight());
                return source;
            }

            width = cBitmap.getWidth();
            height = cBitmap.getHeight();
            if (!(width == height)){
                float nx,ny;
                if (width > height){
                    size = height;
                    nx = (width - size) / 2;
                    ny = 0;
                } else {
                    size = width;
                    ny = (height - size) / 2;
                    nx = 0;
                }

                nBitmap = createBitmap(cBitmap,(int) Math.abs(nx),(int) Math.abs(ny),(int) Math.abs(size),(int) Math.abs(size));
            } else {
                Log.d("CENTER-->","x:"+cBitmap.getWidth()+" y:"+cBitmap.getHeight());
                return cBitmap;
            }
            source.recycle();
            Log.d("NEW-->","x:"+nBitmap.getWidth()+" y:"+nBitmap.getHeight());
            return nBitmap;
        }

        @Override
        public String key() {
            return "null";
        }
    }
}
