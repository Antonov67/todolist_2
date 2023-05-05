package com.arhiser.todolist.screens.main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import com.arhiser.todolist.R;
import com.arhiser.todolist.model.Note;
import com.arhiser.todolist.screens.details.NoteDetailsActivity;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.zzz1zzz.simplecolorpicker.ColorPickerListener;
import com.zzz1zzz.simplecolorpicker.SimpleColorPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    final Adapter adapter = new Adapter();

    List<Note> filteredNotes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final SearchView searchView_home;

        recyclerView = findViewById(R.id.list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        searchView_home = findViewById(R.id.searchView);

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter (newText);
                return true;
            }
        });



        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDetailsActivity.start(MainActivity.this, null);
            }
        });

        FloatingActionButton fabColor = findViewById(R.id.fabcolor);
        MaterialToolbar materialToolbar = findViewById(R.id.toolbar);
        SearchView searchView = findViewById(R.id.searchView);
        fabColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SimpleColorPicker.Builder(MainActivity.this)
                        .setTitle("Select Color") // Optional
                        .setListener(new ColorPickerListener() {
                            @Override
                            public void onColorSelected(@NonNull String color) {
                                // Use selected color
                                Log.d("color ", ""+color);
                                materialToolbar.setBackgroundColor(Color.parseColor(color));
                                searchView.setBackgroundColor(Color.parseColor(color));
                                fabColor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(color)));
                                //сохраним выбранный цвет для других активити
                                Utils.uiColor = Color.parseColor(color);
                            }
                        })
                        .build()
                        .show();
            }
        });

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.getNoteLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                searchView_home.setQuery("", false); //очистим поле поиска
                filteredNotes.clear();
                filteredNotes.addAll(notes);
                adapter.setItems(notes);
            }
        });


    }

    private void filter(String newText) {
        List<Note> notes = new ArrayList<>();
        for (Note singleNote : filteredNotes){
            if (singleNote.text.toLowerCase().contains(newText.toLowerCase())){
                notes.add(singleNote);
                adapter.setItems(notes);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
