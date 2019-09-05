package com.dicio.dicio_android;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.text.InputType;
import android.view.MenuItem;

import com.dicio.component.AssistanceComponent;
import com.dicio.component.input.InputRecognizer;
import com.dicio.component.output.OutputGenerator;
import com.dicio.component.output.views.BaseView;
import com.dicio.component.output.views.DescribedImage;
import com.dicio.component.output.views.Description;
import com.dicio.component.output.views.Header;
import com.dicio.component.output.views.Image;
import com.dicio.dicio_android.eval.ComponentEvaluator;
import com.dicio.dicio_android.eval.ComponentRanker;
import com.dicio.dicio_android.renderer.OutputContainerView;
import com.dicio.dicio_android.renderer.OutputDisplayer;
import com.dicio.dicio_android.renderer.OutputRenderer;
import com.dicio.dicio_android.settings.SettingsActivity;
import com.dicio.dicio_android.util.ThemedActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainActivity extends ThemedActivity
        implements NavigationView.OnNavigationItemSelectedListener, OutputDisplayer {
    DrawerLayout drawer;
    LinearLayout outputViews;
    ComponentRanker componentRanker;
    ComponentEvaluator componentEvaluator;


    ////////////////////////
    // Activity lifecycle //
    ////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        outputViews = findViewById(R.id.outputViews);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initAssistanceComponents();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        final MenuItem textInputItem = menu.findItem(R.id.action_text_input);
        textInputItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                item.setVisible(false);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                // resets the whole menu, setting `item`'s visibility to true
                invalidateOptionsMenu();
                return true;
            }
        });

        SearchView textInputView = (SearchView) textInputItem.getActionView();
        textInputView.setQueryHint(getResources().getString(R.string.text_input_hint));
        textInputView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        textInputView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                componentEvaluator.evaluateMatchingComponent(query);
                textInputItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /////////////////////////////////////
    // Assistance components functions //
    /////////////////////////////////////

    public void initAssistanceComponents() {
        componentRanker = new ComponentRanker(new AssistanceComponent() {
            @Override
            public InputRecognizer.Specificity specificity() {
                return null;
            }

            @Override
            public void setInput(List<String> words) {

            }

            @Override
            public List<String> getInput() {
                return null;
            }

            @Override
            public float score() {
                return 0;
            }

            @Override
            public void calculateOutput() {

            }

            @Override
            public List<BaseView> getGraphicalOutput() {
                return new ArrayList<BaseView>() {{
                    add(new Header("I could not process what you told me, sorry :-("));
                }};
            }

            @Override
            public String getSpeechOutput() {
                return "I don't understand, sorry";
            }

            @Override
            public Optional<OutputGenerator> nextOutputGenerator() {
                return null;
            }

            @Override
            public Optional<List<AssistanceComponent>> nextAssistanceComponents() {
                return null;
            }
        });
        componentEvaluator = new ComponentEvaluator(componentRanker, this, this);
    }

    @Override
    public void addGraphicalOutput(@NonNull OutputContainerView graphicalOutput) {
        outputViews.addView(graphicalOutput);
    }

    @Override
    public void addSpeechOutput(@NonNull String speechOutput) {
        Toast.makeText(this, speechOutput, Toast.LENGTH_LONG).show();
    }
}
