package com.example.saleckope.piszkosanyagiak.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by saleckope on 2016. 11. 16..
 */

public class MoneyItem extends SugarRecord implements Serializable {

    //Categories for outlays
    public enum Category {
        food, cosmetics, car, coffee, party, family, travel, sport, electronics, clothes, shopping, wellness, present, culture, bills;


        public static Category getByOrdinal(int ordinal) {
            Category ret = null;
            for (Category cat : Category.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }

        public static int getSize(){
            return 15;
        }
    }

    //the type of the moneyItem
    public enum Type {
        INCOME, OUTLAY;

        public static Type getByOrdinal(int ordinal) {
            Type ret = null;
            for (Type cat : Type.values()) {
                if (cat.ordinal() == ordinal) {
                    ret = cat;
                    break;
                }
            }
            return ret;
        }
    }

    public String note;
    public Type type;
    public Category category;
    public int amount;
    public int year;
    public int month;
    public int day;
}
