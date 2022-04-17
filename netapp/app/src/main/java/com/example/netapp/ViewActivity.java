package com.example.netapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ViewActivity extends AppCompatActivity {
    EditText et_name,et_spec,et_price,et_image;
    TextView tv_tags,tv_category;
    Button btn_save,btn_cancel;
    ImageView imgProduct;
    Product p;
    public boolean[] selTags;
    Uri selimg;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        Toolbar myChildToolbar =(Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myChildToolbar);
        //myChildToolbar.setTitle("选择商品");
        p=(Product) getIntent().getSerializableExtra("product");


        imgProduct=(ImageView)findViewById(R.id.imageView);
        et_name=(EditText) findViewById(R.id.etName);
        et_spec=(EditText) findViewById(R.id.et_spec);
        et_price=(EditText) findViewById(R.id.et_price);
        et_image=(EditText) findViewById(R.id.etImage);
        tv_tags=(TextView)findViewById(R.id.tvTags);
        tv_category=(TextView)findViewById(R.id.tvCategory);
        btn_cancel=(Button)findViewById(R.id.btnCancel);
        btn_save=(Button)findViewById(R.id.btnSave);

        et_name.setText(p.name);
        et_spec.setText(p.spec);
        et_price.setText(p.price==null?"0":p.price);
        /*
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeimage(view);
            }
        });
        */

        int size=MainActivity.clist.size();
        String[] categoryArray= new String[size];
        for(int i=0;i<size;i++) {
            categoryArray[i]=MainActivity.clist.get(i).sname;
        }
        selTags=new boolean[MainActivity.tlist.size()];

        if(p.id!=null) {
            myChildToolbar.setTitle("编辑商品");
            Picasso.get().load(MainActivity.SERVER+"thumbnail/"+p.images).placeholder(R.drawable.ic_launcher).into(imgProduct);
            et_image.setText(p.images);
            tv_category.setText(findCategoryName(p.cid,MainActivity.clist));
            tv_tags.setText(getTagName(p.tags));
        }
        /*
        String[] stringArray= MainActivity.clist.toArray(new String[size]);
        for (String s : stringArray)
            Log.d("View",s);
*/
        tv_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(ViewActivity.this)
                        .setTitle("请选择分类")
                        .setItems(categoryArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Category c=MainActivity.clist.get(which);
                                tv_category.setText(c.sname);
                                p.cid=c.id;
                                et_name.setText(et_name.getText().toString()+c.sname);
                            }
                        }).create();
                /*
                        .setSingleChoiceItems(categoryArray, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 保存当前选中的列表项索引

                    }
                }).create();*/

                dialog.show();

            }
        });


        tv_tags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(ViewActivity.this)
                        .setTitle("请选择标签")
                        .setMultiChoiceItems(MainActivity.tagArray, selTags,
                                new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked){


                            }
                        })           .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                int size=MainActivity.tlist.size();
                                String str="";
                                p.tags="";
                                for(int j=0;j<size;j++)
                                if(selTags[j]) {
                                    p.tags+=MainActivity.tlist.get(j).id+",";
                                    str+=MainActivity.tlist.get(j).name+",";
                                }
                                et_name.setText(str.replace(",","")+et_name.getText().toString());
                                tv_tags.setText(str.substring(0,str.length()-1));
                                p.tags=p.tags.substring(0,p.tags.length()-1);

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        }).create();

                dialog.show();

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Log.d("view", "p.id:" + p.id);
                    Log.d("view", "p.name:" +et_name.getText().toString());
                    Log.d("view", "p.cid:" + p.cid);
                    Log.d("view", "p.images:" + et_image.getText().toString());
                    Log.d("view", "p.tags:" + p.tags);
                    Log.d("view", "p.spec:" + et_spec.getText().toString());
                    Log.d("view", "p.price:" + et_price.getText().toString());
                    savetodatabase();

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void savetodatabase() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody formBody;

                    if(currentPhotoPath!=null) {
                        File file = new File(currentPhotoPath);

                        formBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("file", file.getName(),
                                        RequestBody.create(MediaType.parse("text/plain"), file))
                                .addFormDataPart("name", et_name.getText().toString())
                                .addFormDataPart("cid", p.cid)
                                .addFormDataPart("imgstr", et_image.getText().toString())
                                .addFormDataPart("tags", p.tags)
                                .addFormDataPart("spec", et_spec.getText().toString())
                                .addFormDataPart("price", et_price.getText().toString())
                                .build();
                    }
                    else {
                        formBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("name", et_name.getText().toString())
                                .addFormDataPart("cid", p.cid)
                                .addFormDataPart("imgstr", et_image.getText().toString())
                                .addFormDataPart("tags", p.tags)
                                .addFormDataPart("spec", et_spec.getText().toString())
                                .addFormDataPart("price", et_price.getText().toString())
                                .build();
                    }
                    Request request;
                    if(p.id==null)
                    request = new Request.Builder()
                            .header("Authorization", Credentials.basic("sb", "songbin"))
                            .url(MainActivity.SERVER+"admin/product.php").post(formBody).build();
                    else
                    request = new Request.Builder()
                            .header("Authorization", Credentials.basic("sb", "songbin"))
                            .url(MainActivity.SERVER+"admin/product.php?id="+p.id).post(formBody).build();

                    Response response = client.newCall(request).execute();

                    showResponse(response.code()+response.message());
                    Log.d("view",request.toString());
                    Log.d("view",request.body().toString());
                    Log.d("view",response.toString());

                    Log.d("view",response.body().toString());


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("view",e.toString());
                }
            }
        }).start();
    }
    private void showResponse(final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 在这里进行UI操作，将结果显示到界面上
                Toast.makeText(ViewActivity.this, response, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("save", true);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    public String findCategoryName(String id, List<Category> categorys) {
        String str="";
        for (Category category : categorys)
            if (category.id.equals(id)) {
                str=category.sname;
                break;
            }
        return str;
    }



    public String getTagName(String tagstr) {
        String str="";
        String[] tags=p.tags.split(",");
        int j,l=tags.length,size=MainActivity.tlist.size();

        for(int i=0;i<l;i++)
            for(j=0;j<size;j++) {
                Tag t=MainActivity.tlist.get(j);
                if(t.id.equals(tags[i])) {
                    str+=t.name+",";
                    selTags[j]=true;
                    Log.d("view","tags["+i+"]="+tags[i]);
                    Log.d("view","t.id="+t.id+",t.name="+t.name);
                    Log.d("view","selTags["+j+"]="+selTags[j]);
                    break;
                }
            }
        Log.d("view",str);
            return str.substring(0,str.length()-1);
    }

    public void  loadimage(View view) {

        imageChooser();

    }
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GET_PHOTO = 0;
    public void  takeimage(View view) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            //File photoFile = null;
                selimg=null;
                selimg= createImageUri();

            // Continue only if the File was successfully created
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,selimg);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PICTURE);

        }

    }
    String currentPhotoPath;

    private String createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "p" + timeStamp + ".jpg";

        return imageFileName;
    }

    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_SELECT_PICTURE);
    }

    // this function is triggered when user
    // selects the image from the imageChooser
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            /*
            if (requestCode == SELECT_PICTURE) {
                // Get the url of the image from data
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    // update the preview image in the layout

                    img.setImageURI(selectedImageUri);
                }

            }


            switch(requestCode) {
                case REQUEST_TAKE_PHOTO:
                    //Uri selectedImage = data.getData();
                    //img.setImageURI(selectedImage);
                    selimg=data.getData();
                    imgProduct.setImageURI(selimg);

                    //et_image.setText(file.getName());
                    break;
                case REQUEST_GET_PHOTO:
                    selimg = data.getData();
                    imgProduct.setImageURI(selimg);
                    String path=selimg.getPath();
                    et_image.setText(path.substring(path.lastIndexOf("/")+1));

                    break;
            }
        }
    }
*/
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void upload(String url, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("prod", "0")
                .addFormDataPart("name", et_name.getText().toString())
                .addFormDataPart("category", p.cid)
                .addFormDataPart("imgstr", et_image.getText().toString())
                .addFormDataPart("spec", et_spec.getText().toString())
                .addFormDataPart("price", et_price.getText().toString())
                .build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Response response = client.newCall(request).execute();
        Log.d("view",request.toString());
        Log.d("view",response.toString());
    }

    public final int REQUEST_SELECT_PICTURE = 0x01;
    public final int REQUEST_CODE_TAKE_PICTURE = 0x2;
    public static String TEMP_PHOTO_FILE_NAME ="photo_";
    Uri mImageCaptureUri;
    File mFileTemp;

    public void initTempFile(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            mFileTemp = new File(Environment.getExternalStorageDirectory() + File.separator
                    + getResources().getString(R.string.app_foldername) + File.separator
                    + getResources().getString(R.string.pictures_folder)
                    , TEMP_PHOTO_FILE_NAME
                    + System.currentTimeMillis() + ".jpg");
            mFileTemp.getParentFile().mkdirs();
        } else {
            mFileTemp = new File(getFilesDir() + File.separator
                    + getResources().getString(R.string.app_foldername)
                    + File.separator + getResources().getString(R.string.pictures_folder)
                    , TEMP_PHOTO_FILE_NAME + System.currentTimeMillis() + ".jpg");
            mFileTemp.getParentFile().mkdirs();
        }
    }
    public void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            mImageCaptureUri = null;
            mImageCaptureUri = Uri.fromFile(mFileTemp);


            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
            intent.putExtra("return-data", true);
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        Bitmap bitmap;
        currentPhotoPath=common.getDownloadDir()+createImageFile();
        switch (requestCode) {

            case REQUEST_SELECT_PICTURE:
                try {
                    Uri uri = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                        Drawable drawable=new BitmapDrawable(bitmapScaled);

                        saveJPGE_After(bitmapScaled,currentPhotoPath);
                        imgProduct.setImageDrawable(drawable);
                        //mImage.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        Log.v("act result", "there is an error : ");
                    }
                } catch (Exception e) {
                    Log.v("act result", "there is an error : ");
                }
                break;
            case REQUEST_CODE_TAKE_PICTURE:
                try{
                    Bitmap bitmappicture = MediaStore.Images.Media.getBitmap(getContentResolver() , selimg);
                    //currentPhotoPath=common.getDownloadDir()+createImageFile();
                    saveJPGE_After(bitmappicture,currentPhotoPath);
                    imgProduct.setImageBitmap(bitmappicture);
                    //mImage.setVisibility(View.VISIBLE);
                }catch (IOException e){
                    Log.v("error camera",e.getMessage());
                }
                break;
        }
        et_image.setText(currentPhotoPath.substring(currentPhotoPath.lastIndexOf("/")+1));
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void saveJPGE_After(Bitmap bitmap, String path) {
        File file = new File(path);
        try {/*from w  w  w.  j a v  a 2 s  .c  om*/
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}