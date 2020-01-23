package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

class Item implements Comparable {

    Recognition recognition;
    public Item(Recognition r ){
        recognition = r;
    }

    @Override
    public int compareTo(Object o) {
        Recognition rt = (Recognition) o;
        double x1 = recognition.getLeft();
        double x2 = rt.getLeft();
        return Double.compare(x1, x2);
    }
    public String toString(){
        return recognition.getLabel() + ": " + String.format("%.1f",recognition.getConfidence()) + " " + recognition.getImageWidth() + " " + recognition.getLeft();
    }
}
