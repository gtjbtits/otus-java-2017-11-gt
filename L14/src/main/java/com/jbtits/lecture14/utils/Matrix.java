package com.jbtits.lecture14.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class Matrix<T> {
    private int rows;
    private int cols;
    private T matrix[][];

    public Matrix(int rows, int cols, Class<T> clazz) {
        this.matrix = (T[][]) ArrayUtils.createArray(clazz, rows, cols);
        this.rows = rows;
        this.cols = cols;
    }

    public Object[] getRow(int row) {
        return matrix[row];
    }

    public void setRow(int rowId, T[] row) {
        if (row.length > cols) {
            String matrixSize = String.format("%dx%d", rows, cols);
            throw new ArrayIndexOutOfBoundsException("Matrix size is "
                + matrixSize + ". Try to set row with length " + row.length);
        }
        matrix[rowId] = row;
    }

    @Override
    public String toString() {
        String out = "";
        for (T[] row : matrix) {
            out += Arrays.asList(row).toString() + "\n";
        }
        return out;
    }
}
