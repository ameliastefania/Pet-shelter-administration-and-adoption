package com.example.pet_shelter_administation_adoption;

import java.util.Map;

public class Pet {
    String petName = "none";
    String petDescription = "none";
    String petKind = "none";
    int petAge =0;
    String petBreed = "none";
    Boolean isVaccinated = Boolean.TRUE;
    String imgURL = "none";

    Map<String, Object> myMap;

    public Pet (Map myMap) {
        this.imgURL = myMap.get("imgURL").toString();
        this.petName = myMap.get("petName").toString();
        this.petDescription = myMap.get("petDescription").toString();
        this.petKind = myMap.get("petKind").toString();
        this.petBreed = myMap.get("petBreed").toString();
        this.petAge = Integer.parseInt(myMap.get("petAge").toString());
        this.isVaccinated = Boolean.parseBoolean(myMap.get("petDescription").toString());
    }

    @Override
    public String toString() {
        return "Pet{" +
                "petName='" + petName + '\'' +
                ", petDescription='" + petDescription + '\'' +
                ", petKind='" + petKind + '\'' +
                ", petAge=" + petAge +
                ", petBreed='" + petBreed + '\'' +
                ", isVaccinated=" + isVaccinated +
                ", imgURL='" + imgURL + '\'' +
                ", myMap=" + myMap +
                '}';
    }

    public Pet(String petName, String petDescription, String petKind, int petAge, String petBreed, Boolean isVaccinated, String imgURL) {

        this.imgURL = imgURL;
        if (petName.trim().equals("")) {
            petName = "Anonymous pet";
        } else {
            this.petName = petName;
        }
        if (petDescription.trim().equals("")) {
            this.petDescription = "none";
        } else {
            this.petDescription = petDescription;
        }

        if (petKind.trim().equals("")) {
            this.petKind = "none";
        } else {
            this.petKind = petKind;
        }

        this.petAge = petAge;

        if (petBreed.trim().equals("")) {
            this.petBreed = "none";
        } else {
            this.petBreed = petBreed;
        }
        this.isVaccinated = isVaccinated;
    }

    public String getPetDescription() {
        return petDescription;
    }

    public void setPetDescription(String petDescription) {
        this.petDescription = petDescription;
    }

    public String getPetKind() {
        return petKind;
    }

    public void setPetKind(String petKind) {
        this.petKind = petKind;
    }

    public int getPetAge() {
        return petAge;
    }

    public void setPetAge(int petAge) {
        this.petAge = petAge;
    }

    public String getPetBreed() {
        return petBreed;
    }

    public void setPetBreed(String petBreed) {
        this.petBreed = petBreed;
    }

    public String getVaccinated() {
        if (isVaccinated == Boolean.TRUE) {
            return "yes";
        } else {
            return "no";
        }
    }

    public void setVaccinated(Boolean vaccinated) {
        isVaccinated = vaccinated;
    }

    public Pet(String petName, String imgURL) {
        if (petName.trim().equals("")) {
            petName = "Anonymous pet";
        } else {
            this.petName = petName;
        }
        this.imgURL = imgURL;
    }

    public Pet() {
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }


}
