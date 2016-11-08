class AtmData {
    
    static public double pi = 3.14; // hPa
    static public double p0 = 1013.25; // hPa
    static public double T0 = 288.16;  // K
	static public double g0 = 9.80665; // m/s2
	static public double a = 0.0065;  // C/m (lapse rate)
	static public double R = 287.04;  // Costante dell'Aria J/K/kg
    static public double H0 = 0;
	static public double Tb = 216.65;  // Temperatura di Troposfera
    static public double Hb = 11000;   // Quota di troposfera


    /*
    public double setH(double h);
    public double setPress(double p);
    public double setTemp(double T);
*/
     
    private double radius;
    private double h;
    private double p0_atm;
    private double T0_atm;
    private double h_bar;
    private double p_mis;


 public AtmData() {  // Costruttore
    h = H0;
    p0_atm = p0;
    T0_atm = T0;
}

public AtmData(double alt) { // Costruttore con 1 parametro
        h = alt;
}
public AtmData(double alt, double p_z) { // Costruttore con 2 parametri
        h = alt;
        p0_atm = p_z;
}
public AtmData(double alt, double p_z, double T_z)  { // Costruttore con 3 parametri
        h = alt;
        p0_atm = p_z;
        T0_atm = T_z;
}


private void baroalt(){

	/*Restituisce la quota barometrica rispetto a ISA ipotizzando che
	% l'atmosfera reale abbia l'andamento di quella standard con parametri T0 e
	% p0 modificati. Valida fino a 20 km.
	% Pressione in millibar (hPa) e Temperatura in Kelvin*/
	
	
	if (h <= 11000) {
		// Calcolo della pressione in atmosfera "reale" a terra (con p0_atm e T0_atm)
		p_mis = p0_atm*Math.pow((T0_atm / (T0_atm + a*(h - H0))) , (g0 / R / a));
		// Con la pressione "misurata" entro nell'ISA per ricavare baroalt
		h_bar = (1 / a)*( T0/Math.pow((p_mis / p0),(R*a / g0)) - T0);
	}
	else {
		// La quota resta uguale.
		// Calcolo della pressione in atmosfera "reale" a terra (con p0_atm e T0_atm)
		double pb = p0_atm*Math.pow((T0_atm / (T0_atm + a*(Hb - H0))) , (g0 / R / a));
		p_mis = pb*Math.exp(-g0*(h - Hb) / R / 216.65);
		// Con la pressione "misurata" entro nell'ISA per ricavare baroalt
		h_bar = Hb - (R*Tb / g0)*Math.log(p_mis / pb);
	}

}

public double getBaroAlt() {
    this.baroalt();
    return h_bar;
}

public double getPmeas() {
    this.baroalt();
    return p_mis;
}
}