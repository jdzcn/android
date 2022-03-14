package com.example.store;




import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;

import java.text.SimpleDateFormat;

import java.util.Arrays;
import java.util.Calendar;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SimpleCursorAdapter;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	
	private storeHelper db;
    private SQLiteDatabase database;
    private boolean deleteOk=false;
    private SimpleCursorAdapter adapter;// 简单的游标适配器
    private ListView listview;
    private int itemid=0,vstyle=1;
    private int itemcost=0;
	public EditText ed_number,ed_price,ed_amount,ed_cost,ed_remark;
	public EditText et_name;
	String date;
	final String mx="select _id,date,itemname,number,store.cost as cost,store.price as price,amount,(amount-store.cost) as profit,remark from store,item where store.itemid=item.itemid and ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);   


        getActionBar().setDisplayShowHomeEnabled(false);

        
        db=new storeHelper(this);
        database=db.getReadableDatabase();
        listview=(ListView)findViewById(R.id.listView1);
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date =simpleDateFormat.format(new Date(System.currentTimeMillis()));
        /*
         
    	  actualListView.setOnItemClickListener(new OnItemClickListener() {
 
  			@Override
  			public void onItemClick(AdapterView<?> parent, View view,
  					int position, long id) {
  				MyCollect myCollect = myCollects.get(position - 1);
  				Intent intent = new Intent(MyCollectActivity.this, FindOrderDetailsActivity.class);
  				intent.putExtra("quotation.id", myCollect.id);
  				startActivity(intent);
  			}

         */
        

  		
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           
        	@Override
        	public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
           	if (!deleteOk) return false;
           	final TextView tv_id=(TextView)view.findViewById(R.id.tv_id);
   	
           	final String str="DELETE FROM store where _id="+tv_id.getText().toString();
           	//Toast.makeText(MainActivity.this,"你选择了"+tv.getText().toString(),Toast.LENGTH_LONG).show();
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setIcon(R.drawable.ic_launcher);
               builder.setTitle("警告");
               builder.setMessage("确定要删除这条记录吗?");
               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       //点击确定按钮之后的回调

                    	database.execSQL(str);
                       	Toast.makeText(MainActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                    	
                    	filllist(vstyle);
                    	
                   }
               });

                            
               builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                   }
               });
              
               AlertDialog dialog = builder.create();
               dialog.show();
               return true;
           }
     });
  		
        //ed_date=(EditText)findViewById(R.id.et_date);
        //ed_date.setText(simpleDateFormat.format(date));
        

        et_name=(EditText)findViewById(R.id.et_name);
        /*
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {  

            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
                  
                if(hasFocus){  
                    showitem(); 
                }  
             }  
      	});

		*/
        
        ed_number=(EditText)findViewById(R.id.et_number);

        ed_price=(EditText)findViewById(R.id.et_price);
        ed_amount=(EditText)findViewById(R.id.et_amount);
        ed_cost=(EditText)findViewById(R.id.et_cost);
        ed_remark=(EditText)findViewById(R.id.et_remark);

        ed_number.addTextChangedListener(new TextWatcher() {
        	  @Override
        	  public void onTextChanged(CharSequence s, int start, int before, int count){}
        	  @Override
        	  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        	  @Override
        	  public void afterTextChanged(Editable s) {
              	if(TextUtils.isEmpty(ed_number.getText())||TextUtils.isEmpty(ed_price.getText())) return;
              	int q = Integer.parseInt(ed_number.getText().toString());
              	int p=Integer.parseInt(ed_price.getText().toString());
              	//int c=Integer.parseInt(ed_cost.getText().toString());
              	ed_amount.setText(q*p+"");
              	ed_cost.setText(q*itemcost+"");
        	  }
        	});
		
        ed_price.addTextChangedListener(new TextWatcher() {
      	  @Override
      	  public void onTextChanged(CharSequence s, int start, int before, int count){}
      	  @Override
      	  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
      	  @Override
      	  public void afterTextChanged(Editable s) {
            	if(TextUtils.isEmpty(ed_number.getText())||TextUtils.isEmpty(ed_price.getText())) return;
            	int q = Integer.parseInt(ed_number.getText().toString());
            	int p=Integer.parseInt(ed_price.getText().toString());
            	ed_amount.setText(q*p+"");
      	  }
      	});
         /*
        ed_amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {  

            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
                  
                if(hasFocus){}
                else
                {
                  	if(TextUtils.isEmpty(ed_number.getText())||TextUtils.isEmpty(ed_amount.getText())) return;

                  	int q = Integer.parseInt(ed_number.getText().toString());

                  	int p=Integer.parseInt(ed_amount.getText().toString());

                  	ed_price.setText(Math.ceil(p/q)+"");                	
                }  
             }  
      	});        

        
       
        ed_amount.addTextChangedListener(new TextWatcher() {
        	  @Override
        	  public void onTextChanged(CharSequence s, int start, int before, int count){}
        	  @Override
        	  public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        	  @Override
        	  public void afterTextChanged(Editable s) {
              	if(TextUtils.isEmpty(ed_number.getText())||TextUtils.isEmpty(ed_amount.getText())) return;

              	int q = Integer.parseInt(ed_number.getText().toString());

              	int p=Integer.parseInt(ed_amount.getText().toString());

              	ed_price.setText(Math.ceil(p/q)+"");
        	  }
        	});        

        */
        SharedPreferences sp=getSharedPreferences("data",MODE_PRIVATE);        
        vstyle=sp.getInt("view",1);
        filllist(vstyle);
        //setOverflowShowingAlways(); 
    }
    
    /*
    private void setOverflowShowingAlways() {  
        try {  
            ViewConfiguration config = ViewConfiguration.get(this);  
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");  
            menuKeyField.setAccessible(true);  
            menuKeyField.setBoolean(config, false);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    } 
    */
    @Override
    protected void onPause()
    {
        super.onPause();
    	SharedPreferences.Editor editor = getSharedPreferences("data", 0).edit();
        editor.putInt("view",vstyle);
        editor.commit();
    }
    
   
    public boolean onCreateOptionsMenu(Menu menu) {
        // 这条表示加载菜单文件，第一个参数表示通过那个资源文件来创建菜单
        // 第二个表示将菜单传入那个对象中。这里我们用Menu传入menu
        // 这条语句一般系统帮我们创建好
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.m_date).setTitle(date);
        switch (vstyle)	{
        case 0:
        	menu.findItem(R.id.m_today).setChecked(true);
        	break;
        case 1:
        	menu.findItem(R.id.m_week).setChecked(true);
        	break;

        }

        return true;
    }
    
    public void btn_sum_click(View v)	{
 	   	try	{
    	database.execSQL("create table tmp (_id integer primary key autoincrement,date date,number integer default 0,amount integer default 0,profit integer default 0)");
 	   	database.execSQL("insert into tmp (date,number,amount,profit) select '本日统计',sum(number),sum(amount),sum(amount-cost) from store where date=date('now','localtime')");
 	   	database.execSQL("insert into tmp (date,number,amount,profit) select '本月统计',sum(number),sum(amount),sum(amount-cost) from store where substr(date,1,7)='"+GetThisMonth()+"'");
 	   	database.execSQL("insert into tmp (date,number,amount,profit) select '本年统计',sum(number),sum(amount),sum(amount-cost) from store where strftime('%Y', 'now', 'localtime')=substr(date,1,4) group by substr(date,1,4)");
 	   	Cursor cursor=database.rawQuery("select * from tmp",null);
 	   	adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.listsum, cursor,new String[]{"date","number","amount","profit"}, new int []{R.id.textview1,R.id.textview2,R.id.textview3,R.id.textview4},0);   
 	   	deleteOk=false;
 	   	listview.setAdapter(adapter);
    	database.execSQL("drop table tmp");
 	   	}
 	   	catch(Exception e) {
 	   	}
 	   		
    }

    public void btn_list_click(View v)	{
    	filllist(vstyle);
    }

    public String GetThisMonth()	{

        return date.substring(0,7);
    }
    
    
    public void finddata()	{
        Calendar instance = Calendar.getInstance();
        int year = instance.get(Calendar.YEAR);
        //int month = instance.get(Calendar.MONTH)+1; // 该方法month 从0 开始
        //int day = instance.get(Calendar.DAY_OF_MONTH);
        Resources res =getResources();
        String[] ystr = res.getStringArray(R.array.year);
        //String[] mstr = res.getStringArray(R.array.month);
        
    	AlertDialog.Builder builder3 = new AlertDialog.Builder(this);
	    final View layout1 = getLayoutInflater().inflate(R.layout.finddlg, null);//获取自定义布局
		final Spinner sp=(Spinner)layout1.findViewById(R.id.sp_style);
		//sp.setSelection(3);
		final Spinner sp_year=(Spinner)layout1.findViewById(R.id.sp_year);
		//String ystr[]={"2018","2019","2020","2021","2022"};
		sp_year.setSelection(Arrays.binarySearch(ystr,""+year));
		//Toast.makeText(this,Arrays.binarySearch(ystr,""+year)+"",Toast.LENGTH_SHORT).show();
		final Spinner sp_month=(Spinner)layout1.findViewById(R.id.sp_month);
		//String mstr[]={"全年","1","2","3","4","5","6","7","8","9","10","11","12"};
		//Toast.makeText(this,Arrays.binarySearch(mstr,Integer.toString(month))+"-"+month,Toast.LENGTH_SHORT).show();
		//sp_month.setSelection(Arrays.binarySearch(mstr,""+month));
		//sp_month.setSelection(month);
		final EditText et_remark=(EditText)layout1.findViewById(R.id.et_remark);
	    builder3.setView(layout1);


		builder3.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {

				String sql=null;
				String ystr=sp_year.getSelectedItem().toString();
				String datestr=sp_month.getSelectedItemPosition()==0?ystr:ystr+"-"+sp_month.getSelectedItem().toString();
				int i=sp.getSelectedItemPosition();
				int l=datestr.length();
				if (l==6) {datestr+="-";l+=1;}
				if(!TextUtils.isEmpty(et_remark.getText()))	{
                	i=0;
                	sql=mx+" remark like '%"+et_remark.getText().toString()+"%'" ;
                }
                else	{
                	switch (i) {
                	case 0:
                		sql=mx+" substr(date,1,"+l+")='"+datestr+"' order by _id desc";
                		break;
                	case 1:
                		sql="select _id,date,sum(amount) as amount,sum(number) as number,sum(amount-cost) as profit from store where substr(date,1,"+l+")='"+datestr+"' group by date order by _id desc";
                		break;
                	case 2:
                		sql="select _id, substr(date,1,7) as date,sum(amount) as amount,sum(number) as number,sum(amount-cost) as profit from store where substr(date,1,"+l+")='"+datestr+"' group by substr(date,1,7) order by _id desc";
                		break;
                	case 3:
                		sql="select _id,itemname as date,sum(number) as number,sum(amount) as amount,sum(store.cost) as cost,sum(amount-store.cost) as profit from store,item where store.itemid=item.itemid and substr(date,1,"+l+")='"+datestr+"' group by store.itemid order by profit desc";
                		break;
                	default:
                		break;
                	}
                }

            	try	{

            		Cursor cursor=database.rawQuery(sql, null);
            		

            	    	if (i==0)	{
            	    		adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.listitem, cursor,new String[]{"date","_id","itemname","number","amount","profit","remark","price","cost"}, new int []{R.id.tv_date,R.id.tv_id,R.id.tv_itemname,R.id.tv_number,R.id.tv_amount,R.id.tv_profit,R.id.tv_remark,R.id.tv_price,R.id.tv_cost},0);//该方法在高的版本中已经过时，因为在如果读取数据库时间过长系统会报错，新的版本中将数据库读取操作放在另外的线程中。
            	    		deleteOk=true;
            	    	}
            	    	else	{
            	     	   adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.listsum, cursor,new String[]{"date","number","amount","profit"}, new int []{R.id.textview1,R.id.textview2,R.id.textview3,R.id.textview4},0);   
            	    	   deleteOk=false;
            	    	}
            	    	   listview.setAdapter(adapter);
            	    	   Toast.makeText(MainActivity.this, cursor.getCount()+"条记录", Toast.LENGTH_SHORT).show();
            		
            	}
            	catch (Exception e)	{
            		
            	};
                
				//Toast.makeText(MainActivity.this,et_remark.getText().toString()+sp.getSelectedItemPosition(),Toast.LENGTH_SHORT).show();
      	
			}
		});
		//取消
		builder3.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});

        AlertDialog dialog2 = builder3.create();
        dialog2.show();

    }
    
    
    public void senddatabase() {
    	/*
    	Intent email = new Intent(android.content.Intent.ACTION_SEND);    

    	File file = new File((Environment.getExternalStorageDirectory().getAbsolutePath() + "/store.db"));    
    	//邮件发送类型：带附件的邮件    
    	
    	email.setType("application/octet-stream");    
    	
    	//邮件接收者（数组，可以是多位接收者）    
    	String[] emailReciver = new String[]{"jdzcn@qq.com","jdzcn1@163.com"};    
    	    
    	String  emailTitle = "数据库";    
    	String emailContent = "内容";    
    	//设置邮件地址    
    	email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);    
    	//设置邮件标题    
    	 email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailTitle);    
    	//设置发送的内容    
    	email.putExtra(android.content.Intent.EXTRA_TEXT, emailContent);    
    	//附件    
    	 
    	email.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));    
    	 //调用系统的邮件系统
    	startActivity(Intent.createChooser(email, "请选择")); 
    	*/
    }
    
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        
        case R.id.m_find:
        	finddata();
        	break;
        case R.id.m_date:
        	Calendar instance = Calendar.getInstance();
        	int year = instance.get(Calendar.YEAR);
        	int month = instance.get(Calendar.MONTH); // 该方法month 从0 开始
        	int day = instance.get(Calendar.DAY_OF_MONTH);
        	final MenuItem i=item;
        	DatePickerDialog dialog = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            	String y=(monthOfYear+1)+"";
            	if (y.length()==1) y="0"+y; 
            	date=year+"-"+y+"-"+dayOfMonth;
                // 获取到的month 需要+1 获取正确的月份
                //Toast.makeText(MainActivity.this, d, Toast.LENGTH_SHORT).show();
                
                i.setTitle(date);
            }
        	},year,month,day);
        
        	dialog.show();	
        	
        	break;
        case R.id.m_send:
        	
        	senddatabase();
        	
        	break;
        
        case R.id.m_item:
           
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
    	    final View layout = getLayoutInflater().inflate(R.layout.mydlg, null);//获取自定义布局
    	    
    	    builder2.setView(layout);


    		builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    				EditText et_name=(EditText)layout.findViewById(R.id.et_name);
    				EditText et_price=(EditText)layout.findViewById(R.id.et_price);
    				EditText et_cost=(EditText)layout.findViewById(R.id.et_cost);
    				//Toast.makeText(getApplication(), et_name.getText().toString(), Toast.LENGTH_SHORT).show();
                    if(TextUtils.isEmpty(et_name.getText()))	return;
                	//String[] str=et.getText().toString().split(",");
                	
                	
                	String sql="insert into item (itemname,price,cost) values ('"+et_name.getText().toString()+"',"+(TextUtils.isEmpty(et_price.getText())?"0":et_price.getText().toString())+","+(TextUtils.isEmpty(et_cost.getText())?"0":et_cost.getText().toString())+")";
                	//Toast.makeText(MainActivity.this, insertitemsql, Toast.LENGTH_SHORT).show();
                	try	{
                		database.execSQL(sql);
      
                		Toast.makeText(getApplicationContext(), "添加成功!",Toast.LENGTH_LONG).show();
                	}
                	catch (Exception e)	{
                		
                	};
          	
    			}
    		});
    		//取消
    		builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface arg0, int arg1) {
    				// TODO Auto-generated method stub
    				
    			}
    		});

            AlertDialog dialog4 = builder2.create();
            dialog4.show();
        	

            break;
            
        case R.id.m_today:
        	item.setChecked(true);
        	vstyle=0;
        	filllist(vstyle);
            break; 

        case R.id.m_week:
        	item.setChecked(true);
        	vstyle=1;
        	filllist(vstyle);
            break; 
        

            
      case R.id.m_export:
        	
        	copyfile((Environment.getDataDirectory().getAbsolutePath() + "/data/" + this.getPackageName() + "/databases/store.db"),(Environment.getExternalStorageDirectory().getAbsolutePath() + "/store.db"),true);
        	
        	break;
        case R.id.m_import:
        	
        	copyfile((Environment.getExternalStorageDirectory().getAbsolutePath() + "/tencent/MicroMsg/Download/store.db"),(Environment.getDataDirectory().getAbsolutePath() + "/data/" +getPackageName() + "/databases/store.db"),false);


        	break;
        default:
            break;
        }
        return true;

    } 
    

    public void btn_item_click(View v)	{
    	
    	showitem();
    }

    

    public void showitem()	{

    	Cursor cursor=database.rawQuery("select * from item order by itemid desc",null);
    	int l=cursor.getCount();
    	int i=0;
    	final int[] iid=new int[l];
    	final int[] cost=new int[l];
    	final int[] price=new int[l];
    	final String[] str=new String[l];
    	
    	if (cursor != null && cursor.moveToFirst()) {
            do {
            	iid[i]=cursor.getInt(0);
            	str[i]=cursor.getString(1);
            	price[i]=cursor.getInt(2);
            	cost[i]=cursor.getInt(3);
            	i++;
            } while (cursor.moveToNext());

        }

    	 AlertDialog dialog = new AlertDialog.Builder(this)
         .setItems(str, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

                 itemid=iid[which];
            	 et_name.setText(str[which]);

                 ed_number.setText("1");
                 ed_price.setText(String.valueOf(price[which]));
                 //ed_amount.setText(String.valueOf(price[which]));
                 itemcost=cost[which];
                 ed_cost.setText(String.valueOf(itemcost));
             }
         }).create();

    	 dialog.show();
    
    }
    

    
    public void btn_save_click(View v)	{
    	
    	if (itemid==0||TextUtils.isEmpty(ed_number.getText())||TextUtils.isEmpty(ed_price.getText())||TextUtils.isEmpty(ed_amount.getText())||TextUtils.isEmpty(ed_cost.getText()))	{
    		Toast.makeText(MainActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	//String str_date=ed_date.getText().toString();
    	String str_itemid=String.valueOf(itemid);
    	String str_number=ed_number.getText().toString();
    	
    	
    	String str_amount=ed_amount.getText().toString();
    	//String str_price=Math.floor(Integer.parseInt(str_amount)/Integer.parseInt(str_number))+"";
    	String str_price=ed_price.getText().toString();
    	
    	String str_cost=ed_cost.getText().toString();

    	String str_remark=ed_remark.getText().toString();
    	String sql="insert into store (date,itemid,number,price,amount,cost,remark) values ('"+date+"',"+str_itemid+","+str_number+","+str_price+","+str_amount+","+str_cost+",'"+str_remark+"')";
    	Log.d("MainActivity",sql);
    	
    	database.execSQL(sql);
    	
    	Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();

    	
    	filllist(vstyle);
    	
    	clear();
    }
    
    public void clear()	{

    	et_name.setText("");
    	ed_number.setText("");
    	ed_price.setText("");
    	ed_amount.setText("");
    	ed_cost.setText("");
    	et_name.requestFocus();
    	//ed_remark.setText("");
    	
    }
	
    
    
    public void filllist(int i)	{
    	
    	
    	String[] s={	"date=date('now','localtime')",
    						"date between datetime(date(datetime('now',strftime('-7 day','now'))),'+1 second') and date('now','localtime') ",
    						"substr(date,1,7)='"+GetThisMonth()+"'"};
    	String sqlstr=mx+s[i]+" order by _id desc";
    	Cursor cursor=database.rawQuery(sqlstr, null);
    	

    	adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.listitem, cursor,new String[]{"date","_id","itemname","number","amount","profit","remark","price","cost"}, new int []{R.id.tv_date,R.id.tv_id,R.id.tv_itemname,R.id.tv_number,R.id.tv_amount,R.id.tv_profit,R.id.tv_remark,R.id.tv_price,R.id.tv_cost},0);//该方法在高的版本中已经过时，因为在如果读取数据库时间过长系统会报错，新的版本中将数据库读取操作放在另外的线程中。
    	deleteOk=true;

    	   listview.setAdapter(adapter);
    	   //Toast.makeText(MainActivity.this, cursor.getCount()+"条记录", Toast.LENGTH_SHORT).show();
    }
	
	public void copyfile(final String inputstr,final String outputstr,final boolean issend)	{
    	AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("详情");
        builder.setMessage("源文件:"+inputstr+"\n目标文件:"+outputstr+"\n确定执行操作吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击确定按钮之后的回调
            	File dbFile = new File(inputstr);
            	if(!dbFile.exists())	{
            		Toast.makeText(MainActivity.this, "文件不存在!", Toast.LENGTH_SHORT).show();
            		return;
            	}
            	FileInputStream fis = null;
            	FileOutputStream fos = null;
            	try {
            	    //文件复制到sd卡中
            	    fis = new FileInputStream(dbFile);
            	    
            	    fos = new FileOutputStream(outputstr);
            	    int len = 0;
            	    byte[] buffer = new byte[2048];
            	    while (-1 != (len = fis.read(buffer))) {
            	        fos.write(buffer, 0, len);
            	    }
            	    fos.flush();
            	    
            	    Toast.makeText(MainActivity.this, issend?"备份成功!":"恢复成功!", Toast.LENGTH_SHORT).show();
            	    if (issend) {senddatabase();} 
            	    else {
            	    	final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
            	    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            	    	startActivity(intent);
            	    }
            	} catch (Exception e) {
            	    e.printStackTrace();
            	} finally {
            	    try {
            	        if (fos != null) fos.close();
            	        if (fis != null) fis.close();

            	    } catch (IOException e) {
            	        e.printStackTrace();
            	    }

            	}
            	
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //点击取消按钮之后的回调
            }
        });

        AlertDialog dialog1 = builder.create();
        dialog1.show(); 		
		
	}

}
