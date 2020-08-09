package com.shivivats.kindcompanion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class OnBoardingActivity extends AppCompatActivity {

    // Previous and Next buttons
    MenuItem nextButton;
    // Toolbar
    Toolbar topBar;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 onBoardingPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_on_boarding);

        // Instantiate a ViewPager2 and a PagerAdapter.
        onBoardingPager = findViewById(R.id.onBoardingViewPager);
        pagerAdapter = new ScreenSlidePagerAdapter(this);
        onBoardingPager.setAdapter(pagerAdapter);

        topBar = findViewById(R.id.onBoardingTopBar);
        setSupportActionBar(topBar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowTitleEnabled(false);
        ab.setDisplayHomeAsUpEnabled(true);

        onBoardingPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                if (nextButton != null) {
                    if (position == onBoardingPager.getAdapter().getItemCount() - 1) {
                        nextButton.setTitle("Done");
                    } else {
                        nextButton.setTitle("Next");
                    }
                }
            }
        });

        //onBoardingPager.setPageTransformer(new ZoomOutPageTransformer());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.topbar_onboarding, menu);

        nextButton = menu.findItem(R.id.action_next_onboarding);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next_onboarding:
                nextPage();
                return true;

            case R.id.action_skip_onboarding:
                finishOnBoarding();
                return true;

            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void nextPage() {
        if (onBoardingPager.getCurrentItem() == onBoardingPager.getAdapter().getItemCount() - 1) {
            // the last screen
            finishOnBoarding();
        } else {
            onBoardingPager.setCurrentItem(onBoardingPager.getCurrentItem() + 1, true);
        }
    }

    private void finishOnBoarding() {
        // set onboarding_complete to true in our preferences
        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);

        preferences.edit().putBoolean("onboarding_complete", true).apply();

        // start the front page activity
        Intent frontPageIntent = new Intent(this, FrontPageActivity.class);
        startActivity(frontPageIntent);

        // close this activity
        finish();
    }

    @Override
    public void onBackPressed() {
        if (onBoardingPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            onBoardingPager.setCurrentItem(onBoardingPager.getCurrentItem() - 1, true);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        //private Context myContext;
        int totalPages = 4;

        public ScreenSlidePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // return a NEW fragment instance in this function
            switch (position) {
                case 0:
                    return new OnBoardingFragmentFirst();
                case 1:
                    return new OnBoardingFragmentSecond();
                case 2:
                    return new OnBoardingFragmentThird();
                case 3:
                    return new OnBoardingFragmentFourth();
                //case 4:
                //    return new OnBoardingFragmentFifth();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return totalPages;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }
}