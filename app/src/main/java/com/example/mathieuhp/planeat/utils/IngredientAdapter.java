package com.example.mathieuhp.planeat.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Ingredient;

import java.text.Normalizer;
import java.util.ArrayList;

public class IngredientAdapter extends ArrayAdapter<Ingredient> implements Filterable {

    private static class ViewHolder {
        private TextView textViewName;
        private TextView textViewType;
    }

    private ViewHolder viewHolder;

    private ArrayList<Ingredient> originalData;
    private ArrayList<Ingredient> filteredList;

    public IngredientAdapter(Context context, int textViewResourceId, ArrayList<Ingredient> listIngredient) {
        super(context, textViewResourceId, listIngredient);

        this.originalData = new ArrayList<>(listIngredient);

        this.filteredList = new ArrayList<>();
        filteredList.addAll(originalData);
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.row_ingredient, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.ingredient_name);
            viewHolder.textViewType = (TextView) convertView.findViewById(R.id.ingredient_type);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ingredient ingr = getItem(position);
        if(ingr != null) {
            viewHolder.textViewName.setText(filteredList.get(position).getName());
            viewHolder.textViewType.setText(filteredList.get(position).getType());
        }

        return convertView;
    }

    /**
     * Search in ingredient name and type if the char sequence is present
     * @return the filter
     */
    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();

                if(charSequence == null || charSequence.length() == 0 || charSequence == "") {
                    // No filter implemented, return the full list
                    results.values = originalData;
                    results.count = originalData.size();
                }
                else {
                    ArrayList<Ingredient> resList = new ArrayList<>();
                    // compare the charSequence with the Ingredient name
                    String data;
                    String dataNormalized;
                    String charSequenceNormalized;

                    for(Ingredient i : originalData) {
                        // search in name and type
                        data = i.getName() + " " + i.getType();
                        dataNormalized = Normalizer.normalize(data, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
                        charSequenceNormalized = Normalizer.normalize(charSequence, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");

                        if(dataNormalized.toLowerCase().contains(charSequenceNormalized.toLowerCase())) {
                            resList.add(i);
                        }
                    }

                    results.values = resList;
                    results.count = resList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                clear();
                filteredList.clear();
                for (Ingredient i : (ArrayList<Ingredient>) filterResults.values) {
                    add(i);
                    filteredList.add(i);
                }
                notifyDataSetChanged();
            }
        };
    }
}
