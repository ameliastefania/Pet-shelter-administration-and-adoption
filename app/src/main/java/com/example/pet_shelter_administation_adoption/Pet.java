package com.example.pet_shelter_administation_adoption;

public class Pet {
    String petName = "Anonymous pet" ;
    String petDescription = "none";
    String petKind = "none";
    int petAge = 0;
    String petBreed = "none";
    Boolean isVaccinated = false;
    String imgURL;

    public Pet(String petName, String petDescription, String petKind, int petAge, String petBreed, Boolean isVaccinated, String imgURL) {

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
        this.imgURL = imgURL;

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
        petName = "Anonymous pet";
        imgURL = null;
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
