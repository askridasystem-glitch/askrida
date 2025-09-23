package com.crux.util.crypt;

public class TksCrypt {
	/**
	 * Default no-args constructor
	 */
	public TksCrypt() {
		initialize();
	}

	private long[] m_lOnBits = new long[31];
	private long[] m_l2Power = new long[31];
	private byte[] m_bytOnBits = new byte[8];
	private byte[] m_byt2Power = new byte[8];
	private byte[] m_InCo = new byte[4];
	private byte[] m_fbsub = new byte[256];
	private byte[] m_rbsub = new byte[256];
	private byte[] m_ptab = new byte[256];
	private byte[] m_ltab = new byte[256];
	private long[] m_ftable = new long[256];
	private long[] m_rtable = new long[256];
	private long[] m_rco = new long[30];
	private long m_Nk;
	private long m_Nb;
	private long m_Nr;
	private byte[] m_fi = new byte[24];
	private byte[] m_ri = new byte[24];
	private long[] m_fkey = new long[120];
	private long[] m_rkey = new long[120];

	private void initialize() {
		m_InCo[0] = 0xB;
		m_InCo[1] = 0xD;
		m_InCo[2] = 0x9;
		m_InCo[3] = 0xE;

		// Could have done this with a loop calculating each value, but simply
		// assigning the values is quicker - BITS SET FROM RIGHT
		m_bytOnBits[0] = 1; // 00000001
		m_bytOnBits[1] = 3; // 00000011
		m_bytOnBits[2] = 7; // 00000111
		m_bytOnBits[3] = 15; // 00001111
		m_bytOnBits[4] = 31; // 00011111
		m_bytOnBits[5] = 63; // 00111111
		m_bytOnBits[6] = 127; // 01111111
		m_bytOnBits[7] = (byte) 255; // 11111111

		// Could have done this with a loop calculating each value, but simply
		// assigning the values is quicker - POWERS OF 2
		m_byt2Power[0] = 1; // 00000001
		m_byt2Power[1] = 2; // 00000010
		m_byt2Power[2] = 4; // 00000100
		m_byt2Power[3] = 8; // 00001000
		m_byt2Power[4] = 16; // 00010000
		m_byt2Power[5] = 32; // 00100000
		m_byt2Power[6] = 64; // 01000000
		m_byt2Power[7] = (byte) 128; // 10000000

		// Could have done this with a loop calculating each value, but simply
		// assigning the values is quicker - BITS SET FROM RIGHT
		m_lOnBits[0] = 1; // 00000000000000000000000000000001
		m_lOnBits[1] = 3; // 00000000000000000000000000000011
		m_lOnBits[2] = 7; // 00000000000000000000000000000111
		m_lOnBits[3] = 15; // 00000000000000000000000000001111
		m_lOnBits[4] = 31; // 00000000000000000000000000011111
		m_lOnBits[5] = 63; // 00000000000000000000000000111111
		m_lOnBits[6] = 127; // 00000000000000000000000001111111
		m_lOnBits[7] = 255; // 00000000000000000000000011111111
		m_lOnBits[8] = 511; // 00000000000000000000000111111111
		m_lOnBits[9] = 1023; // 00000000000000000000001111111111
		m_lOnBits[10] = 2047; // 00000000000000000000011111111111
		m_lOnBits[11] = 4095; // 00000000000000000000111111111111
		m_lOnBits[12] = 8191; // 00000000000000000001111111111111
		m_lOnBits[13] = 16383; // 00000000000000000011111111111111
		m_lOnBits[14] = 32767; // 00000000000000000111111111111111
		m_lOnBits[15] = 65535; // 00000000000000001111111111111111
		m_lOnBits[16] = 131071; // 00000000000000011111111111111111
		m_lOnBits[17] = 262143; // 00000000000000111111111111111111
		m_lOnBits[18] = 524287; // 00000000000001111111111111111111
		m_lOnBits[19] = 1048575; // 00000000000011111111111111111111
		m_lOnBits[20] = 2097151; // 00000000000111111111111111111111
		m_lOnBits[21] = 4194303; // 00000000001111111111111111111111
		m_lOnBits[22] = 8388607; // 00000000011111111111111111111111
		m_lOnBits[23] = 16777215; // 00000000111111111111111111111111
		m_lOnBits[24] = 33554431; // 00000001111111111111111111111111
		m_lOnBits[25] = 67108863; // 00000011111111111111111111111111
		m_lOnBits[26] = 134217727; // 00000111111111111111111111111111
		m_lOnBits[27] = 268435455; // 00001111111111111111111111111111
		m_lOnBits[28] = 536870911; // 00011111111111111111111111111111
		m_lOnBits[29] = 1073741823; // 00111111111111111111111111111111
		m_lOnBits[30] = 2147483647; // 01111111111111111111111111111111

		// Could have done this with a loop calculating each value, but simply
		// assigning the values is quicker - POWERS OF 2
		m_l2Power[0] = 1; // 00000000000000000000000000000001
		m_l2Power[1] = 2; // 00000000000000000000000000000010
		m_l2Power[2] = 4; // 00000000000000000000000000000100
		m_l2Power[3] = 8; // 00000000000000000000000000001000
		m_l2Power[4] = 16; // 00000000000000000000000000010000
		m_l2Power[5] = 32; // 00000000000000000000000000100000
		m_l2Power[6] = 64; // 00000000000000000000000001000000
		m_l2Power[7] = 128; // 00000000000000000000000010000000
		m_l2Power[8] = 256; // 00000000000000000000000100000000
		m_l2Power[9] = 512; // 00000000000000000000001000000000
		m_l2Power[10] = 1024; // 00000000000000000000010000000000
		m_l2Power[11] = 2048; // 00000000000000000000100000000000
		m_l2Power[12] = 4096; // 00000000000000000001000000000000
		m_l2Power[13] = 8192; // 00000000000000000010000000000000
		m_l2Power[14] = 16384; // 00000000000000000100000000000000
		m_l2Power[15] = 32768; // 00000000000000001000000000000000
		m_l2Power[16] = 65536; // 00000000000000010000000000000000
		m_l2Power[17] = 131072; // 00000000000000100000000000000000
		m_l2Power[18] = 262144; // 00000000000001000000000000000000
		m_l2Power[19] = 524288; // 00000000000010000000000000000000
		m_l2Power[20] = 1048576; // 00000000000100000000000000000000
		m_l2Power[21] = 2097152; // 00000000001000000000000000000000
		m_l2Power[22] = 4194304; // 00000000010000000000000000000000
		m_l2Power[23] = 8388608; // 00000000100000000000000000000000
		m_l2Power[24] = 16777216; // 00000001000000000000000000000000
		m_l2Power[25] = 33554432; // 00000010000000000000000000000000
		m_l2Power[26] = 67108864; // 00000100000000000000000000000000
		m_l2Power[27] = 134217728; // 00001000000000000000000000000000
		m_l2Power[28] = 268435456; // 00010000000000000000000000000000
		m_l2Power[29] = 536870912; // 00100000000000000000000000000000
		m_l2Power[30] = 1073741824; // 01000000000000000000000000000000
	}

	private long leftShift(long lValue, int iShiftBits) throws Exception {
		if (iShiftBits == 0) {
			return lValue;

		} else if (iShiftBits == 31) {
			// if (lValue | 1) {
			if ((lValue | 1) == 0x01) {
				return 0x80000000;
			} else {
				return 0;
			}
		} else if (iShiftBits < 0 || iShiftBits > 31) {
			throw new Exception();
		}

		//if (lValue & m_l2Power [31 - iShiftBits]) {
		if ((lValue & m_l2Power[31 - iShiftBits]) == 0x01) {
			return (
				(lValue & m_lOnBits[31 - (iShiftBits + 1)])
					* m_l2Power[iShiftBits])
				| 0x80000000;
		} else {
			return (
				(lValue & m_lOnBits[31 - iShiftBits]) * m_l2Power[iShiftBits]);
		}
	}

	private long rightShift(long lValue, int iShiftBits) throws Exception {
		if (iShiftBits == 0) {
			return lValue;
		} else if (iShiftBits == 31) {
			// if (lValue & 0x80000000) {
			if ((lValue & 0x80000000) == 0x01) {
				return 1;
			} else {
				return 0;
			}
		} else if (iShiftBits < 0 || iShiftBits > 31) {
			throw new Exception();
		}

		long result = (lValue & 0x7FFFFFFE) / m_l2Power[iShiftBits];

		// if (lValue & 0x80000000) {
		if ((lValue & 0x80000000) == 0x01) {
			result = (result | (0x40000000 / m_l2Power[iShiftBits - 1]));
		}

		return result;
	}

	private byte leftShiftByte(byte bytValue, byte bytShiftBits)
		throws Exception {
		if (bytShiftBits == 0) {
			return bytValue;
		} else if (bytShiftBits == 7) {
			// if (bytValue & 1) {
			if ((bytValue & 1) == 0x01) {
				return (byte) 0x80;
			} else {
				return 0;
			}
		} else if (bytShiftBits < 0 || bytShiftBits > 7) {
			throw new Exception();
		}

		return (byte)
			((bytValue & m_bytOnBits[7 - bytShiftBits])
				* m_byt2Power[bytShiftBits]);
	}

	private byte rightShiftByte(byte bytValue, byte bytShiftBits)
		throws Exception {
		if (bytShiftBits == 0) {
			return bytValue;
		} else if (bytShiftBits == 7) {
			// if (bytValue & 0x80) {
			if ((bytValue & 0x80) == 0x01) {
				return 1;
			} else {
				return 0;
			}

		} else if (bytShiftBits < 0 || bytShiftBits > 7) {
			throw new Exception();
		}

		return (byte) (bytValue / m_byt2Power[bytShiftBits]);
	}

	private long rotateLeft(long lValue, int iShiftBits) throws Exception {
		return leftShift(lValue, iShiftBits) | rightShift(lValue, (32 - iShiftBits));
	}

	private byte rotateLeftByte(byte bytValue, byte bytShiftBits)
		throws Exception {
		return (byte)
			(leftShiftByte(bytValue, bytShiftBits)
				| rightShiftByte(bytValue, (byte) (8 - bytShiftBits)));
	}

	private long pack(byte[] b) throws Exception {
		int lCount = 0;
		long lTemp = 0;
		long result = 0;

		for (lCount = 0; lCount < 4; lCount++) {
			lTemp = b[lCount];
			result = result | leftShift(lTemp, (lCount * 8));
		}
		return result;
	}

	private long packFrom(byte[] b, long k) throws Exception {
		int lCount;
		long lTemp;
		long result = 0;

		for (lCount = 0; lCount < 4; lCount++) {
			lTemp = b[lCount + (int) k];
			result = result | leftShift(lTemp, (lCount * 8));
		}
		return result;
	}

	private void unpack(long a, byte[] b) throws Exception {
		b[0] = (byte) (a & m_lOnBits[7]);
		b[1] = (byte) (rightShift(a, 8) & m_lOnBits[7]);
		b[2] = (byte) (rightShift(a, 16) & m_lOnBits[7]);
		b[3] = (byte) (rightShift(a, 24) & m_lOnBits[7]);
	}

	private void unpackFrom(long a, byte[] b, long k) throws Exception {
		b[0 + (int) k] = (byte) (a & m_lOnBits[7]);
		b[1 + (int) k] = (byte) (rightShift(a, 8) & m_lOnBits[7]);
		b[2 + (int) k] = (byte) (rightShift(a, 16) & m_lOnBits[7]);
		b[3 + (int) k] = (byte) (rightShift(a, 24) & m_lOnBits[7]);
	}

	private byte bmul(byte x, byte y) {
		if (x != 0 && y != 0) {
			return m_ptab[((int) (m_ltab[x]) + (int) (m_ltab[y])) % 255];
		} else {
			return 0;
		}
	}

	private byte xtime(byte a) throws Exception {
		byte b;

		// if ((a & 0x80) == 1) {
		if ((a & 0x80) == 0x01) {
			b = 0x1B;
		} else {
			b = 0;
		}

		a = leftShiftByte(a, (byte) 0x01); //LShiftByte(a, (byte) 1);
		a = (byte) ((a ^ b) & 0xFF); //(a ^ b);

		return a;
	}

	private long subByte(long a) throws Exception {
		byte[] b = new byte[4];

		unpack(a, b);
		b[0] = m_fbsub[b[0]];
		b[1] = m_fbsub[b[1]];
		b[2] = m_fbsub[b[2]];
		b[3] = m_fbsub[b[3]];

		return pack(b);
	}

	private long product(long x, long y) throws Exception {
		byte[] xb = new byte[4];
		byte[] yb = new byte[4];

		unpack(x, xb);
		unpack(y, yb);
		return ( bmul(xb[0], yb[0])
			 ^ bmul(xb[1], yb[1])
			 ^ bmul(xb[2], yb[2])
			 ^ bmul(xb[3], yb[3]) & 0xFF );
	}

	private long invMixCol(long x) throws Exception {
		long y;
		long m;
		byte[] b = new byte[3];

		m = pack(m_InCo);
		b[3] = (byte) product(m, x);
		m = rotateLeft(m, 24);
		b[2] = (byte) product(m, x);
		m = rotateLeft(m, 24);
		b[1] = (byte) product(m, x);
		m = rotateLeft(m, 24);
		b[0] = (byte) product(m, x);
		y = pack(b);

		return y;
	}

	private byte byteSub(byte x) throws Exception {
		byte y;

		y = m_ptab[255 - m_ltab[x]];
		x = y;
		x = rotateLeftByte(x, (byte) 0x01);
		y = (byte) (y ^ x & 0xFF);
		x = rotateLeftByte(x, (byte) 0x01);
		y = (byte) (y ^ x & 0xFF);
		x = rotateLeftByte(x, (byte) 0x01);
		y = (byte) (y ^ x & 0xFF);
		x = rotateLeftByte(x, (byte) 0x01);
		y = (byte) (y ^ x & 0xFF);
		y = (byte) (y ^ (byte) 0x63);

		return y;
	}

	private void genTables() throws Exception {
		byte y;
		byte[] b = new byte[3];
		byte ib;

		m_ltab[0] = 0;
		m_ptab[0] = 1;
		m_ltab[1] = 0;
		m_ptab[1] = 3;
		m_ltab[3] = 1;

		//CHECK
		for (int i = 2; i < 256; i++) {
			m_ptab[i] = (byte) (m_ptab[i - 1] ^ xtime(m_ptab[i - 1]) & (long) 0xFF);
			m_ltab[(byte) m_ptab[i]] = (byte) i;
		}

		m_fbsub[0] = 0x63;
		m_rbsub[0x63] = 0;

		for (int i = 0; i < 256; i++) {
			ib = (byte) i;
			y = byteSub(ib);
			m_fbsub[i] = y;
			m_rbsub[y] = (byte) i;
		}

		y = 1;
		for (int i = 0; i < 30; i++) {
			m_rco[i] = y;
			y = xtime(y);
		}

		for (int i = 0; i < 256; i++) {
			y = m_fbsub[i];
			b[3] = (byte) (y ^ xtime(y) & 0xFF);
			b[2] = y;
			b[1] = y;
			b[0] = xtime(y);
			m_ftable[i] = pack(b);

			y = m_rbsub[i];
			b[3] = bmul(m_InCo[0], y);
			b[2] = bmul(m_InCo[1], y);
			b[1] = bmul(m_InCo[2], y);
			b[0] = bmul(m_InCo[3], y);
			m_rtable[i] = pack(b);
		}
	}

	private void gkey(long nb, long nk, byte[] KEY) throws Exception {
		int i;
		int j;
		int k;
		int m;
		int N;
		int C1;
		int C2;
		int C3;
		long[] CipherKey = new long[7];

		m_Nb = nb;
		m_Nk = nk;

		if (m_Nb >= m_Nk) {
			m_Nr = 6 + m_Nb;
		} else {
			m_Nr = 6 + m_Nk;
		}

		C1 = 1;
		if (m_Nb < 8) {
			C2 = 2;
			C3 = 3;
		} else {
			C2 = 3;
			C3 = 4;
		}

		for (j = 0; j <= (nb - 1); j++) {
			m = j * 3;
			m_fi[m] = (byte) ((j + C1) % nb);
			m_fi[m + 1] = (byte) ((j + C2) % nb);
			m_fi[m + 2] = (byte) ((j + C3) % nb);
			m_ri[m] = (byte) ((nb + j - C1) % nb);
			m_ri[m + 1] = (byte) ((nb + j - C2) % nb);
			m_ri[m + 2] = (byte) ((nb + j - C3) % nb);
		}

		N = (int) (m_Nb * (m_Nr + 1));

		for (i = 0; i <= (m_Nk - 1); i++) {
			j = i * 4;
			CipherKey[i] = packFrom(KEY, j);
		}

		for (i = 0; i <= (m_Nk - 1); i++) {
			m_fkey[i] = CipherKey[i];
		}

		j = (int) m_Nk;
		k = 0;

		do {
			m_fkey[j] =
				m_fkey[j
					- (int) m_Nk]
						^ subByte(rotateLeft(m_fkey[j - 1], 24))
						^ m_rco[k] & (long) 0xFF;
			if (m_Nk <= 6) {
				i = 1;
				do {
					m_fkey[i + j] =
						m_fkey[i + j - (int) m_Nk] ^ m_fkey[i + j - 1] & (long) 0xFF;
					i = i + 1;
				} while ((i < m_Nk) && ((i + j) < N));
			} else {
				i = 1;
				do {
					m_fkey[i + j] =
						m_fkey[i + j - (int) m_Nk] ^ m_fkey[i + j - 1] & (long) 0xFF;
					i = i + 1;
				} while ((i < 4) && ((i + j) < N));

				if ((j + 4) < N) {
					m_fkey[j + 4] =
						m_fkey[j + 4 - (int) m_Nk] ^ subByte(m_fkey[j + 3]) & (long) 0xFF;
				}

				i = 5;
				do {
					m_fkey[i + j] =
						m_fkey[i + j - (int) m_Nk] ^ m_fkey[i + j - 1] & (long) 0xFF;
					i = i + 1;
				} while ((i < m_Nk) && ((i + j) < N));
			}

			j = j + (int) m_Nk;
			k = k + 1;
		}
		while (j < N);

		for (j = 0; j <= (m_Nb - 1); j++) {
			m_rkey[j + N - (int) nb] = m_fkey[j];
		}

		i = (int) m_Nb;
		do {
			k = N - (int) m_Nb - i;

			for (j = 0; j <= (m_Nb - 1); j++) {
				m_rkey[k + j] = invMixCol(m_fkey[i + j]);
			}

			i = i + (int) m_Nb;
		} while (i < (N - m_Nb));

		j = N - (int) m_Nb;
		do {
			m_rkey[j - N + (int) m_Nb] = m_fkey[j];
			j = j + 1;
		} while (j < N);

	}

	private void encrypt(byte[] buff) throws Exception {
		int i;
		int j;
		int k;
		int m;
		long[] a = new long[7];
		long[] b = new long[7];
		long[] x;
		long[] y;
		long[] t;

		for (i = 0; i <= (m_Nb - 1); i++) {
			j = i * 4;
			a[i] = packFrom(buff, j);
			a[i] = a[i] ^ m_fkey[i] & (long) 0xFF;
		}

		k = (int) m_Nb;
		x = a;
		y = b;

		for (i = 1; i <= (m_Nr - 1); i++) {
			for (j = 0; j <= (m_Nb - 1); j++) {
				m = j * 3;
				y[j] =
					m_fkey[k]
						^ m_ftable[(int) (x[j] & m_lOnBits[7])]
						^ rotateLeft(
							m_ftable[(int)
								(rightShift(x[m_fi[m]], 8) & m_lOnBits[7])],
							8)
						^ rotateLeft(
							m_ftable[(int)
								(rightShift(x[m_fi[m + 1]], 16) & m_lOnBits[7])],
							16)
						^ rotateLeft(
							m_ftable[(int)
								(rightShift(x[m_fi[m + 2]], 24) & m_lOnBits[7])],
							24)
                              & (long) 0xFF;
				k = k + 1;
			}
			t = x;
			x = y;
			y = t;
		}

		for (j = 0; j <= (m_Nb - 1); j++) {
			m = j * 3;
			y[j] =
				m_fkey[k]
					^ m_fbsub[(int) (x[j] & m_lOnBits[7])]
					^ rotateLeft(
						m_fbsub[(int) (rightShift(x[m_fi[m]], 8) & m_lOnBits[7])],
						8)
					^ rotateLeft(
						m_fbsub[(int)
							(rightShift(x[m_fi[m + 1]], 16) & m_lOnBits[7])],
						16)
					^ rotateLeft(
						m_fbsub[(int)
							(rightShift(x[m_fi[m + 2]], 24) & m_lOnBits[7])],
						24)
                         & (long) 0xFF;
			k = k + 1;
		}

		for (i = 0; i <= (m_Nb - 1); i++) {
			j = i * 4;
			unpackFrom(y[i], buff, j);
			x[i] = 0;
			y[i] = 0;
		}
	}

	private void decrypt(byte[] buff) throws Exception {
		int i;
		int j;
		int k;
		int m;
		long[] a = new long[7];
		long[] b = new long[7];
		long[] x;
		long[] y;
		long[] t;

		for (i = 0; i <= (m_Nb - 1); i++) {
			j = i * 4;
			a[i] = packFrom(buff, j);
			a[i] = a[i] ^ m_rkey[i] & (long) 0xFF;
		}

		k = (int) m_Nb;
		x = a;
		y = b;

		for (i = 1; i <= (m_Nr - 1); i++) {
			for (j = 0; j <= (m_Nb - 1); j++) {
				m = j * 3;
				y[j] =
					m_rkey[k]
						^ m_rtable[(int) (x[j] & m_lOnBits[7])]
						^ rotateLeft(
							m_rtable[(int)
								(rightShift(x[m_ri[m]], 8) & m_lOnBits[7])],
							8)
						^ rotateLeft(
							m_rtable[(int)
								(rightShift(x[m_ri[m + 1]], 16) & m_lOnBits[7])],
							16)
						^ rotateLeft(
							m_rtable[(int)
								(rightShift(x[m_ri[m + 2]], 24) & m_lOnBits[7])],
							24)
                              & (long) 0xFF;
				k = k + 1;
			}
			t = x;
			x = y;
			y = t;
		}

		for (j = 0; j <= (m_Nb - 1); j++) {
			m = j * 3;
			y[j] =
				m_rkey[k]
					^ m_rbsub[(int) (x[j] & m_lOnBits[7])]
					^ rotateLeft(
						m_rbsub[(int) (rightShift(x[m_ri[m]], 8) & m_lOnBits[7])],
						8)
					^ rotateLeft(
						m_rbsub[(int)
							(rightShift(x[m_ri[m + 1]], 16) & m_lOnBits[7])],
						16)
					^ rotateLeft(
						m_rbsub[(int)
							(rightShift(x[m_ri[m + 2]], 24) & m_lOnBits[7])],
						24)
                         & (long) 0xFF;
			k = k + 1;
		}

		for (i = 0; i <= (m_Nb - 1); i++) {
			j = i * 4;
			unpackFrom(y[i], buff, j);
			x[i] = 0;
			y[i] = 0;
		}
	}

	private void copyBytes(
		byte[] bytDest,
		long lDestStart,
		byte[] bytSource,
		long lSourceStart,
		long lLength) {
		long lCount;
		lCount = 0;
		do {
			bytDest[(int) lDestStart + (int) lCount] =
				bytSource[(int) lSourceStart + (int) lCount];
			lCount = lCount + 1;
		} while (lCount != lLength);
	}

	private boolean isInitialized(Object vArray) {
		return vArray != null;
	}

	/**
	 * Takes the message, whatever the size, and password in one call and does
	 * everything for you to return an encoded/encrypted message
	 * 
	 * @param bytMessage
	 *            message buffer to be encrypted
	 * @param bytPassword
	 *            password buffer used for decrypting key
	 * @return buffer of encrypted data
	 * @throws java.lang.Exception
	 */
	public byte[] encryptData(byte[] bytMessage, byte[] bytPassword)
		throws Exception {
		byte[] bytKey = new byte[31];
		byte[] bytIn;
		byte[] bytOut;
		byte[] bytTemp = new byte[31];
		long lCount;
		long lLength;
		long lEncodedLength;
		byte[] bytLen = new byte[3];
		long lPosition;

		if (!isInitialized(bytMessage)) {
			throw new Exception("Message must be supllied");
		}

		if (!isInitialized(bytPassword)) {
			throw new Exception("Password must be supplied");
		}

		// Use first 32 bytes of the password for the key
		for (lCount = 0;
			(lCount < bytPassword.length && lCount != 31);
			lCount++) {
			bytKey[(int) lCount] = bytPassword[(int) lCount];
		}

		// Prepare the key; assume 256 bit block and key size
		genTables();
		gkey(8, 8, bytKey);

		// We are going to put the message size on the front of the message
		// in the first 4 bytes. If the length is more than a max int we are
		// in trouble
		lLength = bytMessage.length; //bytMessage.length + 1;
		lEncodedLength = lLength + 4;

		// The encoded length includes the 4 bytes stuffed on the front
		// and is padded out to be modulus 32
		if ((lEncodedLength % 32) != 0) {
			lEncodedLength = lEncodedLength + 32 - (lEncodedLength % 32);
		}

		bytIn = new byte[(int) lEncodedLength - 1];
		bytOut = new byte[(int) lEncodedLength - 1];

		// Put the length on the front
		// CopyMemory VarPtr(bytIn(0)), VarPtr(lLength), 4
		unpack(lLength, bytIn);

		// Put the rest of the message after it
		// CopyMemory VarPtr(bytIn(4)), VarPtr(bytMessage(0)), lLength
		copyBytes(bytIn, 4, bytMessage, 0, lLength);

		// Encrypt a block at a time
		for (lCount = 0; lCount <= lEncodedLength - 1; lCount += 32) {
			// CopyMemory VarPtr(bytTemp(0)), VarPtr(bytIn(lCount)), 32
			copyBytes(bytTemp, 0, bytIn, lCount, 32);
			// Encrypt temp
			encrypt(bytTemp);
			// CopyMemory VarPtr(bytOut(lCount)), VarPtr(bytTemp(0)), 32
			copyBytes(bytOut, lCount, bytTemp, 0, 32);
		}

		return bytOut;
	}

	/**
	 * Takes the message, whatever the size, and password in one call and does
	 * everything for you to return an decoded/decrypted message
	 * 
	 * @param bytIn
	 *            encrypted message buffer to be decrypted
	 * @param bytPassword
	 *            password buffer used for decrypting key
	 * @return buffer of decrypted data
	 * @throws java.lang.Exception
	 */
	public byte[] decryptData(byte[] bytIn, byte[] bytPassword)
		throws Exception {
		byte[] bytMessage;
		byte[] bytKey = new byte[31];
		byte[] bytOut;
		byte[] bytTemp = new byte[31];
		byte[] bytLen = new byte[3];
		long lCount;
		long lLength;
		long lEncodedLength;
		long lPosition;

		if (!isInitialized(bytIn)) {
			throw new Exception("Message in must be supplied");
		}

		if (!isInitialized(bytPassword)) {
			throw new Exception("Password must be supplied");
		}

		lEncodedLength = bytIn.length; //bytIn.length + 1;

		if ((lEncodedLength % 32) != 0) {
			// throw new Exception ("Length is not in 32 byte");
			return null;
		}

		// Use first 32 bytes of the password for the key
		for (lCount = 0;
			(lCount < bytPassword.length) && (lCount != 31);
			lCount++) {
			bytKey[(int) lCount] = bytPassword[(int) lCount];
		}

		// Prepare the key; assume 256 bit block and key size
		genTables();
		gkey(8, 8, bytKey);

		// The output array needs to be the same size as the input array
		bytOut = new byte[(int) lEncodedLength - 1];

		// Decrypt a block at a time
		for (lCount = 0; lCount <= lEncodedLength - 1; lCount += 32) {
			// CopyMemory VarPtr(bytTemp(0)), VarPtr(bytIn(lCount)), 32
			copyBytes(bytTemp, 0, bytIn, lCount, 32);
			// Decrypt temp
			decrypt(bytTemp);
			// CopyMemory VarPtr(bytOut(lCount)), VarPtr(bytTemp(0)), 32
			copyBytes(bytOut, lCount, bytTemp, 0, 32);
		}

		// Get the original length of the string from the first 4 bytes
		// CopyMemory VarPtr(lLength), VarPtr(bytOut(0)), 4
		lLength = pack(bytOut);

		// Make sure the length is consistent with our data
		if (lLength > (lEncodedLength - 4)) {
			throw new Exception("Length of data is not consitent");
		}

		// Prepare the output message byte array
		bytMessage = new byte[(int) lLength - 1];

		// CopyMemory VarPtr(bytMessage(0)), VarPtr(bytOut(4)), lLength
		copyBytes(bytMessage, 0, bytOut, 4, lLength);

		return bytMessage;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String message = "#EJKTA.HP.04.02.06#E0040A#351983002761262#1#";
		String key = "SU3k@msel";

		TksCrypt tksCrypt = new TksCrypt();

		byte[] en = tksCrypt.encryptData(message.getBytes(), key.getBytes());
		System.out.println("Encrypted : " + new String(en));

		byte[] de = tksCrypt.decryptData(en, key.getBytes());
		System.out.println("Decrypted : " + new String(de));
	}

}