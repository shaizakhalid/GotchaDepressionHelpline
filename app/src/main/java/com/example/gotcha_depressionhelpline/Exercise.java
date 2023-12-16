package com.example.gotcha_depressionhelpline;

 class Exercise {

        private String eName;
        private String eDescription;
        int eImage;

        public Exercise( String eName, String eDescription, int image) {
            this.eName = eName;
            this.eDescription = eDescription;
            this.eImage= image;
        }

     public int geteImage() {
         return eImage;
     }

     public void seteImage(int eImage) {
         this.eImage = eImage;
     }

     public String getexerciseName() {
            return eName;
        }

        public String getexerciseDescription() {
            return eDescription;
        }

        public void setexerciseName(String eName) {
            this.eName = eName;
        }

        public void setexerciseDescription(String eDescription) {
            this.eDescription = eDescription;
        }
    }

