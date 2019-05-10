class IntensityMatrix {
  private int[][] matrix;
  private final int size;

  IntensityMatrix(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException();
    }
    this.size = size;
    matrix = new int[size][size];
  }

  int getIntensity(int sourceNodeNumber, int sinkNodeNumber) {
    if (sourceNodeNumber >= size || sinkNodeNumber >= size) {
      throw new IllegalArgumentException();
    }
    return matrix[sourceNodeNumber][sinkNodeNumber];
  }

  void setRow(int rowNumber, int[] row) {
    if (rowNumber >= size || row.length != size) {
      throw new IllegalArgumentException();
    }
    for (int i = 0; i < size; i++) {
      if (row[i] < 0) {
        throw new IllegalArgumentException();
      }
    }
    matrix[rowNumber] = row;
  }

  int getSum() {
    int sum = 0;
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        sum += matrix[i][j];
      }
    }
    return sum;
  }
}
