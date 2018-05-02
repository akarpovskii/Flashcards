package com.gmail.ooad.flashcards.slideshow;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Created by akarpovskii on 30.04.18.
 */
class SlideShowStatistics implements Parcelable {
    private int mMemorized = 0;

    private int mUnmemorized = 0;

    int getMemorized() {
        return mMemorized;
    }

    int getUnmemorized() {
        return mUnmemorized;
    }

    void incMemorized() {
        ++mMemorized;
    }

    void incUnmemorized() {
        ++mUnmemorized;
    }

    SlideShowStatistics() {

    }

    SlideShowStatistics(Parcel in) {
        int[] data = new int[2];
        in.readIntArray(data);
        mMemorized = data[0];
        mUnmemorized = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {mMemorized, mUnmemorized});
    }

    public static final Parcelable.Creator<SlideShowStatistics> CREATOR =
            new Parcelable.Creator<SlideShowStatistics>() {
        @Override
        public SlideShowStatistics createFromParcel(Parcel source) {
            return new SlideShowStatistics(source);
        }

        @Override
        public SlideShowStatistics[] newArray(int size) {
            return new SlideShowStatistics[size];
        }
    };
}
