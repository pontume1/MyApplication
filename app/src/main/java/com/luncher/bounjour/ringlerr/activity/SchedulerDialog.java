package com.luncher.bounjour.ringlerr.activity;

/**
 * Created by santanu on 11/11/17.
 */

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luncher.bounjour.ringlerr.MyDbHelper;
import com.luncher.bounjour.ringlerr.R;
//import com.luncher.bounjour.ringlerr.SessionManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
//import java.util.HashMap;
import java.util.TimeZone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class SchedulerDialog extends AppCompatActivity {
    TextView name;
    TextView phone;
    String phone_no;
    String c_name;
    String message;
    String spinner_type;
    EditText sendMgs;
    Button dialog_save;
    //Button dialog_share;
    ImageView dialog_contact;
    ImageView shceduler_time_image;
    TextView scheduler_person_text;
    TextView scheduler_date_text;
    Button closeButton;
    ImageView scheduler_date_image;
    TextView scheduler_time_text;
    EditText ago_sel;
    ImageView scheduler_message_icon;
    ImageView scheduler_whatsapp_icon;
    ImageView scheduler_facebook_icon;
//    EditText fec_sel;


    int minute;
    int hour;
    int year;
    int month;
    int day;
    Long time;
    Long alarm_time;
    int remindAgo = 0;
    String remUnit;

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    static final int PICK_CONTACT_REQUEST = 1;
    private int PICK_IMAGE_REQUEST = 2;
    private int PERMISSION_REQUEST_SEND_SMS = 3;

    private Switch scheduler_prompt_toggle;
    private Boolean checkBoxState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("test","outgoing custom dialog a");

//        SessionManager session = new SessionManager(this);
//        // get user data from session
//        HashMap<String, String> user = session.getUserDetails();
//        String mPhoneNo = user.get(SessionManager.KEY_PHONE);
//        String mName = user.get(SessionManager.KEY_NAME);

        setContentView(R.layout.activity_scheduler);

        Calendar rnow = Calendar.getInstance(TimeZone.getDefault());
        year = rnow.get(Calendar.YEAR);
        month = rnow.get(Calendar.MONTH);
        day = rnow.get(Calendar.DAY_OF_MONTH);
        hour = rnow.get(Calendar.HOUR);
        minute = rnow.get(Calendar.MINUTE);

        //ImageView profile_image = findViewById(R.id.profile_image);
        scheduler_person_text = findViewById(R.id.scheduler_person_text);
        scheduler_date_image = findViewById(R.id.scheduler_date_image);
        shceduler_time_image = findViewById(R.id.shceduler_time_image);
        scheduler_time_text = findViewById(R.id.scheduler_time_text);
        ago_sel = findViewById(R.id.ago_sel);
        scheduler_date_text = findViewById(R.id.scheduler_date_text);
        closeButton = findViewById(R.id.close_btn_reminder);
        dialog_contact   = findViewById(R.id.scheduler_person_image);
        scheduler_message_icon   = findViewById(R.id.scheduler_message_icon);
        scheduler_whatsapp_icon   = findViewById(R.id.scheduler_whatsapp_icon);
        scheduler_facebook_icon   = findViewById(R.id.scheduler_facebook_icon);
        dialog_save   = findViewById(R.id.save);
        sendMgs = findViewById(R.id.scheduler_message);

        //initiate a check box
        scheduler_prompt_toggle = findViewById(R.id.scheduler_prompt_toggle);

        Calendar calendar = new GregorianCalendar(year,
                month,
                day,
                hour,
                minute);

        time = calendar.getTimeInMillis();
        alarm_time = time;
        scheduler_date_text.setText(formateDates(time));
        scheduler_time_text.setText(formateTimes(time));

        spinner_type = "SMS";

        dialog_contact.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choosePhoneNo();
            }

        });

        scheduler_person_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoneNo();
            }

        });

//        closeButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//
//        });

        scheduler_date_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        scheduler_date_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        shceduler_time_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });

        scheduler_time_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime();
            }
        });
//
//        ago_sel.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("MissingPermission")
//            @Override
//            public void onClick(View v) {
//
//                final View dialogView = View.inflate(SchedulerDialog.this, R.layout.reminder_ago, null);
//                final AlertDialog alertDialog = new AlertDialog.Builder(SchedulerDialog.this).create();
//                alertDialog.setView(dialogView);
//                alertDialog.show();
//
//                RadioRealButtonGroup group = (RadioRealButtonGroup) dialogView.findViewById(R.id.groupAgo);
//
//                // onClickButton listener detects any click performed on buttons by touch
//                group.setOnClickedButtonListener(new RadioRealButtonGroup.OnClickedButtonListener() {
//                    @Override
//                    public void onClickedButton(RadioRealButton button, int position) {
//                        switch (position) {
//                            case 0:
//                                remindAgo = 0;
//                                remUnit = "minutes";
//                                alarm_time = time;
//                                break;
//
//                            case 1:
//                                remindAgo = 5;
//                                remUnit = "minutes";
//                                alarm_time = time - (5 * 60 * 1000);
//                                break;
//
//                            case 2:
//                                remindAgo = 10;
//                                remUnit = "minutes";
//                                alarm_time = time - (10 * 60 * 1000);
//                                break;
//
//                            case 3:
//                                remindAgo = 15;
//                                remUnit = "minutes";
//                                alarm_time = time - (15 * 60 * 1000);
//                                break;
//
//                            case 4:
//                                remindAgo = 30;
//                                remUnit = "minutes";
//                                alarm_time = time - (30 * 60 * 1000);
//                                break;
//
//                            case 5:
//                                remindAgo = 1;
//                                remUnit = "hour";
//                                alarm_time = time - (60 * 60 * 1000);
//                                break;
//                        }
//
//                        ago_sel.setText("Remind me "+remindAgo+" "+ remUnit + " ago");
//                    }
//                });
//
//                dialogView.findViewById(R.id.rem_ago_set).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//            }
//        });

        scheduler_facebook_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scheduler_facebook_icon.setBackgroundColor(Color.parseColor("#EEEEEE"));
                scheduler_message_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                scheduler_whatsapp_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                spinner_type = "Facebook";
            }
        });

        scheduler_message_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scheduler_facebook_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                scheduler_message_icon.setBackgroundColor(Color.parseColor("#EEEEEE"));
                scheduler_whatsapp_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                spinner_type = "SMS";
            }
        });

        scheduler_whatsapp_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scheduler_facebook_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                scheduler_message_icon.setBackgroundColor(Color.parseColor("#FFFFFF"));
                scheduler_whatsapp_icon.setBackgroundColor(Color.parseColor("#EEEEEE"));
                spinner_type = "Whatsapp";
            }
        });

//        scheduler_prompt_toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // update your model (or other business logic) based on isChecked
//                if(isChecked){
//                    ago_sel.setVisibility(View.VISIBLE);
//                }else{
//                    ago_sel.setVisibility(View.GONE);
//                    remindAgo = 0;
//                }
//            }
//        });

        dialog_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(spinner_type.equals("SMS")){
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

                        if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {

                            String[] permissions = {Manifest.permission.SEND_SMS};

                            requestPermissions(permissions, PERMISSION_REQUEST_SEND_SMS);

                        }else{
                            saveSchedular();
                        }
                    }else{
                        saveSchedular();
                    }
                }else{
                    saveSchedular();
                }
            }
        });
    }

    private void saveSchedular(){
        message = sendMgs.getText().toString();
        //check current state of a check box (true or false)
        checkBoxState = scheduler_prompt_toggle.isChecked();

        String is_manual = "0";
        if(checkBoxState){
            is_manual = "1";
            remindAgo = 5;
        }

        if(remindAgo == 1){
            alarm_time = time - (60 * 60 * 1000);
        }else {
            alarm_time = time - (remindAgo * 60 * 1000);
        }

        if(message.equals("")){
            Toast.makeText(SchedulerDialog.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        if(phone_no == null){
            Toast.makeText(SchedulerDialog.this, "Please Select a Contact", Toast.LENGTH_SHORT).show();
            return;
        }

        phone_no = phone_no.replace(" ", "");

        MyDbHelper myDbHelper = new MyDbHelper(SchedulerDialog.this, null, 1);
        long Id = myDbHelper.addSchedule(c_name, spinner_type, remindAgo+"", phone_no, message, time, is_manual);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(SchedulerDialog.this, SchedulerAlarmDialog.class);
        myIntent.putExtra("alarm_mgs", message);
        myIntent.putExtra("date_time", time);
        myIntent.putExtra("phone", phone_no);
        myIntent.putExtra("name", c_name);
        myIntent.putExtra("is_manual", is_manual);
        myIntent.putExtra("spinner_type", spinner_type);
        myIntent.putExtra("id", Id);
        pendingIntent = PendingIntent.getActivity(SchedulerDialog.this, (int)Id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm_time, pendingIntent);

        Long notification_time = (time/1000);
        long notification_id = myDbHelper.addNotification(phone_no, "Scheduled "+spinner_type+" for "+c_name, notification_time.toString(), 3, "");

        message = "";
        Toast.makeText(SchedulerDialog.this, "Your message has been scheduled Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void selectTime() {
        final View dialogView = View.inflate(SchedulerDialog.this, R.layout.time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(SchedulerDialog.this).create();
        alertDialog.setView(dialogView);
        alertDialog.show();

        dialogView.findViewById(R.id.time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePicker timePicker = dialogView.findViewById(R.id.time_picker_r);

                hour = timePicker.getCurrentHour();
                minute = timePicker.getCurrentMinute();

                Calendar calendar = new GregorianCalendar(year,
                        month,
                        day,
                        hour,
                        minute);

                time = calendar.getTimeInMillis();
                alarm_time = time;
                scheduler_time_text.setText(formateTimes(time));
                alertDialog.dismiss();
            }});
    }

    private void selectDate() {
        final View dialogView = View.inflate(SchedulerDialog.this, R.layout.date_time_picker, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(SchedulerDialog.this).create();
        alertDialog.setView(dialogView);
        alertDialog.show();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = dialogView.findViewById(R.id.date_picker);
                year = datePicker.getYear();
                month = datePicker.getMonth();
                day = datePicker.getDayOfMonth();

                Calendar calendar = new GregorianCalendar(year,
                        month,
                        day,
                        hour,
                        minute);

                time = calendar.getTimeInMillis();
                alarm_time = time;
                scheduler_date_text.setText(formateDates(time));
                alertDialog.dismiss();
            }});
    }

    private void choosePhoneNo() {
        Intent intent = new Intent(this, SelectSosContact.class);
        startActivityForResult(intent, PICK_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                phone_no = data.getExtras().getString("POS_PHONE");
                phone_no = "+91"+getLastnCharacters(phone_no.replace(" ", ""), 10);
                c_name = getContactName(this, phone_no);

                // Do something with the phone number...
                if(c_name == null){
                    c_name = phone_no;
                    scheduler_person_text.setText(phone_no);
                }else{
                    scheduler_person_text.setText(c_name);
                }

                String filePath = Environment.getExternalStorageDirectory() + "/ringerrr/profile/"+phone_no+".jpeg";
                File file = new File(filePath);
                if (file.exists()){
                    RequestOptions requestOptions = RequestOptions.circleCropTransform()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true);
                    Glide.with(SchedulerDialog.this)
                            .load(filePath)
                            .apply(requestOptions)
                            .into(dialog_contact);
                }else{
                    String fl = c_name.substring(0, 1);
                    if (fl.equals("+")) {
                        fl = c_name.substring(1, 2);
                    }
                    ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
                    // generate random color
                    int color1 = generator.getRandomColor();
                    TextDrawable drawable2 = TextDrawable.builder()
                            .buildRound(fl, color1);
                    dialog_contact.setImageDrawable(drawable2);
                }

            }
        }

        if(requestCode == PERMISSION_REQUEST_SEND_SMS && resultCode == Activity.RESULT_OK ){
            saveSchedular();
        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String formateDates(long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy");
        String formattedDate = formatter.format(date);

        return formattedDate;
    }

    private String formateTimes(long timestamp){
        Date date = new Date(timestamp);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = formatter.format(date);

        return formattedDate;
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    public String getLastnCharacters(String inputString, int subStringLength){
        int length = inputString.length();
        if(length <= subStringLength){
            return inputString;
        }
        int startIndex = length-subStringLength;
        return inputString.substring(startIndex);
    }

    public void pickImage(){
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}