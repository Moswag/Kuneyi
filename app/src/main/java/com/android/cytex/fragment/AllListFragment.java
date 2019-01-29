package com.android.cytex.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.cytex.R;
import com.android.cytex.util.SelectedOptionSession;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllListFragment extends Fragment {
    private String category;
    SelectedOptionSession selectedOptionSession;
    String seldata;




    public AllListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category=getArguments().getString("category");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_all_list, container, false);

        selectedOptionSession=new SelectedOptionSession(getActivity());
        HashMap<String, String> selected = selectedOptionSession.getSelectedData();
        seldata = selected.get(SelectedOptionSession.KEY_CATEGORY);


        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }



    class PagerAdapter extends FragmentPagerAdapter {
        String tabTitles[] = new String[]{"Company", seldata+" Posts"};
        Context context;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CompanyFragment();
                case 1:
                    return new SelectedCategoryFragment();
                default:
                    return null;

            }

        }


        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        public View getTabView(int position) {
            View tab = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;

        }
    }
}
