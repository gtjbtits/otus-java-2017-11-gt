package com.jbtits.otus.lecture1;


import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Main {
    public static void main (String... args) {
        double[][] matrixData = {{0d,1d,1d}, {2d,3d,5d}};
        RealMatrix m = MatrixUtils.createRealMatrix(matrixData);
        System.out.println("Matrix before transpose");
        System.out.println(m);
        RealMatrix tm = m.transpose();
        System.out.println("Matrix after transpose");
        System.out.println(tm);
    }
}
