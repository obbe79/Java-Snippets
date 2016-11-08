class AtmData {
    
    constexpr static double pi = 3.14; // hPa
    constexpr static double p0 = 1013.25; // hPa
    constexpr static double T0 = 288.16;  // K
	constexpr static double g0 = 9.80665; // m/s2
	constexpr static double a = 0.0065;  // °C/m (lapse rate)
	constexpr static double R = 287.04;  // Costante dell'Aria J/K/kg
    constexpr static double H0 = 0;
	constexpr static double Tb = 216.65;  // Temperatura di Troposfera
    constexpr static double Hb = 11000;   // Quota di troposfera

  public:
     AtmData();
     AtmData(double);
     AtmData(double, double);
     AtmData(double, double, double);
     
     double setH(double h);
     double setPress(double);
     double setTemp(double);
     double getBaroAlt(void);
     double getPmeas(void);
     
  private:
     double radius;
     double h;
     double p0_atm;
     double T0_atm;
     double h_bar;
     double p_mis;
     void baroalt();
      
};

// Implementazioni
AtmData::AtmData() {  // Costruttore
    h = H0;
    p0_atm = p0;
    T0_atm = T0;
}

AtmData::AtmData(double alt) { // Costruttore con 1 parametro
        h = alt;
}
AtmData::AtmData(double alt, double p_z) { // Costruttore con 2 parametri
        h = alt;
        p0_atm = p_z;
}
AtmData::AtmData(double alt, double p_z, double T_z)  { // Costruttore con 3 parametri
        h = alt;
        p0_atm = p_z;
        T0_atm = T_z;
}


void AtmData::baroalt() {

	/*Restituisce la quota barometrica rispetto a ISA ipotizzando che
	% l'atmosfera reale abbia l'andamento di quella standard con parametri T0 e
	% p0 modificati. Valida fino a 20 km.
	% Pressione in millibar (hPa) e Temperatura in Kelvin*/
	
	
	if (h <= 11000) {
		// Calcolo della pressione in atmosfera "reale" a terra (con p0_atm e T0_atm)
		p_mis = p0_atm*pow((T0_atm / (T0_atm + a*(h - H0))) , (g0 / R / a));
		// Con la pressione "misurata" entro nell'ISA per ricavare baroalt
		h_bar = (1 / a)*( T0/pow((p_mis / p0),(R*a / g0)) - T0);
	}
	else {
		// La quota resta uguale.
		// Calcolo della pressione in atmosfera "reale" a terra (con p0_atm e T0_atm)
		double pb = p0_atm*pow((T0_atm / (T0_atm + a*(Hb - H0))) , (g0 / R / a));
		p_mis = pb*exp(-g0*(h - Hb) / R / 216.65);
		// Con la pressione "misurata" entro nell'ISA per ricavare baroalt
		h_bar = Hb - (R*Tb / g0)*log(p_mis / pb);
	}

}

double AtmData::getBaroAlt() {
    this->baroalt();
    return h_bar;
}

double AtmData::getPmeas() {
    this->baroalt();
    return p_mis;
}