package com.example.saleckope.piszkosanyagiak.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.model.MoneyItem;
import com.example.saleckope.piszkosanyagiak.ui.statistics.StatisticsActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by saleckope on 2016. 11. 16..
 */

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.MoneyViewHolder> {
    private static ArrayList<MoneyItem> items = new ArrayList<MoneyItem>();
    public static int money;
    private static Calendar calendar = Calendar.getInstance();
    private static int thisYear = calendar.get(Calendar.YEAR);
    private static int thisMonth = calendar.get(Calendar.MONTH) + 1;

    public MoneyAdapter() {
        items = new ArrayList<>();
    }

    //Method for creating MoneyViewHolder
    @Override
    public MoneyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate MoneyItem's view so we can use it in ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_money_list, parent, false);
        //Create ViewHolder
        MoneyViewHolder viewHolder = new MoneyViewHolder(itemView);

        return viewHolder;
    }

    //Method for sorting itmes list by date
    //decreasing
    public void sortItemsByDate() {
        //Create new Comparator for sorting Collections
        Collections.sort(items, new Comparator<MoneyItem>() {
            @Override
            public int compare(MoneyItem o1, MoneyItem o2) {
                //If the year is bigger
                if (o1.year > o2.year) {
                    //o1 gets before o2
                    return -1;
                } else {
                    //In the same year
                    if (o1.year == o2.year) {
                        //Check month
                        if (o1.month > o2.month) {
                            return -1;
                        } else {
                            //At same month
                            if (o1.month == o2.month) {
                                //Check days
                                if (o1.day > o2.day) {
                                    return -1;
                                } else {
                                    //At same day
                                    //Sort by the amount (bigger gets before smaller)
                                    //So incomes gets before outlays
                                    if (o1.day == o2.day && o1.amount >= o2.amount) {
                                        return -1;
                                    }
                                    return 1;
                                }
                            }
                        }
                    }
                }
                return 1;
            }
        });
    }

    //Method for calculate actual budget
    public static void haveLeft() {
        //Clear spent and haveLeft
        MainActivity.spent = 0;
        MainActivity.haveLeft = MainActivity.budget;

        for (int i = 0; i < items.size(); i++) {
            //If type is outlay
            //If month and year is the actual
            if (items.get(i).type == MoneyItem.Type.OUTLAY && items.get(i).year == thisYear && items.get(i).month == thisMonth) {
                //Add spent money amount
                MainActivity.spent += Math.abs(items.get(i).amount);
                //Decrease haveLeft
                MainActivity.haveLeft += items.get(i).amount;
            }
        }

        //Percent line width
        int lineSize = 1000;
        //Calculate spent and haveLeft line width with right percents
        int spentSize = MainActivity.spent * lineSize / MainActivity.budget;
        int haveSize = lineSize - spentSize;
        //Set spentTextView text to the actual amount of spent
        MainActivity.spentTextView.setText("Spent: " + Integer.toString(MainActivity.spent) + " Ft");
        //Set the width of spent line
        MainActivity.spentSeparator.setLayoutParams(new LinearLayout.LayoutParams(spentSize, 2));
        //Set color of spent line to red
        MainActivity.spentSeparator.setBackgroundColor(Color.RED);
        //If got some money left to spend
        if (MainActivity.haveLeft > 0) {
            //Set haveTextView text to the actual amount of money left to spend
            MainActivity.haveTextView.setText("Have " + Integer.toString(MainActivity.haveLeft) + " Ft left");
            //Set width of haveLeft line
            MainActivity.haveSeparator.setLayoutParams(new LinearLayout.LayoutParams(haveSize, 2));
            //Set color to green
            MainActivity.haveSeparator.setBackgroundColor(Color.rgb(20, 150, 30));
        } else {
            //Set haveTextView text to over budget if no money left to spend
            MainActivity.haveTextView.setText("You are over budget.");
            //Set width to 0
            MainActivity.haveSeparator.setLayoutParams(new LinearLayout.LayoutParams(0, 2));
        }
    }

    //Binding ViewHolder
    @Override
    public void onBindViewHolder(final MoneyViewHolder holder, int position) {
        //Get which item
        MoneyItem item = items.get(position);
        //Set amountTextView
        holder.amountTextView.setText(item.amount + " Ft");

        switch (item.type) {
            case INCOME:
                //If amount type is INCOME
                //Then set amountTextView color to green
                holder.amountTextView.setTextColor(Color.rgb(20, 150, 30));
                //Set category to income, because it has no outlay category
                holder.categoryTextView.setText("income");
                break;
            case OUTLAY:
                //If amount type is OUTLAY
                //Then set amountTextView color to red
                holder.amountTextView.setTextColor(Color.RED);
                //Set outlay's category
                holder.categoryTextView.setText(item.category.name());
                break;
        }
        //Set note
        holder.noteTextView.setText(item.note);
        //Set date
        holder.dateTextView.setText(Integer.toString(item.year) + "." + Integer.toString(item.month) + "." + Integer.toString(item.day) + ".");
        //Set icon to the right type (income/outlay)
        holder.iconImageView.setImageResource(getImageResource(item.type));
        //Set remove button action
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remove item from list
                removeItem(holder.getAdapterPosition());
            }
        });

        //Calculate sum of all items in list
        if (MainActivity.sumMoneyTextView != null) {
            MainActivity.sumMoneyTextView.setText(Integer.toString(money));
        }
    }

    //Method for removing item from the list
    public void removeItem(int position) {
        MoneyItem removed = items.remove(position);

        //Get amount out of sum
        money -= removed.amount;
        if (MainActivity.sumMoneyTextView != null) {
            //Refresh sumMonexTextView with the new amount
            MainActivity.sumMoneyTextView.setText(Integer.toString(money));
        }

        //Calculate spent/haveLeft without the removed item
        haveLeft();
        //Show daily budget
        MainActivity.showDailyBudget();

        //Remove item from database
        removed.delete();
        //Reload list
        notifyItemRemoved(position);
        if (position < items.size()) {
            notifyItemRangeChanged(position, items.size() - position);
        }
    }

    //Method for get items list size
    @Override
    public int getItemCount() {
        return items.size();
    }

    //Method for get the correct image resource to items
    @DrawableRes
    private int getImageResource(MoneyItem.Type type) {
        @DrawableRes int ret;
        switch (type) {
            case INCOME:
                ret = R.drawable.income;
                break;
            case OUTLAY:
                ret = R.drawable.outlay;
                break;
            default:
                ret = 0;
        }
        return ret;
    }

    //Method for add new item to list
    public void addItem(MoneyItem item) {
        items.add(item);
        //Increase sum money amount
        money += item.amount;
        //Resort list
        sortItemsByDate();
        //Notify adapter about item is inserted
        notifyItemInserted(items.size() - 1);
        //Calculate spent/haveLeft with the added item
        haveLeft();
        //Show daily budget
        MainActivity.showDailyBudget();
        //Reload list view
        notifyDataSetChanged();
    }

    //Method for updating list
    public void update(List<MoneyItem> moneyItems) {
        //Clear list
        items.clear();
        //Reload list from moneyItems
        items.addAll(moneyItems);
        //Sort list by date
        sortItemsByDate();
        //Calculate sum money amount
        money = 0;
        for (MoneyItem m : moneyItems) {
            money += m.amount;
        }
        //Calculate spent/haveLeft
        haveLeft();
        //Show daily budget
        MainActivity.showDailyBudget();
        //Reload list view
        notifyDataSetChanged();
    }

    //Class for item view recycling
    public class MoneyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImageView;
        TextView amountTextView;
        TextView noteTextView;
        TextView categoryTextView;
        TextView dateTextView;
        ImageButton removeButton;
        private Context context;

        public MoneyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
            iconImageView = (ImageView) itemView.findViewById(R.id.MoneyItemIcon);
            amountTextView = (TextView) itemView.findViewById(R.id.MoneyItemAmountTextView);
            noteTextView = (TextView) itemView.findViewById(R.id.MoneyItemNoteTextView);
            categoryTextView = (TextView) itemView.findViewById(R.id.MoneyItemCategoryTextView);
            dateTextView = (TextView) itemView.findViewById(R.id.MoneyItemDateTextView);
            removeButton = (ImageButton) itemView.findViewById(R.id.MoneyItemRemoveButton);
        }


        //Method for clicked on one item
        @Override
        public void onClick(View v) {
            //Create intent to StatisticsActivity
            Intent showDetailsIntent = new Intent(context, StatisticsActivity.class);
            //Create extra data to intent
            Bundle bundle = new Bundle();
            bundle.putSerializable("DATA", items.get(getPosition()));
            bundle.putSerializable("LIST", items);
            //Put data on intent
            showDetailsIntent.putExtras(bundle);
            //start activity with the intent
            context.startActivity(showDetailsIntent);
        }
    }
}