package stuff;
public class Matrix {
	public float grid[][]; //grid[row][column]
	final int rows;
	final int cols;
	
	public Matrix(int height, int width){ //Matrix(# of rows, # of cols)
		rows = height;
		cols = width;
		if(rows < 1){
			throw new IndexOutOfBoundsException(String.format("Height set to %d, must be 1 or greater", rows));
		}else if(cols < 1){
			throw new IndexOutOfBoundsException(String.format("Width set to %d, must be 1 or greater", cols));			
		}
		grid = new float[rows][cols];
	}
	
	public Matrix(int width, int height, float[] filler){
		this(width, height);
		this.fill(filler);
	}
	
	public void fill(float[] values){
		if(values.length==rows*cols){
			int counter = 0;
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					grid[r][c] = values[counter];
					counter++;
				}
			}
		}else{
			throw new ArithmeticException(String.format("%d arguments provided, need %d arguments to fill a %dx%d matrix.", values.length, (rows*cols), cols, rows));
		}
	}
	
	public void copy(Matrix m){
		if(rows==m.rows && cols==m.cols){
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					grid[r][c] = m.grid[r][c];
				}
			}
		}else{
			throw new IllegalArgumentException("Matrices must be the same size to copy.");
		}
	}
		
	public void add(Matrix m){
		if(m.rows == rows && m.cols == cols){
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					grid[r][c] += m.grid[r][c];
				}
			}
		}else{
			throw new ArithmeticException("Matrices must be the same size to be added.");
		}
	}
	
	public void multiply(float i){
		for(int r=0;r<rows;r++){
			for(int c=0;c<cols;c++){
				grid[r][c] *= i;
			}
		}
	}
	
	public Matrix multiply(Matrix m){
		if(m.rows == 1 && m.cols == 1){
			multiply(m.grid[0][0]);
			return this;
		}else if(rows == m.cols && m.rows == m.cols){ //Result will be the same size. Mutate and return this array.
			float[] store = new float[rows*cols];
			float product;
			for(int r=0;r<rows;r++){
				for(int c=0;c<cols;c++){
					product = 0f;
					for(int c2 = 0;c2<cols;c2++){
						product += ((grid[r][c2])*m.grid[c2][c]);
					}
					store[c+(r*cols)]=product;
				}
			}
			fill(store);
			return this;
		}else if(rows == m.cols){ //Result will be different size. Delegate to static method and return new array
			return productOf(this, m);
		}else{
			throw new ArithmeticException("This array's height must match input array's width.");
		}
	}
	
	public void invert(){
		copy(inverseOf(this));
	}
	
	public static float[] arrayOf(Matrix m){
		return arrayOf(m.grid);
	}

	public static float[] arrayOf(float[][]m){
		float[] result = new float[m.length*m[0].length];
		for(int r=0;r<m.length;r++){
			for(int c=0;c<m[0].length;c++){
				result[c+(r*m[0].length)] = m[r][c];
			}
		}
		return result;
	}
	
	public static Matrix sumOf (Matrix A, Matrix B){
		if(A.rows == B.rows && A.cols == B.cols){
			float[] newVals = new float[A.rows * A.cols];
			for(int r=0;r<A.rows;r++){
				for(int c=0;c<A.cols;c++){
					newVals[(r*A.rows)+c] = A.grid[r][c] + B.grid[r][c];
				}
			}
			Matrix result = new Matrix(A.cols, A.rows);
			result.fill(newVals);
			return result;
		}else{
			throw new ArithmeticException("Matrices must be the same size to be added.");
		}
	}
	
	public static Matrix inverseOf(Matrix m){
		if(m.rows != m.cols || m.rows > 3){
			throw new ArithmeticException("Inverse can only be found for square matrices of side 3 or less.");
		}else if(determinantOf(m) == 0){
			throw new ArithmeticException("A matrix with a zero determinant cannot be inverted.");
		}else if(m.rows == 1 && m.cols == 1){
			return new Matrix(1, 1, new float[]{1f/m.grid[0][0]});
		}else if(m.rows == 2 && m.cols == 2){
			return productOf(new Matrix(2, 2, new float[]{m.grid[1][1], - m.grid[0][1], - m.grid[1][0], m.grid[0][0]}), 1f/determinantOf(m));
		}else{
			Matrix op = new Matrix(3, 6);
			float mult; int i = 0;
			op.fill(new float[]{m.grid[0][0], m.grid[0][1], m.grid[0][2], 1f, 0f, 0f, m.grid[1][0], m.grid[1][1], m.grid[1][2], 0f, 1f, 0f, m.grid[2][0], m.grid[2][1], m.grid[2][2], 0f, 0f, 1f});
			mult = op.grid[1][0]/op.grid[0][0];
			for(i=0; i < 6; i++){ op.grid[1][i] -= op.grid[0][i]*mult; }
			mult = op.grid[2][0]/op.grid[0][0];
			for(i=0; i < 6; i++){ op.grid[2][i] -= op.grid[0][i]*mult; }
			mult = op.grid[2][1]/op.grid[1][1];
			for(i=0; i < 6; i++){ op.grid[2][i] -= op.grid[1][i]*mult; }
			mult = op.grid[1][2]/op.grid[2][2];
			for(i=0; i < 6; i++){ op.grid[1][i] -= op.grid[2][i]*mult; }
			mult = op.grid[0][1]/op.grid[1][1];
			for(i=0; i < 6; i++){ op.grid[0][i] -= op.grid[1][i]*mult; }
			mult = op.grid[0][2]/op.grid[2][2];
			for(i=0; i < 6; i++){ op.grid[0][i] -= op.grid[2][i]*mult; }
			mult = op.grid[0][0];
			for(i=0; i < 6; i++){ op.grid[0][i] /= mult; }
			mult = op.grid[1][1];
			for(i=0; i < 6; i++){ op.grid[1][i] /= mult; }
			mult = op.grid[2][2];
			for(i=0; i < 6; i++){ op.grid[2][i] /= mult; }
			return new Matrix(3, 3, new float[]{op.grid[0][3], op.grid[0][4], op.grid[0][5], op.grid[1][3], op.grid[1][4], op.grid[1][5], op.grid[2][3], op.grid[2][4], op.grid[2][5]});
		}
	}
	
	public static float determinantOf(Matrix m){
		if(m.rows == 1 && m.cols == 1){
			return m.grid[0][0];
		}else if(m.rows == 2 && m.cols == 2){
			return((m.grid[0][0]*m.grid[1][1])-(m.grid[0][1]*m.grid[1][0]));
		}else if(m.rows == 3 && m.cols == 3){
			return (m.grid[0][0]*m.grid[1][1]*m.grid[2][2])+(m.grid[0][1]*m.grid[1][2]*m.grid[2][0])+(m.grid[0][2]*m.grid[1][0]*m.grid[2][1])-(m.grid[0][2]*m.grid[1][1]*m.grid[2][0])-(m.grid[2][1]*m.grid[1][2]*m.grid[0][0])-(m.grid[2][2]*m.grid[1][0]*m.grid[0][1]);
		}else{
			throw new ArithmeticException("Determinant can only be found for square matrices of side 3 or less.");
		}
	}
	
	public static Matrix productOf(Matrix A, Matrix B){
		if(A.cols == B.rows){
			float[] store = new float[A.rows*B.cols];
			float product;
			for(int r=0;r<A.rows;r++){
				for(int c=0;c<B.cols;c++){
					product = 0f;
					for(int c2 = 0;c2<A.cols;c2++){
						product += ((A.grid[r][c2])*B.grid[c2][c]);
					}
					store[c+(r*B.cols)]=product;
				}
			}
		return new Matrix(A.rows, B.cols, store);
		}else if(A.rows == 1 && A.cols == 1){
			float[]filler = arrayOf(B);
			for(int i=0;i<filler.length;i++){
				filler[i] *= A.grid[0][0];
			}
			return new Matrix(B.rows, B.cols, filler);
		}else if(B.rows == 1 && B.cols == 1){
			float[]filler = arrayOf(A);
			for(int i=0;i<filler.length;i++){
				filler[i] *= B.grid[0][0];
			}
			return new Matrix(A.rows, A.cols, filler);
		}else{
			throw new ArithmeticException("First array's height must match second array's width.");
		}
	}
	
	public static Matrix productOf(Matrix A, float x){
		float[]filler = arrayOf(A);
		for(int i=0;i<filler.length;i++){
			filler[i] *= x;
		}
		return new Matrix(A.rows, A.cols, filler);
	}
	
	public String toString(){
		String result = "";
		for(int r=0;r<rows;r++){
			result += "( ";
			for(int c=0;c<cols;c++){
				result += String.format("%06.2f ", grid[r][c]);
			}
			result += ")\n";
		}
		return result;
	}
}
