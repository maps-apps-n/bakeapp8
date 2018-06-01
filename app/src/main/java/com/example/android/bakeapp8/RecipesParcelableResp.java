package com.example.android.bakeapp8;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;



public class RecipesParcelableResp implements Parcelable {

    private int id;
    private int servings;
    private String name;
    private String image;
    private ArrayList<iObjects> ingredients;
    private ArrayList<sObjects> steps;

    public RecipesParcelableResp() { }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
        Bundle b = new Bundle();
        b.putParcelableArrayList("iList",ingredients);
        b.putParcelableArrayList("sList", steps);
        dest.writeBundle(b);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<RecipesParcelableResp> CREATOR = new Parcelable.Creator<RecipesParcelableResp>() {
        @Override
        public RecipesParcelableResp createFromParcel(Parcel in) {
            RecipesParcelableResp recipesParcelableResp = new RecipesParcelableResp();
            recipesParcelableResp.id = in.readInt();
            recipesParcelableResp.image = in.readString();
            recipesParcelableResp.name = in.readString();
            recipesParcelableResp.servings = in.readInt();
            Bundle b = in.readBundle(iObjects.class.getClassLoader());
            Bundle b2 = in.readBundle(sObjects.class.getClassLoader());
            recipesParcelableResp.ingredients = b.getParcelableArrayList("ingredientsList");
            recipesParcelableResp.steps = b2.getParcelableArrayList("stepsList");
            return recipesParcelableResp;
        }

        @Override
        public RecipesParcelableResp[] newArray(int size) {
            return new RecipesParcelableResp[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public ArrayList<iObjects> getIngredients() {
        return ingredients;
    }

    public ArrayList<sObjects> getSteps() {
        return steps;
    }


    public static class iObjects implements Parcelable {

        private double quantity;
        private String measure;
        private String ingredient;


        protected iObjects(Parcel in) {
            quantity = in.readDouble();
            measure = in.readString();
            ingredient = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(quantity);
            dest.writeString(measure);
            dest.writeString(ingredient);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<iObjects> CREATOR = new Creator<iObjects>() {
            @Override
            public iObjects createFromParcel(Parcel in) {
                return new iObjects(in);
            }

            @Override
            public iObjects[] newArray(int size) {
                return new iObjects[size];
            }
        };

        public double getQuantity() {
            return quantity;
        }

        public String getMeasure() {
            return measure;
        }

        public String getIngredient() {
            return ingredient;
        }
    }

    public static class sObjects implements Parcelable {

        private int id;
        private String shortDescription;
        private String description;
        private String videoURL;
        private String thumbnailURL;

        protected sObjects(Parcel in) {
            id = in.readInt();
            shortDescription = in.readString();
            description = in.readString();
            videoURL = in.readString();
            thumbnailURL = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(shortDescription);
            dest.writeString(description);
            dest.writeString(videoURL);
            dest.writeString(thumbnailURL);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<sObjects> CREATOR = new Creator<sObjects>() {
            @Override
            public sObjects createFromParcel(Parcel in) {
                return new sObjects(in);
            }

            @Override
            public sObjects[] newArray(int size) {
                return new sObjects[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getVideoURL() {
            return videoURL;
        }

        public String getThumbnailURL() {
            return thumbnailURL;
        }
    }
}
