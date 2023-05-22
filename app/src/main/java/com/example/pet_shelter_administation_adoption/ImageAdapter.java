package com.example.pet_shelter_administation_adoption;

//import com.smarteist.autoimageslider.SliderViewAdapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ImageAdapter extends PagerAdapter {


    private Context context;
    private List<Pet> listPets;

    public ImageAdapter(Context context, List<Pet> listPets) {
        this.context = context;
        this.listPets = listPets;
    }

    @Override
    public int getCount() {
        return listPets.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_item,container,false);

        ImageView slideImage = (ImageView) view.findViewById(R.id.image_view_upload);
        Pet currentPet = listPets.get(position);
        Uri imgUri = Uri.parse(currentPet.getImgURL());
        slideImage.setImageURI(imgUri);

        container.addView(view);
        return view;
    }
}
