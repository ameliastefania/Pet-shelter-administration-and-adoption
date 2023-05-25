package com.example.pet_shelter_administation_adoption;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends PagerAdapter {


    private Context context;
    private List<Pet> listPets;

    int images[] = {

            R.drawable.git,
            R.drawable.dog
    };

    int names[] = {
            R.string.cat,
            R.string.dog
    };

    public ImageAdapter(Context context, List<Pet> listPets) {
        this.context = context;
        this.listPets = listPets;
    }

//    public ImageAdapter(Context context) {
//        this.context = context;
//    }

    @Override
    public int getCount() {
//          return images.length;
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
        TextView tvPetNameAdapter = (TextView) view.findViewById(R.id.tvPetNameAdapter);
        TextView tvPetDescriere = (TextView) view.findViewById(R.id.tvPetDescriereLayout);

        Pet currentPet = listPets.get(position);
        tvPetNameAdapter.setText(currentPet.getPetName());
        tvPetDescriere.setText(currentPet.getPetDescription());

        Uri imgUri = Uri.parse(currentPet.getImgURL());

        //slideImage.setImageURI(imgUri);
        Picasso.get()
                .load(imgUri)
                .into(slideImage);
        Picasso.get().setLoggingEnabled(true);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

}
