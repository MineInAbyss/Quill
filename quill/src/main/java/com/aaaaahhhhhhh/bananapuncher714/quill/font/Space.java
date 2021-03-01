package com.aaaaahhhhhhh.bananapuncher714.quill.font;

public enum Space {
	_N1( '\uF801', -1 ),
	_N2( '\uF802', -2 ),
	_N4( '\uF804', -4 ),
	_N8( '\uF808', -8 ),
	_N16( '\uF809', -16 ),
	_N32( '\uF80A', -32 ),
	_N64( '\uF80B', -64 ),
	_N128( '\uF80C', -128 ),
	_N256( '\uF80D', -256 ),
	_N512( '\uF80E', -512 ),
	_N1024( '\uF80F', -1024 ),
	_1( '\uF821', 1 ),
	_2( '\uF822', 2 ),
	_4( '\uF824', 4 ),
	_8( '\uF828', 8 ),
	_16( '\uF829', 16 ),
	_32( '\uF82A', 32 ),
	_64( '\uF82B', 64 ),
	_128( '\uF82C', 128 ),
	_256( '\uF82D', 256 ),
	_512( '\uF82E', 512 ),
	_1024( '\uF82F', 1024 );
	
	private char c;
	private int v;
	
	Space( char c, int v ) {
		this.c = c;
		this.v = v;
	}

	public char getChar() {
		return c;
	}
	
	public int getValue() {
		return v;
	}
	
	public static Space[] negatives() {
		return new Space[] {
				 _N1,
		         _N2,
		         _N4,
		         _N8,
		         _N16,
		         _N32,
		         _N64,
		         _N128,
		         _N256,
		         _N512,
		         _N1024 };
	}
	
	public static Space[] positives() {
		return new Space[] {
				 _1,
		         _2,
		         _4,
		         _8,
		         _16,
		         _32,
		         _64,
		         _128,
		         _256,
		         _512,
		         _1024 };
	}
	
	public static String getSpaceFor( int offset ) {
		StringBuilder builder = new StringBuilder();
		Space[] spaces = offset < 0 ? Space.negatives() : Space.positives();
		int index = spaces.length - 1;
		Space current = spaces[ index ];
		while ( offset != 0 ) {
			while ( Math.abs( offset ) - Math.abs( current.getValue() ) < 0 ) current = spaces[ --index ];
			offset -= current.getValue();
			builder.append( current.getChar() );
		}
		
		return builder.toString();
	}
}
