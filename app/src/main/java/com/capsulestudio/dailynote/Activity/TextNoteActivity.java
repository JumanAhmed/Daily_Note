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

import com.capsulestudio.dailynote.Adapter.TextNoteAdapter;
import com.capsulestudio.dailynote.Db.DatabaseHandler;
import com.capsulestudio.dailynote.Helper.Email;
import com.capsulestudio.dailynote.Model.TextNoteModel;
import com.capsulestudio.dailynote.R;

import java.util.List;

public class TextNoteActivity extends AppCompatActivity {

    private LinearLayout LayEmpty;
    ListView listView;
    TextNoteAdapter textNoteAdapter;
    List<TextNoteModel> textNoteModelList;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_note);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Text Note");


        listView = (ListView) findViewById(R.id.listViewTextNote);
        LayEmpty = (LinearLayout) findViewById(R.id.empty_view);
        db = new DatabaseHandler(TextNoteActivity.this);
        textNoteModelList = db.getAllTextNote(Email.getEmail());

        textNoteAdapter = new TextNoteAdapter(getApplicationContext(), R.layout.text_note_row, textNoteModelList);

        if (textNoteAdapter.isEmpty()) {
            LayEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            LayEmpty.setVisibility(View.GONE);
        }

        listView.setAdapter(textNoteAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                TextNoteModel textNoteModel = textNoteModelList.get(position);

                Intent intent = new Intent(getApplicationContext(), TextNoteDetailsActivity.class);
                intent.putExtra("textNote", textNoteModel);
                startActivity(intent);
            }
        });



        // On Item Long Click
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {

                LayoutInflater layoutInflater = LayoutInflater.from(TextNoteActivity.this);
                View cview = layoutInflater.inflate(R.layout.edit_delete_dialog, null);
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TextNoteActivity.this);
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
                        TextNoteModel textNoteModel = textNoteModelList.get(position);

                        Intent intent = new Intent(getApplicationContext(), UpdateTextNoteActivity.class);
                        intent.putExtra("textNote", textNoteModel);
                        startActivity(intent);

                        alertDialog.dismiss();
                    }
                });

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        TextNoteModel textNoteModel = textNoteModelList.get(position);
                        String id = String.valueOf(textNoteModel.getId());
                        int delete = db.deleteTextNote(id);
                        if (delete > 0){
                            Toast.makeText(getApplicationContext(), "Note Delete", Toast.LENGTH_SHORT).show();
                            textNoteAdapter.notifyDataSetChanged();
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
                startActivity(new Intent(getApplicationContext(), AddTextNoteActivity.class));
            }
        });
    }





    private void reloadFromDatabase() {

        String query = "SELECT * FROM tbl_text_note WHERE email = ? ";

        Cursor cursor = db.getInstance().rawQuery(query, new String[]{Email.getEmail()});

        if (cursor.moveToFirst()) {
            textNoteModelList.clear();
            do {
                textNoteModelList.add(new TextNoteModel(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)

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
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
                    textNoteAdapter.filter("");
                    listView.clearTextFilter();
                } else {
                    textNoteAdapter.filter(newText);
                }
                return true;
            }
        });
        return true;
    }

}
