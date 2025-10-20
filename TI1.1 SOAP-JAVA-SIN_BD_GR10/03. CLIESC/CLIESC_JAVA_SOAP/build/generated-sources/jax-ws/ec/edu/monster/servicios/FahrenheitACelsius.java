
package ec.edu.monster.servicios;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para fahrenheitACelsius complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="fahrenheitACelsius"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="fahrenheit" type="{http://www.w3.org/2001/XMLSchema}double"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fahrenheitACelsius", propOrder = {
    "fahrenheit"
})
public class FahrenheitACelsius {

    protected double fahrenheit;

    /**
     * Obtiene el valor de la propiedad fahrenheit.
     * 
     */
    public double getFahrenheit() {
        return fahrenheit;
    }

    /**
     * Define el valor de la propiedad fahrenheit.
     * 
     */
    public void setFahrenheit(double value) {
        this.fahrenheit = value;
    }

}
