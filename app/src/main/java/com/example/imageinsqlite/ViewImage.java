package com.example.imageinsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ViewImage extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView=findViewById(R.id.imageView);

        Bundle bundle=getIntent().getExtras();
        int getid=bundle.getInt("Id");
         SQLIteData sqlIteData=new SQLIteData(ViewImage.this);
         sqlIteData.search(getid);
        Cursor res=sqlIteData.search(getid);
        if(res.moveToFirst())
        {
            do{
                byte[] bytes=res.getBlob(1);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
              }while(res.moveToNext());
        }


    }
}