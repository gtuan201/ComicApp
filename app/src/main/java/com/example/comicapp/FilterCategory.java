package com.example.comicapp;

import android.widget.Filter;

import com.example.comicapp.adapter.CategoryAdapter;
import com.example.comicapp.model.Category;

import java.util.ArrayList;

public class FilterCategory extends Filter {
    public ArrayList<Category> filterList;
    private CategoryAdapter categoryAdapter;

    public FilterCategory(ArrayList<Category> filterList, CategoryAdapter categoryAdapter) {
        this.filterList = filterList;
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        //value k null và empty
        if (constraint!=null && constraint.length() > 0){
            constraint = constraint.toString().toUpperCase();
            ArrayList<Category> filtermodelCate = new ArrayList<>();
            for (int i=0 ; i< filterList.size();i++){
                if (filterList.get(i).getCategory().toUpperCase().contains(constraint)){
                    filtermodelCate.add(filterList.get(i));
                }
            }
            results.count = filtermodelCate.size();
            results.values = filtermodelCate;
        }
        else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        categoryAdapter.categoryArrayList = (ArrayList<Category>)results.values;
        categoryAdapter.notifyDataSetChanged();
    }
}
