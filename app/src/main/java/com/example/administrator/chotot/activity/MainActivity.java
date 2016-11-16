package com.example.administrator.chotot.activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.chotot.R;
import com.example.administrator.chotot.fragment.ChatFragment;
import com.example.administrator.chotot.fragment.InviteFragment;
import com.example.administrator.chotot.fragment.ProductsFragment;
import com.example.administrator.chotot.fragment.SellFragment;
import com.example.administrator.chotot.fragment.SettingFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnQueryTextListener{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private SearchView searchView;

    private ViewPager viewPager;

    private SharedPreferences pre;
    public static String phone, city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pre = getSharedPreferences(LoginActivity.prefname, MODE_PRIVATE);
        phone = pre.getString("username", "");
        city = pre.getString("city", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        ImageView tab1 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setImageResource(R.drawable.xml_tab_products);
        tabLayout.getTabAt(0).setCustomView(tab1);

        ImageView tab2 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setImageResource(R.drawable.xml_tab_chat);
        tabLayout.getTabAt(1).setCustomView(tab2);

        ImageView tab3 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab3.setImageResource(R.drawable.xml_tab_invite);
        tabLayout.getTabAt(2).setCustomView(tab3);

        ImageView tab4 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab4.setImageResource(R.drawable.xml_tab_sell);
        tabLayout.getTabAt(3).setCustomView(tab4);

        ImageView tab5 = (ImageView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab5.setImageResource(R.drawable.xml_tab_setting);
        tabLayout.getTabAt(4).setCustomView(tab5);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ProductsFragment(), "");
        adapter.addFrag(new ChatFragment(), "");
        adapter.addFrag(new InviteFragment(), "");
        adapter.addFrag(new SellFragment(), "");
        adapter.addFrag(new SettingFragment(), "");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search_view);

        searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Tìm kiếm...");
        changeSearchViewTextColor(searchView);
        return true;
    }

    private void changeSearchViewTextColor(View view) {
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(Color.BLACK);
                ((TextView) view).setHintTextColor(getResources().getColor(R.color.colorTextHint));
                return;
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    changeSearchViewTextColor(viewGroup.getChildAt(i));
                }
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pre = getSharedPreferences(LoginActivity.prefname, MODE_PRIVATE);
        city = pre.getString("city", "");
    }
}
