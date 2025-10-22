package ec.edu.monster.utils;

/**
 * Conversiones de unidades:
 * - 3 métodos de longitud
 * - 3 métodos de masa
 * - 3 métodos de temperatura
 *
 * Devuelven -1 si la entrada no pasa la validación (por ejemplo valores negativos donde no aplican).
 *
 * @author rodri
 */
public class ConversionUnidades {

    // --------------------
    // LONGITUD (no negativas)
    // --------------------

    // centímetros -> metros
    public double centimetrosAMetros(double centimetros) {
        if (!validarNoNegativo(centimetros)) {
            return -1;
        }
        return centimetros / 100.0;
    }

    // metros -> centímetros
    public double metrosACentimetros(double metros) {
        if (!validarNoNegativo(metros)) {
            return -1;
        }
        return metros * 100.0;
    }

    // metros -> kilómetros
    public double metrosAKilometros(double metros) {
        if (!validarNoNegativo(metros)) {
            return -1;
        }
        return metros / 1000.0;
    }

    // --------------------
    // MASA (no negativas)
    // --------------------

    // toneladas -> libras
    public double toneladaALibra(double tonelada) {
        if (!validarNoNegativo(tonelada)) {
            return -1;
        }
        // 1 tonelada (short ton) ≈ 2204.6226218 lb
        return tonelada * 2204.6226218;
    }

    // kilogramos -> libras
    public double kilogramoALibra(double kilogramo) {
        if (!validarNoNegativo(kilogramo)) {
            return -1;
        }
        return kilogramo * 2.2046226218;
    }

    // gramos -> kilogramos
    public double gramoAKilogramo(double gramos) {
        if (!validarNoNegativo(gramos)) {
            return -1;
        }
        return gramos / 1000.0;
    }

    // --------------------
    // TEMPERATURA
    // --------------------
    // Nota: temperaturas en Celsius y Fahrenheit pueden ser negativas;
    // la validación de no-negativo se aplica donde corresponde (p. ej. Kelvin resultante).

    // Celsius -> Fahrenheit
    public double celsiusAFahrenheit(double celsius) {
        return celsius * 1.8 + 32.0;
    }

    // Fahrenheit -> Celsius
    public double fahrenheitACelsius(double fahrenheit) {
        // usar 5.0/9.0 para evitar división entera
        return (fahrenheit - 32.0) * (5.0 / 9.0);
    }

    // Celsius -> Kelvin (valida que el resultado no sea negativo)
    public double celsiusAKelvin(double celsius) {
        double kelvin = celsius + 273.15;
        if (!validarNoNegativo(kelvin)) {
            return -1;
        }
        return kelvin;
    }

    // --------------------
    // VALIDACIONES
    // --------------------

    // valida que el número no sea negativo (útil para longitud, masa y Kelvin)
    private boolean validarNoNegativo(double number) {
        return number >= 0.0;
    }
}
