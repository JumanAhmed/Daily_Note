package com.capsulestudio.dailynote.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.capsulestudio.dailynote.Adapter.ImageNoteAdapter;
import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.ImageNoteModel;
import com.capsulestudio.dailynote.R;

import java.util.List;

public class ImageNoteActivity extends AppCompatActivity {

    private LinearLayout LayEmpty;
    ListView listView;
    ImageNoteAdapter imageNoteAdapter;
    List<ImageNoteModel> imageNoteModelList;
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Image Note");

        listView = findViewById(R.id.listViewImageNote);
        LayEmpty = (LinearLayout) findViewById(R.id.empty_view);
        db = new DatabaseHandler(ImageNoteActivity.this);
        imageNoteModelList = db.getAllImageNote(Email.getEmail());
        imageNoteAdapter = new ImageNoteAdapter(getApplicationContext(), R.layout.image_note_row, imageNoteModelList);

        if (imageNoteAdapter.isEmpty()) {
            LayEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            LayEmpty.setVisibility(View.GONE);
        }

        listView.setAdapter(imageNoteAdapter);

        // On Item click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
               ImageNoteModel imageNoteModel = imageNoteModelList.get(position);

               Intent intent = new Intent(getApplicationContext(), ImageNoteDetailsActivity.class);
                intent.putExtra("imageNote", imageNoteModel);
                startActivity(intent);
            }
        });

        // On Item Long Click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                LayoutInflater layoutInflater = LayoutInflater.from(ImageNoteActivity.this);
                View cview = layoutInflater.inflate(R.layout.edit_delete_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ImageNoteActivity.this);
                alertDialogBuilder.setView(cview);

                TextView tvEdit = (TextView) cview.findViewById(R.id.tvEdit);
                TextView tvDelete = (TextView) cview.findViewById(R.id.tvDelete);

                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Note")
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //selectImageFromGallery();
                        ImageNoteModel imageNoteModel = imageNoteModelList.get(position);

                        Intent intent = new Intent(getApplicationContext(), UpdateImageNoteActivity.class);
                        intent.putExtra("imageNote", imageNoteModel);
                        startActivity(intent);

                        alertDialog.dismiss();
                    }
                });

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //captureImageFromCamera();

                        ImageNoteModel imageNoteModel = imageNoteModelList.get(position);
                        String id = String.valueOf(imageNoteModel.getId());
                        int delete = db.deleteImageNote(id);
                        if (delete > 0){
                            Toast.makeText(getApplicationContext(), "Note Delete", Toast.LENGTH_SHORT).show();
                            imageNoteAdapter.notifyDataSetChanged();
                            reloadFromDatabase();
                            //reloadStudentTableAfterOperation();

                        }else {
                            Toast.makeText(getApplicationContext(), "Something Wrong", Toast.LENGTH_LONG).show();
                        }
                        alertDialog.dismiss();
                    }
                });

                return true;
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddImageNoteActivity.class));

            }
        });
    }

    private void reloadFromDatabase() {

        String query = "SELECT * FROM tbl_image_note WHERE email = ? ";

        Cursor cursor = db.getInstance().rawQuery(query, new String[]{Email.getEmail()});

        if (cursor.moveToFirst()) {
            imageNoteModelList.clear();
            do {
                imageNoteModelList.add(new ImageNoteModel(
                        cursor.getInt(0),
                        cursor.getBlob(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)

                ));
            } while (cursor.moveToNext());
        }

        if(cursor.moveToFirst())
        {
            LayEmpty.setVisibility(View.GONE);
        }
        else
        {
            LayEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (TextUtils.isEmpty(newText)) {
                    imageNoteAdapter.filter("");
                    listView.clearTextFilter();
                } else {
                    imageNoteAdapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
