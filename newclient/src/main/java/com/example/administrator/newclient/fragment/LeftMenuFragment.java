package com.example.administrator.newclient.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.newclient.HomeActivity;
import com.example.administrator.newclient.R;
import com.example.administrator.newclient.bean.Categories;
import com.example.administrator.newclient.page.impl.NewsPage;

/**
 * Created by Administrator on 2016/4/11.
 */
public class LeftMenuFragment extends Fragment {
    private ListView lv_slidingmenu_type;
    Categories categories;
    private MyleftMenulistAdapter adapter;
    private ContentFragment contentFragment;
    private int currentPostion;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*TextView tv = new TextView(getActivity());
        tv.setText("leftmenu");
        tv.setBackgroundColor(Color.RED);*/
        View view = inflater.inflate(R.layout.slidingmenu, null);
        final HomeActivity homeactivity = (HomeActivity) getActivity();
        contentFragment = homeactivity.getContentFragment();
        lv_slidingmenu_type = (ListView) view.findViewById(R.id.lv_slidingmenu_type);//为侧滑的菜单设置了listview的布局
        lv_slidingmenu_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsPage newsPage = contentFragment.getNewsPage();
                newsPage.setNewsType(position);//将得到的是什么类型的告诉content页面 然后主页面去刷新它里面的view
                homeactivity.setSlidingMenuToggle();
                final TextView tv = (TextView) view.findViewById(R.id.tv_menulistitem_category);
                currentPostion=position;
                adapter.notifyDataSetChanged();
//                tv.setEnabled(true);

            }
        });

        return view;//super.onCreateView(inflater, container, savedInstanceState);
    }
    class MyleftMenulistAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return categories.data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.menu_list_item, null);
            TextView tv_menulistitem_category = (TextView) view.findViewById(R.id.tv_menulistitem_category);
            tv_menulistitem_category.setText(categories.data.get(position).title);
            if (position==currentPostion){
                tv_menulistitem_category.setEnabled(true);
            }
            return view;
        }
    }
    //设置类型
    public void setCategories(Categories categories){
        this.categories=categories;
        adapter = new MyleftMenulistAdapter();
        lv_slidingmenu_type.setAdapter(adapter);
    }
}
