package com.example.imageinsqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.BitSet;

public class MainActivity extends AppCompatActivity {
    Button camera_btn,gallary_btn,load_from_app_btn;
    EditText id_et;
    ImageView image_iv,search;
    public static final int MY_CAMERA_PERMISSION_CODE=100;
    public static final int MY_CAMERA_REQUEST_CODE=102;

    public static final int MY_STORAGE_PERMISSION_CODE=200;
    public static final int MY_STORAGE_REQUEST_CODE=202;
    Bitmap bitmap;
    int get_id_From_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camera_btn=findViewById(R.id.camera);
        gallary_btn=findViewById(R.id.gallary);
        id_et=findViewById(R.id.id);
        image_iv=findViewById(R.id.img);
        search=findViewById(R.id.search);
        load_from_app_btn=findViewById(R.id.internal);

        load_from_app_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((get_id_From_user%2)==0)
                {
                    bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.photo2);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] img = byteArrayOutputStream.toByteArray();
                    SQLIteData db=new SQLIteData(MainActivity.this);

                    boolean insert = db.insert(img);
                    if (insert == true) {
                        Toast.makeText(MainActivity.this, "Image saved Sucessfully", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(MainActivity.this, "Image Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.photo);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] img = byteArrayOutputStream.toByteArray();
                    SQLIteData db=new SQLIteData(MainActivity.this);

                    boolean insert = db.insert(img);
                    if (insert == true) {
                        Toast.makeText(MainActivity.this, "Image saved Sucessfully", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        Toast.makeText(MainActivity.this, "Image Error", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int get_id=get_id_From_user;
               Intent intent=new Intent(MainActivity.this,ViewImage.class);
               intent.putExtra("Id",get_id);
               startActivity(intent);

            }
        });

        camera_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else
                {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);


                }
            }
        });

        gallary_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_PERMISSION_CODE);
                }
                else
                {
                    Intent gallartintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(gallartintent,MY_STORAGE_REQUEST_CODE);

                }

            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_id_From_user= Integer.parseInt(id_et.getText().toString());
                SQLIteData db=new SQLIteData(MainActivity.this);
                Cursor res=db.search(get_id_From_user);
                if(res.moveToFirst())
                {
                    do{
                        byte[] bytes=res.getBlob(1);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image_iv.setImageBitmap(bitmap);
                    }while(res.moveToNext());
                }
                else {
                    Toast.makeText(MainActivity.this, "Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(intent);
                 } else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            } break;
            case MY_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent,MY_STORAGE_PERMISSION_CODE);
                } else {
                    Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if(requestCode==MY_CAMERA_REQUEST_CODE && resultCode== Activity.RESULT_OK)
            {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                byte[] img = byteArrayOutputStream.toByteArray();

                SQLIteData sqlIteData=new SQLIteData(MainActivity.this);
                sqlIteData.insert(img);
                Toast.makeText(MainActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();
            }

        if(requestCode==MY_STORAGE_REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (selectedImage != null) {
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    Bitmap bv=(BitmapFactory.decodeFile(picturePath));
                    cursor.close();

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bv.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
                    byte[] img1 = byteArrayOutputStream.toByteArray();
                    SQLIteData sqlIteData=new SQLIteData(MainActivity.this);
                    sqlIteData.insert(img1);
                    Toast.makeText(MainActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();

                }
            }
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
//            byte[] img = byteArrayOutputStream.toByteArray();
//
//            SQLIteData sqlIteData=new SQLIteData(MainActivity.this);
//            sqlIteData.insert(img);
//            Toast.makeText(MainActivity.this, "Photo saved", Toast.LENGTH_SHORT).show();
        }
    }
}